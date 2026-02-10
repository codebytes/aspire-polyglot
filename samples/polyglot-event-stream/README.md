# Polyglot Event Streaming with Kafka

This sample demonstrates **event streaming** using Kafka with a polyglot architecture orchestrated by .NET Aspire. Unlike task queues (like RabbitMQ), event streaming allows multiple consumers to independently process the same events for different purposes.

## Architecture

- **.NET Producer** (`EventProducer`): Publishes sensor readings to Kafka every 2 seconds
- **Python Consumer** (`python-consumer`): Consumes events, computes aggregates, detects anomalies, publishes alerts
- **Node.js Dashboard** (`node-dashboard`): Real-time IoT monitoring dashboard consuming events from Kafka
- **Kafka + KafkaUI**: Message broker with web-based management UI

## Key Differences from polyglot-task-queue

| Feature | polyglot-task-queue | polyglot-event-stream |
|---------|---------------------|----------------------|
| **Messaging** | RabbitMQ (task queue) | Kafka (event streaming) |
| **Paradigm** | Work queue - each task consumed once | Event stream - multiple consumers, replay capability |
| **Pattern** | Task distribution | Event sourcing / pub-sub |
| **Use Case** | Distribute work among workers | Real-time analytics, monitoring, event processing |
| **Aspire API** | `AddRabbitMQ()` | `AddKafka()` + `WithKafkaUI()` |

## Components

### EventProducer (.NET)

- Simulates 5 IoT sensors across different locations
- Publishes sensor readings (temperature, humidity) every 2 seconds
- REST API:
  - `GET /api/sensors` - List sensor configurations
  - `POST /api/events` - Manually publish a custom event
  - `GET /health` - Health check

### Python Consumer

- Subscribes to `sensor-readings` topic
- Computes rolling averages (last 5 minutes per sensor)
- Detects anomalies (temp > 35°C or humidity > 90%)
- Publishes alerts to `sensor-alerts` topic
- REST API:
  - `GET /api/aggregates` - Get computed aggregates per sensor
  - `GET /api/alerts` - Get recent alerts
  - `GET /health` - Health check

### Node.js Dashboard

- Consumes both `sensor-readings` and `sensor-alerts` topics
- Real-time web dashboard showing:
  - Live sensor readings with color-coded status
  - Sparkline charts showing recent trends
  - Alert feed with recent anomalies
- Auto-refreshes every 5 seconds
- Dark theme optimized for monitoring

## Running the Sample

### Prerequisites

- .NET 9.0 SDK
- Python 3.8+
- Node.js 18+
- Docker (for Kafka)

### Quick Start

1. **Navigate to the AppHost directory:**
   ```bash
   cd AppHost
   ```

2. **Run with Aspire:**
   ```bash
   dotnet run
   ```

3. **Access the applications:**
   - **Aspire Dashboard**: http://localhost:15888 (check console for actual URL)
   - **Node.js Dashboard**: Check Aspire dashboard for the `dashboard` endpoint
   - **KafkaUI**: Check Aspire dashboard for the `messaging-kafkaui` endpoint
   - **Producer API**: Check Aspire dashboard for the `producer` endpoint

### Manual Event Publishing

Send a custom sensor event:

```bash
curl -X POST http://localhost:<producer-port>/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "sensorId": "sensor-test",
    "temperature": 42.5,
    "humidity": 95,
    "location": "Test Area",
    "timestamp": "2024-01-01T12:00:00Z"
  }'
```

## Event Streaming Concepts

### Topics

- **sensor-readings**: Raw sensor events from all sensors
- **sensor-alerts**: Anomaly alerts detected by the consumer

### Consumer Groups

- **python-consumer-group**: Processes events for aggregation and alerting
- **dashboard-group**: Consumes alerts for display
- **dashboard-readings-group**: Consumes readings for real-time display

### Event Processing

1. **Producer** generates events → publishes to `sensor-readings`
2. **Python Consumer** reads events → computes aggregates → publishes to `sensor-alerts`
3. **Dashboard** reads from both topics → displays in real-time

### Kafka Benefits

- **Multiple consumers**: Both Python and Node.js can consume the same events
- **Replay**: New consumers can read historical events (within retention period)
- **Scalability**: Add more consumer instances to scale processing
- **Durability**: Events are persisted, not lost if consumer is down
- **Ordering**: Events are ordered within partitions

## Aspire Integration

### Kafka Configuration

```csharp
var kafka = builder.AddKafka("messaging")
    .WithKafkaUI();
```

This provisions:
- Kafka broker (via container)
- KafkaUI web interface for management
- Connection string passed to all referenced services

### Service References

```csharp
// .NET Producer
builder.AddKafkaProducer<string, string>("messaging");

// Python Consumer
var consumer = builder.AddPythonApp("consumer", "../python-consumer", "main.py")
    .WithReference(kafka);

// Node.js Dashboard
var dashboard = builder.AddNpmApp("dashboard", "../node-dashboard", "start")
    .WithReference(kafka);
```

Connection strings are automatically injected via `ConnectionStrings__messaging`.

## Monitoring

### KafkaUI

Access the KafkaUI dashboard to:
- View topics and partitions
- Inspect messages
- Monitor consumer lag
- View broker health

### Aspire Dashboard

Monitor:
- Service health and logs
- Distributed tracing
- Metrics and resource usage
- OpenTelemetry data

## Extending the Sample

### Add More Consumers

Create additional consumers in any language to process events differently:
- Real-time ML inference
- Data warehouse export
- Notification service
- Archival service

### Add More Topics

Create topic hierarchies:
- `sensor-readings-raw` → initial events
- `sensor-readings-validated` → after validation
- `sensor-readings-enriched` → with additional context

### Add Stream Processing

Integrate with Kafka Streams or Apache Flink for complex event processing:
- Windowed aggregations
- Joins across streams
- Stateful processing

## Related Samples

- **polyglot-task-queue**: Task distribution with RabbitMQ (different paradigm)
- **polyglot-streaming**: Similar event streaming concepts with different tech stack

## Learn More

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [.NET Aspire Kafka Integration](https://learn.microsoft.com/en-us/dotnet/aspire/messaging/kafka-integration)
- [Event Streaming vs Message Queuing](https://www.confluent.io/blog/kafka-vs-rabbitmq/)
