# Vite React + FastAPI + Redis

A Vite-powered React frontend backed by a Python FastAPI service with Redis caching.

## Tech Stack
- **Frontend**: React 19 + TypeScript + Vite
- **Backend**: Python FastAPI
- **Cache**: Redis
- **AppHost**: Standalone `apphost.cs`

## Running

```bash
cd samples/vite-react-api
aspire run
```

## What It Demonstrates
- `AddNpmApp` for Vite dev server integration
- `AddPythonApp` for FastAPI backend
- `AddRedis` for caching layer
- Cross-service reference wiring
- Vite proxy to backend API
