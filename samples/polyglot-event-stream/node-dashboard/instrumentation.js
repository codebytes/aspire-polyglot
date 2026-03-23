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

  // Bridge console output to OTel structured logs so they appear in the
  // Aspire dashboard's Structured Logs view.
  const { logs, SeverityNumber } = require('@opentelemetry/api-logs');
  const otelLogger = logs.getLogger('node-dashboard');

  const originalLog = console.log;
  const originalWarn = console.warn;
  const originalError = console.error;

  function formatArgs(args) {
    return args.map(a => (typeof a === 'string' ? a : JSON.stringify(a))).join(' ');
  }

  console.log = function (...args) {
    originalLog.apply(console, args);
    otelLogger.emit({
      severityNumber: SeverityNumber.INFO,
      severityText: 'INFO',
      body: formatArgs(args),
    });
  };

  console.warn = function (...args) {
    originalWarn.apply(console, args);
    otelLogger.emit({
      severityNumber: SeverityNumber.WARN,
      severityText: 'WARN',
      body: formatArgs(args),
    });
  };

  console.error = function (...args) {
    originalError.apply(console, args);
    otelLogger.emit({
      severityNumber: SeverityNumber.ERROR,
      severityText: 'ERROR',
      body: formatArgs(args),
    });
  };

  process.on('SIGTERM', () => {
    sdk.shutdown().finally(() => process.exit(0));
  });
  process.on('SIGINT', () => {
    sdk.shutdown().finally(() => process.exit(0));
  });
}
