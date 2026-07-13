// Browser OpenTelemetry SDK bootstrap.
//
// This file MUST be the first import in main.js so the global tracer/propagator
// are installed before any fetch() / XHR is made by Svelte components.
//
// The OTLP/HTTP endpoint is read from VITE_OTEL_EXPORTER_OTLP_ENDPOINT, which
// vite.config.js forwards from the Aspire-injected OTEL_EXPORTER_OTLP_ENDPOINT
// (rewriting any gRPC scheme to plain http for /v1/traces). When unset, the
// SDK is skipped entirely so the SPA still runs standalone.
//
// Trace correlation with the Go backend works because @opentelemetry/instrumentation-fetch
// adds a `traceparent` header to every same-origin fetch, which otelhttp.NewHandler
// on the server side reads via propagation.TraceContext to make the server span
// a child of the browser span.

import { WebTracerProvider, BatchSpanProcessor } from '@opentelemetry/sdk-trace-web';
import { resourceFromAttributes } from '@opentelemetry/resources';
import { ATTR_SERVICE_NAME } from '@opentelemetry/semantic-conventions';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { ZoneContextManager } from '@opentelemetry/context-zone';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';
import { XMLHttpRequestInstrumentation } from '@opentelemetry/instrumentation-xml-http-request';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { W3CTraceContextPropagator } from '@opentelemetry/core';

const rawEndpoint = import.meta.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT;
const serviceName = import.meta.env.VITE_OTEL_SERVICE_NAME || 'svelte-bookmarks-frontend';

function normalizeEndpoint(endpoint) {
  if (!endpoint) return '';
  // Aspire injects the gRPC OTLP URL (e.g. https://localhost:21146). Browsers
  // can't speak gRPC, so we strip the scheme/host/port if a Vite proxy is used,
  // or rewrite to the OTLP/HTTP traces path. Production deployments should set
  // VITE_OTEL_EXPORTER_OTLP_ENDPOINT directly to the http endpoint.
  try {
    const u = new URL(endpoint);
    return `${u.origin}/v1/traces`;
  } catch {
    return endpoint;
  }
}

const tracesEndpoint = normalizeEndpoint(rawEndpoint);

if (tracesEndpoint) {
  const headerPairs = (import.meta.env.VITE_OTEL_EXPORTER_OTLP_HEADERS || '')
    .split(',')
    .map((kv) => kv.trim())
    .filter(Boolean)
    .reduce((acc, kv) => {
      const eq = kv.indexOf('=');
      if (eq > 0) acc[kv.slice(0, eq)] = kv.slice(eq + 1);
      return acc;
    }, {});

  const provider = new WebTracerProvider({
    resource: resourceFromAttributes({
      [ATTR_SERVICE_NAME]: serviceName,
      'service.namespace': 'svelte-go-bookmarks',
    }),
    spanProcessors: [
      new BatchSpanProcessor(
        new OTLPTraceExporter({ url: tracesEndpoint, headers: headerPairs }),
      ),
    ],
  });

  provider.register({
    contextManager: new ZoneContextManager(),
    propagator: new W3CTraceContextPropagator(),
  });

  registerInstrumentations({
    instrumentations: [
      new DocumentLoadInstrumentation(),
      new FetchInstrumentation({
        // Tell the fetch instrumentation that calls to the same-origin /api
        // path should propagate traceparent — required for browser↔Go stitching.
        propagateTraceHeaderCorsUrls: [/.*/],
        clearTimingResources: true,
      }),
      new XMLHttpRequestInstrumentation({ propagateTraceHeaderCorsUrls: [/.*/] }),
    ],
  });

  // eslint-disable-next-line no-console
  console.info('[otel] browser tracing initialized', { tracesEndpoint, serviceName });
} else {
  // eslint-disable-next-line no-console
  console.info('[otel] VITE_OTEL_EXPORTER_OTLP_ENDPOINT unset — browser tracing disabled');
}
