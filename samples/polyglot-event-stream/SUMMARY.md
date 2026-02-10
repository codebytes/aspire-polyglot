# Polyglot Event Stream Sample - Summary

## Files Created

### .NET Projects (3 projects)
1. **AppHost/AppHost.csproj** + **Program.cs**
   - Aspire orchestrator with Kafka, KafkaUI, Python, and Node.js integration
   - Suppresses experimental API warnings for Python/Node.js hosting

2. **EventProducer/EventProducer.csproj** + **Program.cs**
   - ASP.NET Core producer with BackgroundService
   - Publishes sensor readings every 2 seconds to Kafka
   - REST endpoints: /api/sensors, POST /api/events, /health

3. **ServiceDefaults/ServiceDefaults.csproj** + **Extensions.cs**
   - Standard Aspire service defaults with OpenTelemetry

### Python Consumer
- **main.py**: Kafka consumer with Flask API
- **requirements.txt**: confluent-kafka, flask, waitress
- Computes rolling aggregates and detects anomalies
- Publishes alerts to sensor-alerts topic
- REST API: /api/aggregates, /api/alerts, /health

### Node.js Dashboard
- **app.js**: Express server with Kafka consumers
- **package.json**: express, kafkajs, ejs
- **views/index.ejs**: IoT dashboard with real-time visualization
- Dark theme with color-coded sensor status
- Auto-refreshing every 5 seconds

### Documentation
- **README.md**: Comprehensive guide with architecture, concepts, and usage
- **PolyglotEventStream.slnx**: Solution file
- **.gitignore**: Exclusions for .NET, Python, Node.js

## Key Features

### Event Streaming Architecture
- Kafka-based event streaming (vs. RabbitMQ task queues)
- Multiple independent consumers
- Event replay capability
- Real-time processing pipeline

### Sensor Simulation
- 5 simulated sensors (warehouses, cold storage, server room, production floor)
- Random temperature/humidity variations around base values
- 2-second publish interval

### Data Flow
1. **Producer** → sensor-readings topic
2. **Python Consumer** → processes → sensor-alerts topic
3. **Dashboard** → consumes both topics → visualizes

### Monitoring Features
- Real-time sensor cards with sparklines
- Color-coded status (normal/warning/critical)
- Alert feed with recent anomalies
- Rolling average calculations (5-minute window)

## Build Status
✅ Solution builds successfully with no errors
✅ All projects reference correct Aspire 9.2.1 packages
✅ Python and Node.js integration configured
✅ Kafka with KafkaUI enabled

## Next Steps
1. Run with `dotnet run` from AppHost directory
2. Access Aspire Dashboard (displayed in console)
3. View Node.js IoT Dashboard via Aspire endpoints
4. Monitor Kafka topics via KafkaUI
5. Send custom events via Producer REST API
