/**
 * Browser-side OpenTelemetry instrumentation for the ts-starter Vite + React SPA.
 *
 * Wiring:
 *   - The Aspire AppHost calls withOtlpExporter() + withOtlpExporterProtocol(HttpProtobuf)
 *     on the frontend resource so the dashboard's OTLP/HTTP listener URL is
 *     injected as OTEL_EXPORTER_OTLP_ENDPOINT for the Vite dev server process.
 *   - vite.config.ts re-exports those env vars as VITE_OTEL_* so they are
 *     visible to client code via import.meta.env. (No host rewrite needed —
 *     addViteApp runs on the host, not in a container.)
 *   - This module gates on VITE_OTEL_EXPORTER_OTLP_ENDPOINT — without it the
 *     SPA runs unchanged with no export attempts.
 *
 * Coverage:
 *   - Traces: DocumentLoad + every fetch / XHR / qualifying click submit gets a
 *     client span, and W3C `traceparent` headers are propagated so the Express
 *     API's server spans stitch into the same trace.
 *   - Logs: console.* is bridged into the active span as events; thrown
 *     Errors are recorded as exceptions on the span.
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
  const serviceName = import.meta.env.VITE_OTEL_SERVICE_NAME ?? 'frontend';
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
    contextManager: new ZoneContextManager(),
  });

  registerInstrumentations({
    instrumentations: [
      new DocumentLoadInstrumentation(),
      new UserInteractionInstrumentation({
        eventNames: ['click', 'submit'],
      }),
      new FetchInstrumentation({
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
