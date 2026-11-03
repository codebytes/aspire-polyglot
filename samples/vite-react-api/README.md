# Vite React + FastAPI + Redis

A **TypeScript** AppHost runs a React frontend and a Python FastAPI backend, both
**natively**, with Redis in a container. One browser click produces one trace that
spans the browser, the API, and Redis.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `web` | Vite dev server | `addViteApp` |
| `api` | native uvicorn (Aspire-managed venv) | `addUvicornApp` |
| `cache` | Redis container | `addRedis` |

```typescript
const cache = await builder.addRedis("cache");

const api = await builder.addUvicornApp("api", "./src/api", "main:app")
  .withReference(cache)
  .waitFor(cache);

await builder.addViteApp("web", "./src/web")
  .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
  .withReference(api)
  .waitFor(api);
```

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, Node.js 22+, Python 3.12+, Docker.

```bash
cd samples/vite-react-api
npm install
aspire run
```

Open **web**, add a TODO, reload (it survives in Redis), and delete it. In the dashboard:

- **Traces**: the `POST` trace starts with the browser's fetch span and continues
  into FastAPI and its Redis calls.
- **Structured logs**: `Created todo …` from Python's `logging`, linked to that trace.

## How it is wired

- `withReference(cache)` injects `CACHE_URI`; the API calls `redis.Redis.from_url`.
- `withReference(api)` gives Vite `API_HTTPS`, which `vite.config.ts` uses to proxy `/api`.
- The API's telemetry is the OpenTelemetry **distro**: `initialize()` reads Aspire's
  `OTEL_*` settings (including the dev certificate) and instruments FastAPI and Redis.
- `withOtlpExporter({ protocol: HttpProtobuf })` gives Vite an OTLP/HTTP endpoint;
  `vite.config.ts` forwards it as `VITE_OTEL_*` to
  [`instrumentation.ts`](src/web/src/instrumentation.ts), which traces page loads and
  `fetch` calls and propagates `traceparent` to the API.
- Concurrent edits are safe: TODO changes run in a Redis `WATCH` transaction.

## Checks

```bash
npm run typecheck                                  # the AppHost
(cd src/web && npm install && npm run build)       # the frontend
cd src/api && uv run --with-requirements requirements.txt python -m unittest -v test_todos
```
