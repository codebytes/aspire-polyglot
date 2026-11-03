import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';

// Referencing the api resource injects its URL as API_HTTP. Aspire also
// supplies the dev server's port, so only the /api proxy is configured here.
const api = process.env.API_HTTPS || process.env.API_HTTP || 'http://localhost:8080';

export default defineConfig({
  plugins: [svelte()],
  server: {
    proxy: {
      '/api': { target: api, changeOrigin: true },
    },
  },
});
