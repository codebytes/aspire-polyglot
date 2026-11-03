import { defineConfig } from 'vite';

// Referencing the api resource injects its URL as API_HTTP.
const api = process.env.API_HTTPS || process.env.API_HTTP || 'http://localhost:8080';

export default defineConfig({
  server: {
    proxy: {
      '/api': { target: api, changeOrigin: true },
      // Short links are served by the API's /r/:code redirect.
      '/r': { target: api, changeOrigin: true },
    },
  },
});
