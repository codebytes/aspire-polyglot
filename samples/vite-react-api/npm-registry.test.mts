import assert from 'node:assert/strict';
import { mkdtempSync, rmSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import test from 'node:test';
import { getNpmRegistry } from './npm-registry.mjs';

test('an explicit registry overrides npm-run environment and configuration', () => {
  assert.equal(getNpmRegistry({
    NPM_CONFIG_REGISTRY: 'https://explicit.example.test/',
    npm_config_registry: 'https://npm-run.example.test/',
  }, '/unused'), 'https://explicit.example.test/');
});

test('the npm-run registry environment is honored without an uppercase override', () => {
  assert.equal(getNpmRegistry({
    npm_config_registry: 'https://npm-run.example.test/',
  }, '/unused'), 'https://npm-run.example.test/');
});

test('without environment overrides, the AppHost project npm configuration is used', (t) => {
  const directory = mkdtempSync(join(tmpdir(), 'aspire-npm-registry-'));
  t.after(() => rmSync(directory, { recursive: true }));
  writeFileSync(join(directory, 'package.json'), '{"name":"registry-fixture","private":true}');
  writeFileSync(join(directory, '.npmrc'), 'registry=https://project.example.test/\n');

  assert.equal(getNpmRegistry({
    ...process.env,
    NPM_CONFIG_REGISTRY: undefined,
    npm_config_registry: undefined,
  }, directory), 'https://project.example.test/');
});
