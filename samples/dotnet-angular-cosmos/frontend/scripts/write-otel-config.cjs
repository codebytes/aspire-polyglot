const fs = require('node:fs');
const path = require('node:path');

const explicitEndpoint = process.env.OTEL_EXPORTER_OTLP_ENDPOINT_HTTP;
const endpoint = explicitEndpoint ||
  (process.env.OTEL_EXPORTER_OTLP_PROTOCOL === 'http/protobuf'
    ? process.env.OTEL_EXPORTER_OTLP_ENDPOINT
    : undefined);

if (endpoint) new URL(endpoint);

const headers = {};
for (const entry of (process.env.OTEL_EXPORTER_OTLP_HEADERS || '').split(',')) {
  if (!entry.trim()) continue;
  const separator = entry.indexOf('=');
  if (separator <= 0) throw new Error('Invalid OTEL_EXPORTER_OTLP_HEADERS entry');
  headers[entry.slice(0, separator).trim()] = entry.slice(separator + 1).trim();
}

const config = {
  otlpEndpoint: endpoint,
  headers,
  serviceName: process.env.OTEL_SERVICE_NAME || 'frontend',
  serviceVersion: process.env.OTEL_SERVICE_VERSION || '1.0.0',
};
const directory = path.resolve(__dirname, '..', '.generated');
fs.mkdirSync(directory, { recursive: true });
fs.writeFileSync(
  path.join(directory, 'otel-config.js'),
  `window.__OTEL_CONFIG__ = ${JSON.stringify(config)};\n`,
  'utf8',
);
console.log(`[otel-config] ${endpoint ? `HTTP collector: ${endpoint}` : 'No browser collector configured'}`);
