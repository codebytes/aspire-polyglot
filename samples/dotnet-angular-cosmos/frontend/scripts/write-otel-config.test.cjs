const assert = require('node:assert/strict');
const { test } = require('node:test');
const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

function generate(overrides, args = []) {
  const generated = path.join(__dirname, '..', '.generated');
  fs.mkdirSync(generated, { recursive: true });
  const root = fs.mkdtempSync(path.join(generated, 'otel-test-'));
  try {
    fs.mkdirSync(path.join(root, 'scripts'));
    const script = path.join(root, 'scripts', 'write-otel-config.cjs');
    fs.copyFileSync(path.join(__dirname, 'write-otel-config.cjs'), script);
    const env = { ...process.env };
    for (const key of Object.keys(env)) {
      if (key.startsWith('OTEL_')) delete env[key];
    }
    const result = spawnSync(process.execPath, [script, ...args], {
      env: { ...env, ...overrides },
      encoding: 'utf8',
    });
    if (result.status !== 0) return { result };
    const content = fs.readFileSync(path.join(root, '.generated', 'otel-config.js'), 'utf8');
    return { result, config: JSON.parse(content.slice('window.__OTEL_CONFIG__ = '.length, -2)) };
  } finally {
    fs.rmSync(root, { recursive: true, force: true });
  }
}

test('does not treat an absent or gRPC collector as an HTTP collector', () => {
  assert.equal(generate({}).config.otlpEndpoint, undefined);
  assert.equal(generate({
    OTEL_EXPORTER_OTLP_ENDPOINT: 'https://localhost:4317',
    OTEL_EXPORTER_OTLP_PROTOCOL: 'grpc',
  }).config.otlpEndpoint, undefined);
});

test('uses the HTTP collector and forwards authentication without logging it', () => {
  const { config, result } = generate({
    OTEL_EXPORTER_OTLP_ENDPOINT: 'https://localhost:4318',
    OTEL_EXPORTER_OTLP_PROTOCOL: 'http/protobuf',
    OTEL_EXPORTER_OTLP_HEADERS: 'x-otlp-api-key=test-value, custom=part=two',
  });
  assert.equal(config.otlpEndpoint, 'https://localhost:4318');
  assert.deepEqual(config.headers, { 'x-otlp-api-key': 'test-value', custom: 'part=two' });
  assert.ok(!result.stdout.includes('test-value'));
});

test('honors an explicit HTTP collector over a gRPC endpoint', () => {
  const { config } = generate({
    OTEL_EXPORTER_OTLP_ENDPOINT_HTTP: 'http://localhost:9999/otel',
    OTEL_EXPORTER_OTLP_ENDPOINT: 'https://localhost:4317',
    OTEL_EXPORTER_OTLP_PROTOCOL: 'grpc',
  });
  assert.equal(config.otlpEndpoint, 'http://localhost:9999/otel');
});

test('reports invalid collector and header configuration', () => {
  assert.notEqual(generate({ OTEL_EXPORTER_OTLP_ENDPOINT_HTTP: 'not a URL' }).result.status, 0);
  assert.notEqual(generate({ OTEL_EXPORTER_OTLP_HEADERS: 'invalid' }).result.status, 0);
});

test('production builds never embed local collector endpoints or credentials', () => {
  const { config, result } = generate({
    OTEL_EXPORTER_OTLP_ENDPOINT_HTTP: 'https://localhost:4318',
    OTEL_EXPORTER_OTLP_PROTOCOL: 'http/protobuf',
    OTEL_EXPORTER_OTLP_ENDPOINT: 'https://localhost:4318',
    OTEL_EXPORTER_OTLP_HEADERS: 'x-otlp-api-key=local-only-test-value',
  }, ['--production']);
  assert.equal(config.otlpEndpoint, undefined);
  assert.deepEqual(config.headers, {});
  assert.ok(!result.stdout.includes('local-only-test-value'));
  assert.ok(!result.stdout.includes('localhost'));
});

test('production builds ignore malformed local collector configuration', () => {
  const { config, result } = generate({
    OTEL_EXPORTER_OTLP_ENDPOINT_HTTP: 'not a URL',
    OTEL_EXPORTER_OTLP_HEADERS: 'invalid',
  }, ['--production']);
  assert.equal(result.status, 0);
  assert.equal(config.otlpEndpoint, undefined);
  assert.deepEqual(config.headers, {});
});
