import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// The API port is assigned dynamically by the Aspire AppHost. Because the `web`
// resource declares `.WithReference(api)`, Aspire injects the API base URL into
// this process as a service-discovery env var (services__api__https__0 /
// services__api__http__0). vite.config.ts runs at dev-server start, so it can
// read those and proxy /api to the right place.
function resolveApiTarget(): string {
  const preferred =
    process.env.services__api__https__0 || process.env.services__api__http__0;
  if (preferred) return preferred;

  const match = Object.keys(process.env)
    .filter((k) => /^services__api__https?__\d+$/.test(k))
    .sort()
    .map((k) => process.env[k])
    .find(Boolean);
  if (match) return match;

  // Standalone `vite` (no Aspire): default local API port.
  return 'http://localhost:5265';
}

const target = resolveApiTarget();
console.log(`[vite.config] /api -> ${target}`);

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': { target, changeOrigin: true, secure: false },
    },
  },
});
