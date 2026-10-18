const assert = require('node:assert/strict');
const { readFileSync } = require('node:fs');
const { test } = require('node:test');
const { runInNewContext } = require('node:vm');

function launch(env, result = { status: 0 }) {
  let invocation;
  let exitCode;
  const requireMock = () => ({
    spawnSync: (...args) => {
      invocation = args;
      return result;
    },
  });
  requireMock.resolve = () => 'angular-cli.js';
  runInNewContext(readFileSync(`${__dirname}/start.cjs`, 'utf8'), {
    require: requireMock,
    process: {
      execPath: 'node.exe', argv: ['node.exe', 'start.cjs', '--verbose'], env,
      exit: (code) => { exitCode = code; },
    },
  });
  return { invocation, exitCode };
}

test('passes the Aspire port without shell expansion on Windows', () => {
  const { invocation, exitCode } = launch({ PORT: '54321' });
  assert.equal(invocation[0], 'node.exe');
  assert.deepEqual(Array.from(invocation[1]), [
    'angular-cli.js', 'serve', '--port', '54321', '--host', '0.0.0.0', '--verbose',
  ]);
  assert.equal(exitCode, 0);
});

test('uses the standalone port and propagates Angular failures', () => {
  const { invocation, exitCode } = launch({}, { status: 2 });
  assert.equal(invocation[1][3], '4200');
  assert.equal(exitCode, 2);
  assert.throws(() => launch({}, { error: new Error('Cannot launch Node') }), /Cannot launch Node/);
});
