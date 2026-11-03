import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Referencing the "app" resource injects its URL as APP_HTTPS / APP_HTTP.
const api = process.env.APP_HTTPS || process.env.APP_HTTP;

// Hand the OTLP/HTTP settings Aspire injects to browser code. Vite only exposes
// VITE_-prefixed variables through import.meta.env.
for (const name of ['ENDPOINT', 'HEADERS']) {
  process.env[`VITE_OTEL_EXPORTER_OTLP_${name}`] ??= process.env[`OTEL_EXPORTER_OTLP_${name}`] ?? '';
}
process.env.VITE_OTEL_SERVICE_NAME ??= process.env.OTEL_SERVICE_NAME ?? 'frontend';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': { target: api, changeOrigin: true },
    },
  },
});
