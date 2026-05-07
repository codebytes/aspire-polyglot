import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const apiUrl = process.env.services__api__https__0 || process.env.services__api__http__0 || 'http://localhost:8000';

// The Vite SPA runs in a Docker container; Aspire injects OTLP env vars
// using `host.docker.internal` so the container can reach the dashboard.
// The browser, however, lives on the user's machine and reaches the
// dashboard via `localhost`. Rewrite the value baked into the SPA so the
// browser actually has a usable URL.
function rewriteHostDockerInternal(value: string | undefined): string | undefined {
  if (!value) return value;
  return value.replace(/host\.docker\.internal/g, 'localhost');
}

const otlpEndpoint = rewriteHostDockerInternal(process.env.OTEL_EXPORTER_OTLP_ENDPOINT);
const otlpHeaders = process.env.OTEL_EXPORTER_OTLP_HEADERS;
const otelServiceName = process.env.OTEL_SERVICE_NAME;
const otelResourceAttrs = process.env.OTEL_RESOURCE_ATTRIBUTES;

export default defineConfig({
  plugins: [react()],
  server: {
    port: parseInt(process.env.PORT || '5173'),
    proxy: {
      '/api': {
        target: apiUrl,
        changeOrigin: true,
      }
    }
  },
  // Re-export server-side OTel env vars to client code through Vite's
  // VITE_-prefixed channel. Without this the browser SDK has no endpoint.
  define: {
    'import.meta.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT': JSON.stringify(otlpEndpoint),
    'import.meta.env.VITE_OTEL_EXPORTER_OTLP_HEADERS': JSON.stringify(otlpHeaders),
    'import.meta.env.VITE_OTEL_SERVICE_NAME': JSON.stringify(otelServiceName),
    'import.meta.env.VITE_OTEL_RESOURCE_ATTRIBUTES': JSON.stringify(otelResourceAttrs),
  },
})
