const { spawnSync } = require('node:child_process');

const cli = require.resolve('@angular/cli/bin/ng.js');
const result = spawnSync(process.execPath, [
  cli, 'serve', '--port', process.env.PORT || '4200', '--host', '0.0.0.0',
  ...process.argv.slice(2),
], { stdio: 'inherit' });

if (result.error) throw result.error;
process.exit(result.status ?? 1);
