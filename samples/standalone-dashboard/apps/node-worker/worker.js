// Node.js OpenTelemetry emitter — traces, metrics, and logs to the standalone
// Aspire dashboard. No AppHost, no framework: just the OTEL SDK pointed at the
// OTLP endpoint Aspire exposes. Config comes from the standard env vars:
//   OTEL_EXPORTER_OTLP_ENDPOINT, OTEL_EXPORTER_OTLP_PROTOCOL, OTEL_SERVICE_NAME
import { NodeSDK } from '@opentelemetry/sdk-node';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-grpc';
import { OTLPMetricExporter } from '@opentelemetry/exporter-metrics-otlp-grpc';
import { OTLPLogExporter } from '@opentelemetry/exporter-logs-otlp-grpc';
import { PeriodicExportingMetricReader } from '@opentelemetry/sdk-metrics';
import { BatchLogRecordProcessor } from '@opentelemetry/sdk-logs';
import { trace, metrics, SpanStatusCode } from '@opentelemetry/api';
import { logs, SeverityNumber } from '@opentelemetry/api-logs';

const sdk = new NodeSDK({
  traceExporter: new OTLPTraceExporter(),
  metricReader: new PeriodicExportingMetricReader({
    exporter: new OTLPMetricExporter(),
    exportIntervalMillis: 5000,
  }),
  logRecordProcessor: new BatchLogRecordProcessor({
    exporter: new OTLPLogExporter(),
  }),
});
sdk.start();

const tracer = trace.getTracer('node-worker');
const meter = metrics.getMeter('node-worker');
const logger = logs.getLogger('node-worker');

const jobsProcessed = meter.createCounter('jobs.processed', {
  description: 'Number of jobs processed',
});
const jobDuration = meter.createHistogram('job.duration', {
  description: 'Job processing time',
  unit: 'ms',
});

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
const REGIONS = ['us-east', 'us-west', 'eu-central', 'ap-south'];

// One unit of work = a parent span with two child spans, a metric, and a log.
async function processJob(id) {
  const region = REGIONS[Math.floor(Math.random() * REGIONS.length)];
  const start = Date.now();

  await tracer.startActiveSpan('process-job', async (span) => {
    span.setAttribute('job.id', id);
    span.setAttribute('job.region', region);

    await tracer.startActiveSpan('fetch-records', async (child) => {
      await sleep(30 + Math.random() * 120);
      child.setAttribute('records.count', Math.floor(Math.random() * 500));
      child.end();
    });

    await tracer.startActiveSpan('transform', async (child) => {
      await sleep(20 + Math.random() * 80);
      // ~1 in 8 jobs fails, so the dashboard shows error traces too.
      if (Math.random() < 0.12) {
        const err = new Error('transform failed: schema mismatch');
        child.recordException(err);
        child.setStatus({ code: SpanStatusCode.ERROR, message: err.message });
        span.setStatus({ code: SpanStatusCode.ERROR });
        child.end();
        logger.emit({
          severityNumber: SeverityNumber.ERROR,
          severityText: 'ERROR',
          body: `Job ${id} failed in ${region}`,
          attributes: { 'job.id': id, 'job.region': region },
        });
      } else {
        child.end();
      }
      span.end();
    });
  });

  const durationMs = Date.now() - start;
  jobsProcessed.add(1, { region });
  jobDuration.record(durationMs, { region });
  logger.emit({
    severityNumber: SeverityNumber.INFO,
    severityText: 'INFO',
    body: `Processed job ${id} in ${region} (${durationMs}ms)`,
    attributes: { 'job.id': id, 'job.region': region, 'duration.ms': durationMs },
  });
  console.log(`[node-worker] job ${id} in ${region} took ${durationMs}ms`);
}

console.log('[node-worker] emitting telemetry to', process.env.OTEL_EXPORTER_OTLP_ENDPOINT);

let counter = 0;
const timer = setInterval(() => processJob(++counter), 2000);

async function shutdown() {
  clearInterval(timer);
  await sdk.shutdown().catch(() => {});
  process.exit(0);
}
process.on('SIGTERM', shutdown);
process.on('SIGINT', shutdown);
