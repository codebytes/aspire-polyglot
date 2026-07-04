// Angular dev-server proxy for the API.
//
// The API port is assigned dynamically by the Aspire AppHost, so we cannot
// hard-code it. Aspire injects the API's base URL into this process as a
// service-discovery env var (services__api__https__0 / services__api__http__0)
// because the frontend resource declares `.WithReference(api)`.
//
// This file is evaluated by `ng serve` at start time, so it can read those
// env vars directly. A plain proxy.conf.json cannot, which is why the target
// used to be hard-coded to http://localhost:5000 -- a port that on macOS is
// often already taken by the AirPlay Receiver (Control Center), producing
// 403 responses for every /api call.

function resolveApiTarget() {
  // Prefer explicit Aspire-injected endpoints (https first, then http).
  const preferred =
    process.env.services__api__https__0 || process.env.services__api__http__0;
  if (preferred) {
    return preferred;
  }

  // Fall back to any services__api__<scheme>__<index> the AppHost may have set.
  const match = Object.keys(process.env)
    .filter((k) => /^services__api__https?__\d+$/.test(k))
    .sort() // https sorts before http; index 0 before 1
    .map((k) => process.env[k])
    .find(Boolean);
  if (match) {
    return match;
  }

  // Standalone `ng serve` (no Aspire): default local API port.
  return 'http://localhost:5000';
}

const target = resolveApiTarget();
console.log(`[proxy.conf] /api -> ${target}`);

module.exports = {
  '/api': {
    target,
    secure: false,
    changeOrigin: true,
  },
};
