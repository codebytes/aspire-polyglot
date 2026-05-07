import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Re-export Aspire-injected OTEL_* env vars as VITE_OTEL_* so the browser
// SPA can read them via import.meta.env. addViteApp runs on the host (no
// container) so the localhost OTLP/HTTP URL the dashboard injects is
// reachable from the browser as-is.
const otelEndpoint = process.env.OTEL_EXPORTER_OTLP_ENDPOINT;
if (otelEndpoint) {
  process.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT = otelEndpoint;
  process.env.VITE_OTEL_EXPORTER_OTLP_HEADERS = process.env.OTEL_EXPORTER_OTLP_HEADERS ?? '';
  process.env.VITE_OTEL_SERVICE_NAME = process.env.OTEL_SERVICE_NAME ?? 'frontend';
  process.env.VITE_OTEL_RESOURCE_ATTRIBUTES = process.env.OTEL_RESOURCE_ATTRIBUTES ?? '';
}

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Proxy API calls to the Express service
      '/api': {
        target: process.env.APP_HTTPS || process.env.APP_HTTP,
        changeOrigin: true
      }
    }
  }
});
