#!/usr/bin/env python3
import os
import json
import logging

logger = logging.getLogger(__name__)

# --- OpenTelemetry setup (must run before Flask app creation) ---
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry import trace, metrics
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.instrumentation.flask import FlaskInstrumentor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    import logging

    resource = Resource.create({
        "service.name": os.environ.get("OTEL_SERVICE_NAME", "python-consumer"),
    })

    # Traces
    trace_provider = TracerProvider(resource=resource)
    trace_provider.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))
    trace.set_tracer_provider(trace_provider)

    # Metrics
    metric_reader = PeriodicExportingMetricReader(OTLPMetricExporter())
    meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])
    metrics.set_meter_provider(meter_provider)
    logger_provider = LoggerProvider(resource=resource)
    logger_provider.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter()))
    handler = LoggingHandler(logger_provider=logger_provider)
    logging.getLogger().addHandler(handler)
    logging.getLogger().setLevel(logging.INFO)

    _flask_instrumentor = FlaskInstrumentor()

import threading
from collections import defaultdict, deque
from confluent_kafka import Consumer, Producer, KafkaError
from flask import Flask, jsonify
from waitress import serve

app = Flask(__name__)

# Instrument Flask app if OTel is enabled
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    _flask_instrumentor.instrument_app(app)

# Rolling aggregates: store last 5 minutes of readings per sensor
aggregates = defaultdict(lambda: {
    'readings': deque(maxlen=150),  # 150 readings = 5 min at 2s intervals
    'avg_temperature': 0.0,
    'avg_humidity': 0.0,
    'last_update': None
})

alerts = deque(maxlen=100)  # Keep last 100 alerts

def get_kafka_config():
    connection_string = os.environ.get('ConnectionStrings__messaging', 'localhost:9092')
    bootstrap_servers = connection_string.split(';')[0]  # Handle Aspire format
    return bootstrap_servers

def consume_events():
    """Kafka consumer thread"""
    consumer_config = {
        'bootstrap.servers': get_kafka_config(),
        'group.id': 'python-consumer-group',
        'auto.offset.reset': 'earliest'
    }
    
    producer_config = {
        'bootstrap.servers': get_kafka_config()
    }
    
    consumer = Consumer(consumer_config)
    producer = Producer(producer_config)
    consumer.subscribe(['sensor-readings'])
    
    logger.info("Consumer connected to Kafka at %s", get_kafka_config())
    logger.info("Subscribed to topic: sensor-readings")
    
    while True:
        msg = consumer.poll(timeout=1.0)
        
        if msg is None:
            continue
        if msg.error():
            if msg.error().code() == KafkaError._PARTITION_EOF:
                continue
            logger.error("Consumer error: %s", msg.error())
            continue
        
        try:
            event = json.loads(msg.value().decode('utf-8'))
            sensor_id = event['SensorId']
            temperature = event['Temperature']
            humidity = event['Humidity']
            location = event['Location']
            timestamp = event['Timestamp']
            
            # Update aggregates
            sensor_data = aggregates[sensor_id]
            sensor_data['readings'].append({
                'temperature': temperature,
                'humidity': humidity,
                'timestamp': timestamp
            })
            
            # Calculate rolling averages
            readings = sensor_data['readings']
            sensor_data['avg_temperature'] = sum(r['temperature'] for r in readings) / len(readings)
            sensor_data['avg_humidity'] = sum(r['humidity'] for r in readings) / len(readings)
            sensor_data['last_update'] = timestamp
            sensor_data['location'] = location
            
            logger.info("Processed %s at %s: %.1f°C, %.1f%% "
                  "(avg: %.1f°C, %.1f%%)", sensor_id, location, temperature, humidity,
                  sensor_data['avg_temperature'], sensor_data['avg_humidity'])
            
            # Detect anomalies
            if temperature > 35 or humidity > 90:
                alert = {
                    'sensorId': sensor_id,
                    'location': location,
                    'temperature': temperature,
                    'humidity': humidity,
                    'timestamp': timestamp,
                    'reason': []
                }
                
                if temperature > 35:
                    alert['reason'].append(f"High temperature: {temperature:.1f}°C")
                if humidity > 90:
                    alert['reason'].append(f"High humidity: {humidity:.1f}%")
                
                alert['reason'] = ', '.join(alert['reason'])
                alerts.append(alert)
                
                # Publish alert to Kafka
                alert_json = json.dumps(alert)
                producer.produce('sensor-alerts', key=sensor_id, value=alert_json)
                producer.flush()
                
                logger.warning("ALERT: %s - %s", sensor_id, alert['reason'])
        
        except Exception as e:
            logger.error("Error processing message: %s", e)

@app.route('/health')
def health():
    return jsonify({'status': 'healthy'}), 200

@app.route('/api/aggregates')
def get_aggregates():
    result = {}
    for sensor_id, data in aggregates.items():
        result[sensor_id] = {
            'sensorId': sensor_id,
            'location': data.get('location', 'Unknown'),
            'avgTemperature': round(data['avg_temperature'], 1),
            'avgHumidity': round(data['avg_humidity'], 1),
            'lastUpdate': data['last_update'],
            'readingCount': len(data['readings'])
        }
    return jsonify(result)

@app.route('/api/alerts')
def get_alerts():
    return jsonify(list(alerts))

if __name__ == '__main__':
    # Start Kafka consumer in background thread
    consumer_thread = threading.Thread(target=consume_events, daemon=True)
    consumer_thread.start()
    
    # Start Flask server
    port = int(os.environ.get('PORT', 8000))
    logger.info("Starting Flask server on port %d", port)
    serve(app, host='0.0.0.0', port=port)
