# Vite React + FastAPI + Redis

A Vite-powered React frontend backed by a Python FastAPI service with Redis caching.

## Tech Stack
- **Frontend**: React 19 + TypeScript + Vite
- **Backend**: Python FastAPI
- **Cache**: Redis
- **AppHost**: TypeScript `apphost.ts`

## Running

```bash
cd samples/vite-react-api
npm install
aspire run
```

## What It Demonstrates
- TypeScript AppHost (`apphost.ts`) orchestrating polyglot services
- `addDockerfile` for Vite frontend and FastAPI backend containers
- `addContainer` for Redis caching layer
- Environment variable wiring between services
- HTTP endpoint configuration with external access
