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

## Browser telemetry

The `web` resource exports browser traces through the dashboard's OTLP/HTTP
listener. Aspire supplies a container-network URL; Vite maps `aspire.dev.internal`
and the legacy `host.docker.internal` hostname to `localhost` for the browser.
The collector's scheme, port, and path are preserved, and HTTPS certificate
validation remains enabled.
