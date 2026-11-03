require('./otel');

const express = require('express');
const { Kafka } = require('kafkajs');
const path = require('path');
// pino records become dashboard structured logs, linked to the active trace.
const log = require('pino')();

const app = express();
const port = process.env.PORT || 3000;

// Parse Aspire connection string
function getKafkaConfig() {
    const connectionString = process.env.ConnectionStrings__messaging || 'localhost:9092';
    const brokers = connectionString.split(';')[0];
    return { brokers: [brokers] };
}

// Kafka setup
const kafka = new Kafka({
    clientId: 'node-dashboard',
    ...getKafkaConfig()
});

// In-memory storage for latest readings and alerts
const latestReadings = new Map();
const recentAlerts = [];
const MAX_ALERTS = 50;

// Consumer setup
const consumer = kafka.consumer({ groupId: 'dashboard-group' });
const readingsConsumer = kafka.consumer({ groupId: 'dashboard-readings-group' });

async function startConsumers() {
    try {
        // Consumer for alerts
        await consumer.connect();
        await consumer.subscribe({ topic: 'sensor-alerts', fromBeginning: false });
        await readingsConsumer.connect();
        await readingsConsumer.subscribe({ topic: 'sensor-readings', fromBeginning: false });
        
        await consumer.run({
            eachMessage: async ({ topic, partition, message }) => {
                try {
                    const alert = JSON.parse(message.value.toString());
                    recentAlerts.unshift(alert);
                    if (recentAlerts.length > MAX_ALERTS) {
                        recentAlerts.pop();
                    }
                    log.warn({ sensorId: alert.sensorId, reason: alert.reason }, 'alert received');
                } catch (err) {
                    log.error({ err }, 'error processing alert');
                }
            }
        });
        
        // Consumer for readings
        await readingsConsumer.run({
            eachMessage: async ({ topic, partition, message }) => {
                try {
                    const reading = JSON.parse(message.value.toString());
                    
                    // Store latest reading per sensor with recent history
                    if (!latestReadings.has(reading.SensorId)) {
                        latestReadings.set(reading.SensorId, {
                            sensorId: reading.SensorId,
                            location: reading.Location,
                            temperature: reading.Temperature,
                            humidity: reading.Humidity,
                            timestamp: reading.Timestamp,
                            history: []
                        });
                    }
                    
                    const sensor = latestReadings.get(reading.SensorId);
                    sensor.temperature = reading.Temperature;
                    sensor.humidity = reading.Humidity;
                    sensor.timestamp = reading.Timestamp;
                    sensor.location = reading.Location;
                    
                    // Keep last 20 readings for sparkline
                    sensor.history.push({
                        temperature: reading.Temperature,
                        humidity: reading.Humidity,
                        timestamp: reading.Timestamp
                    });
                    
                    if (sensor.history.length > 20) {
                        sensor.history.shift();
                    }
                    
                } catch (err) {
                    log.error({ err }, 'error processing reading');
                }
            }
        });
        
        log.info('Kafka consumers started');
    } catch (err) {
        log.warn({ err }, 'Kafka consumers not ready; retrying in 5s');
        await Promise.all([consumer.disconnect(), readingsConsumer.disconnect()]);
        // Retry after delay
        setTimeout(startConsumers, 5000);
    }
}

// Start consumers
startConsumers();

// Configure EJS
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

// Routes
app.get('/', (req, res) => {
    res.render('index');
});

app.get('/api/readings', (req, res) => {
    const readings = Array.from(latestReadings.values());
    res.json(readings);
});

app.get('/api/alerts', (req, res) => {
    res.json(recentAlerts.slice(0, 20));
});

app.get('/health', (req, res) => {
    res.json({ status: 'healthy' });
});

// Graceful shutdown
process.on('SIGTERM', async () => {
    log.info('SIGTERM received, shutting down');
    await consumer.disconnect();
    await readingsConsumer.disconnect();
    process.exit(0);
});

app.listen(port, () => {
    log.info({ port, broker: getKafkaConfig().brokers[0] }, 'dashboard listening');
});
