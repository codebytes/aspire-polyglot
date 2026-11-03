import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Referencing the api resource injects its URL as API_HTTPS / API_HTTP.
const api = process.env.API_HTTPS || process.env.API_HTTP || 'http://localhost:8000'

// Hand the OTLP/HTTP settings Aspire injects to browser code. Vite only exposes
// VITE_-prefixed variables through import.meta.env.
for (const name of ['ENDPOINT', 'HEADERS']) {
  process.env[`VITE_OTEL_EXPORTER_OTLP_${name}`] ??= process.env[`OTEL_EXPORTER_OTLP_${name}`] ?? ''
}
process.env.VITE_OTEL_SERVICE_NAME ??= process.env.OTEL_SERVICE_NAME ?? 'web'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': { target: api, changeOrigin: true },
    },
  },
})
