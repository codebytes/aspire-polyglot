/**
 * Browser tracing. vite.config.ts forwards Aspire's OTLP/HTTP endpoint and
 * headers as VITE_OTEL_*; without them (plain `npm run dev`) nothing is exported.
 *
 * Every fetch() gets a client span and a W3C `traceparent` header, so the
 * Express server span joins the same trace in the dashboard.
 */
import { resourceFromAttributes } from '@opentelemetry/resources';
import { ATTR_SERVICE_NAME } from '@opentelemetry/semantic-conventions';
import { BatchSpanProcessor, WebTracerProvider } from '@opentelemetry/sdk-trace-web';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { ZoneContextManager } from '@opentelemetry/context-zone';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';

const endpoint = import.meta.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT;

if (endpoint) {
  // OTEL_EXPORTER_OTLP_HEADERS is "key=value,key=value" (the dashboard API key).
  const headers = Object.fromEntries(
    (import.meta.env.VITE_OTEL_EXPORTER_OTLP_HEADERS ?? '')
      .split(',')
      .filter(Boolean)
      .map((pair) => pair.split('=').map((part) => part.trim())),
  );

  const provider = new WebTracerProvider({
    resource: resourceFromAttributes({
      [ATTR_SERVICE_NAME]: import.meta.env.VITE_OTEL_SERVICE_NAME || 'frontend',
    }),
    spanProcessors: [
      new BatchSpanProcessor(
        new OTLPTraceExporter({ url: `${endpoint.replace(/\/$/, '')}/v1/traces`, headers }),
        { scheduledDelayMillis: 1000 },
      ),
    ],
  });
  provider.register({ contextManager: new ZoneContextManager() });

  registerInstrumentations({
    instrumentations: [
      new DocumentLoadInstrumentation(),
      new FetchInstrumentation({ propagateTraceHeaderCorsUrls: [/.*/] }),
    ],
  });
}
