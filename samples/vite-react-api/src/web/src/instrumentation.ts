/**
 * Browser-side OpenTelemetry instrumentation for the Vite + React SPA.
 *
 * Wiring:
 *   - The Aspire AppHost calls withOtlpExporter() + withOtlpExporterProtocol(HttpProtobuf)
 *     on the web resource so the dashboard's OTLP/HTTP listener URL is injected
 *     as OTEL_EXPORTER_OTLP_ENDPOINT inside the container.
 *   - vite.config.ts reads those env vars (rewriting host.docker.internal -> localhost
 *     so the browser can reach them) and re-exports them as VITE_OTEL_* so they
 *     are visible to client code via import.meta.env.
 *   - This module gates on VITE_OTEL_EXPORTER_OTLP_ENDPOINT — without it the SPA
 *     runs unchanged with no export attempts.
 *
 * Three signals reach the dashboard once Aspire is in front:
 *   - Traces: document load + every fetch / XMLHttpRequest gets a client span,
 *     and the W3C `traceparent` header is propagated so server-side spans stitch.
 *   - Metrics: BatchSpanProcessor exports continuously, surfacing in the Traces
 *     tab. (Browser metrics are out of scope for the Aspire dashboard today.)
 *   - Logs: console.* is bridged into the active span as events; serious errors
 *     are also recorded as exceptions on the span.
 */
import { trace, context, type Span } from '@opentelemetry/api';
import { resourceFromAttributes } from '@opentelemetry/resources';
import {
  ATTR_SERVICE_NAME,
  ATTR_SERVICE_VERSION,
} from '@opentelemetry/semantic-conventions';
import {
  WebTracerProvider,
  BatchSpanProcessor,
} from '@opentelemetry/sdk-trace-web';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { ZoneContextManager } from '@opentelemetry/context-zone';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';
import { XMLHttpRequestInstrumentation } from '@opentelemetry/instrumentation-xml-http-request';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { UserInteractionInstrumentation } from '@opentelemetry/instrumentation-user-interaction';

const endpoint = import.meta.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT;

if (endpoint) {
  const serviceName = import.meta.env.VITE_OTEL_SERVICE_NAME ?? 'web';
  const headers = parseOtlpHeaders(import.meta.env.VITE_OTEL_EXPORTER_OTLP_HEADERS);
  const resourceAttrs = parseResourceAttrs(import.meta.env.VITE_OTEL_RESOURCE_ATTRIBUTES);

  const provider = new WebTracerProvider({
    resource: resourceFromAttributes({
      [ATTR_SERVICE_NAME]: serviceName,
      [ATTR_SERVICE_VERSION]: '1.0.0',
      ...resourceAttrs,
    }),
    spanProcessors: [
      new BatchSpanProcessor(
        new OTLPTraceExporter({
          // Aspire's OTLP/HTTP listener accepts protobuf POSTs at /v1/traces.
          url: joinUrl(endpoint, 'v1/traces'),
          headers,
        }),
        {
          maxQueueSize: 100,
          maxExportBatchSize: 32,
          scheduledDelayMillis: 1000,
          exportTimeoutMillis: 5000,
        }
      ),
    ],
  });

  provider.register({
    // ZoneContextManager keeps span context alive across async boundaries
    // (fetch promises, setTimeout callbacks, etc.) so child spans can find
    // their parent.
    contextManager: new ZoneContextManager(),
  });

  registerInstrumentations({
    instrumentations: [
      new DocumentLoadInstrumentation(),
      new UserInteractionInstrumentation({
        eventNames: ['click', 'submit'],
      }),
      new FetchInstrumentation({
        // Propagate W3C traceparent on every same-origin fetch so the API's
        // server span becomes a child of the browser's client span.
        propagateTraceHeaderCorsUrls: [/.*/],
        clearTimingResources: true,
      }),
      new XMLHttpRequestInstrumentation({
        propagateTraceHeaderCorsUrls: [/.*/],
      }),
    ],
  });

  bridgeConsoleToActiveSpan();

  console.info(`[otel] browser tracing enabled — service.name=${serviceName} -> ${endpoint}`);
}

function parseOtlpHeaders(raw: string | undefined): Record<string, string> {
  if (!raw) return {};
  return raw
    .split(',')
    .map((kv) => kv.trim())
    .filter(Boolean)
    .reduce<Record<string, string>>((acc, kv) => {
      const eq = kv.indexOf('=');
      if (eq > 0) {
        acc[kv.slice(0, eq).trim()] = kv.slice(eq + 1).trim();
      }
      return acc;
    }, {});
}

function parseResourceAttrs(raw: string | undefined): Record<string, string> {
  return parseOtlpHeaders(raw);
}

function joinUrl(base: string, path: string): string {
  const trimmed = base.endsWith('/') ? base.slice(0, -1) : base;
  return `${trimmed}/${path}`;
}

function bridgeConsoleToActiveSpan(): void {
  const tracer = trace.getTracer('browser-console');
  const orig = {
    log: console.log.bind(console),
    info: console.info.bind(console),
    warn: console.warn.bind(console),
    error: console.error.bind(console),
  };
  type Level = 'log' | 'info' | 'warn' | 'error';
  const wrap = (level: Level) => (...args: unknown[]) => {
    orig[level](...args);
    const active = trace.getSpan(context.active());
    if (active) {
      addEventToSpan(active, level, args);
    } else if (level === 'error') {
      // Errors outside a span still want to be visible — open a tiny span.
      const span = tracer.startSpan(`console.${level}`);
      addEventToSpan(span, level, args);
      span.end();
    }
  };
  console.log = wrap('log');
  console.info = wrap('info');
  console.warn = wrap('warn');
  console.error = wrap('error');
}

function addEventToSpan(span: Span, level: string, args: unknown[]): void {
  const message = args
    .map((a) => (typeof a === 'string' ? a : safeStringify(a)))
    .join(' ');
  span.addEvent('log', { 'log.severity': level, 'log.message': message });
  if (level === 'error') {
    const err = args.find((a) => a instanceof Error) as Error | undefined;
    if (err) {
      span.recordException(err);
    }
  }
}

function safeStringify(value: unknown): string {
  try {
    return JSON.stringify(value);
  } catch {
    return String(value);
  }
}
