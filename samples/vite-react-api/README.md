# Vite React + FastAPI + Redis

A Vite-powered React frontend backed by a Python FastAPI service with Redis caching.

## Tech Stack
- **Frontend**: React 19 + TypeScript + Vite
- **Backend**: Python FastAPI
- **Cache**: Redis
- **AppHost**: TypeScript `apphost.mts`

## Running

```bash
cd samples/vite-react-api
npm install
aspire run
```

The frontend image build uses `NPM_CONFIG_REGISTRY`, then `npm_config_registry`,
then `npm config get registry` from the AppHost directory. This preserves explicit
overrides and uses your project/user `.npmrc` feed when no override is exported.
The API image build uses `PIP_INDEX_URL`, then `UV_DEFAULT_INDEX`, then public PyPI.
This forwards registry settings, not `.npmrc` authentication tokens.

### Recovering a failed image build

`lookup registry-1.docker.io: no such host` means Docker could not resolve Docker
Hub while fetching a base image. Restore Docker's network/DNS access first; changing
application code or image versions does not address that failure.

If a build fails downloading npm packages, check `npm config get registry` and
access to that feed from Docker. After connectivity is restored, retry the failed
resource without stopping the whole AppHost:

```bash
aspire resource api start # If the API failed to build
aspire resource web start # If the frontend failed to build
aspire describe
```

Retry only resources in a failed/stopped state; the frontend automatically starts
when its API dependency becomes ready.

## What It Demonstrates
- TypeScript AppHost (`apphost.mts`) orchestrating polyglot services
- `addDockerfile` for Vite frontend and FastAPI backend containers
- `addContainer` for Redis caching layer
- Environment variable wiring between services
- HTTP endpoint configuration with external access
- Redis transactions retry concurrent TODO changes without overwriting other requests

## Backend regression checks

```bash
cd src/api
uv run --with-requirements requirements.txt python -B -m unittest -v test_todos
```

Run `npm test` from the sample root for build-registry configuration checks.

## Browser telemetry

The `web` resource exports browser traces through the dashboard's OTLP/HTTP
listener. Aspire supplies a container-network URL; Vite maps `aspire.dev.internal`
and the legacy `host.docker.internal` hostname to `localhost` for the browser.
The collector's scheme, port, and path are preserved, and HTTPS certificate
validation remains enabled.
