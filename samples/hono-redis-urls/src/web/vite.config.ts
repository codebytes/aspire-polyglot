import { defineConfig } from 'vite';

// Aspire injects the API's endpoint as a service-discovery env var when the
// web resource references it (see apphost.ts `.withReference(api.getEndpoint(...))`).
const apiTarget =
  process.env.services__api__https__0 ||
  process.env.services__api__http__0 ||
  'http://localhost:8080';

console.log(`[vite.config] proxy /api and /r -> ${apiTarget}`);

export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: parseInt(process.env.PORT || '5173'),
    proxy: {
      '/api': {
        target: apiTarget,
        changeOrigin: true,
      },
      // Short-link redirects live on the API; forward them so clicking a
      // short link on the web origin reaches the API's /r/:code handler.
      '/r': {
        target: apiTarget,
        changeOrigin: true,
      },
    },
  },
});
