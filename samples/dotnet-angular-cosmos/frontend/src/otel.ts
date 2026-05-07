// Browser OpenTelemetry bootstrap.
// Must be imported BEFORE @angular/core / bootstrapApplication so document_load
// and zone-aware context propagation are wired before Angular's first tick.

import { context, trace, propagation } from '@opentelemetry/api';
import { resourceFromAttributes } from '@opentelemetry/resources';
import {
  ATTR_SERVICE_NAME,
  ATTR_SERVICE_VERSION,
} from '@opentelemetry/semantic-conventions';
import {
  WebTracerProvider,
  BatchSpanProcessor,
} from '@opentelemetry/sdk-trace-web';
import { ZoneContextManager } from '@opentelemetry/context-zone-peer-dep';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { OTLPLogExporter } from '@opentelemetry/exporter-logs-otlp-http';
import { OTLPMetricExporter } from '@opentelemetry/exporter-metrics-otlp-http';
import {
  LoggerProvider,
  BatchLogRecordProcessor,
} from '@opentelemetry/sdk-logs';
import {
  MeterProvider,
  PeriodicExportingMetricReader,
} from '@opentelemetry/sdk-metrics';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';
import { XMLHttpRequestInstrumentation } from '@opentelemetry/instrumentation-xml-http-request';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { UserInteractionInstrumentation } from '@opentelemetry/instrumentation-user-interaction';
import { W3CTraceContextPropagator } from '@opentelemetry/core';

interface RuntimeConfig {
  otlpEndpoint?: string;
  serviceName?: string;
  serviceVersion?: string;
}

// Runtime config is injected into window by index.html (populated by AppHost).
// Falls back to localhost:4318 for standalone runs.
declare global {
  interface Window {
    __OTEL_CONFIG__?: RuntimeConfig;
  }
}

const cfg: RuntimeConfig = window.__OTEL_CONFIG__ ?? {};
const otlpBase =
  cfg.otlpEndpoint?.replace(/\/$/, '') ?? 'http://localhost:4318';
const serviceName = cfg.serviceName ?? 'frontend';
const serviceVersion = cfg.serviceVersion ?? '1.0.0';

const resource = resourceFromAttributes({
  [ATTR_SERVICE_NAME]: serviceName,
  [ATTR_SERVICE_VERSION]: serviceVersion,
  'service.namespace': 'dotnet-angular-cosmos',
  'telemetry.sdk.language': 'webjs',
});

// W3C tracecontext is the default — set it explicitly so cross-origin fetches
// stamp `traceparent` headers that the .NET API can pick up.
propagation.setGlobalPropagator(new W3CTraceContextPropagator());

// ---- Traces ----
const tracerProvider = new WebTracerProvider({
  resource,
  spanProcessors: [
    new BatchSpanProcessor(
      new OTLPTraceExporter({ url: `${otlpBase}/v1/traces` }),
      { scheduledDelayMillis: 1000 }
    ),
  ],
});
tracerProvider.register({
  contextManager: new ZoneContextManager(),
});

// ---- Logs ----
const loggerProvider = new LoggerProvider({
  resource,
  processors: [
    new BatchLogRecordProcessor(
      new OTLPLogExporter({ url: `${otlpBase}/v1/logs` }),
      { scheduledDelayMillis: 1000 }
    ),
  ],
});

// ---- Metrics ----
const meterProvider = new MeterProvider({
  resource,
  readers: [
    new PeriodicExportingMetricReader({
      exporter: new OTLPMetricExporter({ url: `${otlpBase}/v1/metrics` }),
      exportIntervalMillis: 5000,
    }),
  ],
});

// ---- Auto-instrumentations ----
registerInstrumentations({
  tracerProvider,
  meterProvider,
  instrumentations: [
    new DocumentLoadInstrumentation(),
    new UserInteractionInstrumentation({
      eventNames: ['click', 'submit'],
    }),
    new FetchInstrumentation({
      // Match same-origin /api/** plus any explicit cross-origin endpoints.
      propagateTraceHeaderCorsUrls: [/.*/],
      clearTimingResources: true,
    }),
    new XMLHttpRequestInstrumentation({
      propagateTraceHeaderCorsUrls: [/.*/],
    }),
  ],
});

// Sanity log so it's obvious in DevTools that init ran.
// eslint-disable-next-line no-console
console.info(
  `[otel] initialised service.name=${serviceName} otlp=${otlpBase}`
);

export { tracerProvider, loggerProvider, meterProvider };
export const browserTracer = trace.getTracer(serviceName, serviceVersion);
export { context as otelContext };
