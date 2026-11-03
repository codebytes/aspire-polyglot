// Loaded first by app.js. The OpenTelemetry SDK reads Aspire's OTEL_* settings
// (endpoint, protocol, headers, service name) and exports traces, metrics, and logs.
const { NodeSDK } = require('@opentelemetry/sdk-node');
const { HttpInstrumentation } = require('@opentelemetry/instrumentation-http');
const { ExpressInstrumentation } = require('@opentelemetry/instrumentation-express');
const { KafkaJsInstrumentation } = require('@opentelemetry/instrumentation-kafkajs');
const { PinoInstrumentation } = require('@opentelemetry/instrumentation-pino');

if (process.env.OTEL_EXPORTER_OTLP_ENDPOINT) {
    const sdk = new NodeSDK({
        instrumentations: [
            new HttpInstrumentation(),
            new ExpressInstrumentation(),
            new KafkaJsInstrumentation(), // a span per consumed Kafka message
            new PinoInstrumentation(),    // pino records -> OTLP logs with trace context
        ],
    });
    sdk.start();
    process.on('SIGTERM', () => sdk.shutdown());
}
