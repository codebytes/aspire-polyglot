import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';

const apiUrl = process.env.services__api__http__0 || 'http://localhost:8080';

function rewriteHostDockerInternal(value) {
  if (!value) return value;
  const endpoint = new URL(value);
  if (endpoint.hostname === 'aspire.dev.internal' || endpoint.hostname === 'host.docker.internal') {
    endpoint.hostname = 'localhost';
    return endpoint.toString();
  }
  return value;
}

const otlpEndpoint = rewriteHostDockerInternal(process.env.OTEL_EXPORTER_OTLP_ENDPOINT);
const otlpHeaders = process.env.OTEL_EXPORTER_OTLP_HEADERS;
const otelServiceName = process.env.OTEL_SERVICE_NAME || 'svelte-bookmarks-frontend';
const otelResourceAttrs = process.env.OTEL_RESOURCE_ATTRIBUTES;

export default defineConfig({
  plugins: [svelte()],
  server: {
    host: true,
    port: parseInt(process.env.PORT || '5173'),
    strictPort: true,
    proxy: {
      '/api': {
        target: apiUrl,
        changeOrigin: true,
      },
    },
  },
  define: {
    'import.meta.env.VITE_OTEL_EXPORTER_OTLP_ENDPOINT': JSON.stringify(otlpEndpoint),
    'import.meta.env.VITE_OTEL_EXPORTER_OTLP_HEADERS': JSON.stringify(otlpHeaders),
    'import.meta.env.VITE_OTEL_SERVICE_NAME': JSON.stringify(otelServiceName),
    'import.meta.env.VITE_OTEL_RESOURCE_ATTRIBUTES': JSON.stringify(otelResourceAttrs),
  },
});
