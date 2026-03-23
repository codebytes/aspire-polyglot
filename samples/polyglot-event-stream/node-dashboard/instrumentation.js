/**
 * OpenTelemetry instrumentation for the Node.js dashboard.
 *
 * When OTEL_EXPORTER_OTLP_ENDPOINT is set (automatically configured by Aspire),
 * this initializes the OTel Node.js SDK to export traces, metrics, and logs
 * to the Aspire dashboard.
 *
 * This file must be required before any other modules.
 */
const { env } = require('node:process');

if (env.OTEL_EXPORTER_OTLP_ENDPOINT) {
  const { NodeSDK } = require('@opentelemetry/sdk-node');
  const { getNodeAutoInstrumentations } = require('@opentelemetry/auto-instrumentations-node');
  const { OTLPTraceExporter } = require('@opentelemetry/exporter-trace-otlp-grpc');
  const { OTLPMetricExporter } = require('@opentelemetry/exporter-metrics-otlp-grpc');
  const { OTLPLogExporter } = require('@opentelemetry/exporter-logs-otlp-grpc');
  const { BatchLogRecordProcessor } = require('@opentelemetry/sdk-logs');
  const { PeriodicExportingMetricReader } = require('@opentelemetry/sdk-metrics');

  const sdk = new NodeSDK({
    traceExporter: new OTLPTraceExporter(),
    metricReader: new PeriodicExportingMetricReader({
      exporter: new OTLPMetricExporter(),
    }),
    logRecordProcessor: new BatchLogRecordProcessor(
      new OTLPLogExporter(),
    ),
    instrumentations: [getNodeAutoInstrumentations()],
  });

  sdk.start();

  process.on('SIGTERM', () => {
    sdk.shutdown().finally(() => process.exit(0));
  });
  process.on('SIGINT', () => {
    sdk.shutdown().finally(() => process.exit(0));
  });
}
