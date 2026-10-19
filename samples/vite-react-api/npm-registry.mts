import { execSync } from 'node:child_process';

export function getNpmRegistry(environment: NodeJS.ProcessEnv, directory: string): string {
  return environment.NPM_CONFIG_REGISTRY
    ?? environment.npm_config_registry
    ?? execSync('npm config get registry', {
      cwd: directory,
      env: environment,
      encoding: 'utf8',
    }).trim();
}
