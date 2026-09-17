import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const apiUrl = process.env.services__api__https__0 || process.env.services__api__http__0 || 'http://localhost:8000';

// Map Aspire's container-only collector hostnames to the browser's loopback host.
function toBrowserEndpoint(value: string | undefined): string | undefined {
  if (!value) return value;
  const endpoint = new URL(value);
  if (endpoint.hostname === 'aspire.dev.internal' || endpoint.hostname === 'host.docker.internal') {
    endpoint.hostname = 'localhost';
    return endpoint.toString();
  }
  return value;
}

const otlpEndpoint = toBrowserEndpoint(process.env.OTEL_EXPORTER_OTLP_ENDPOINT);
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
