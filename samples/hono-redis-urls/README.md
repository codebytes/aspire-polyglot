# URL shortener: Hono + Vite + Redis

A **TypeScript** AppHost runs a [Hono](https://hono.dev/) API and a Vite frontend,
both **natively on Node.js**, with Redis in a container.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `api` | `npm start` (Node.js runs the TypeScript directly) | `addNodeApp` |
| `web` | Vite dev server | `addViteApp` |
| `cache` | Redis container | `addRedis` |

```typescript
const cache = await builder.addRedis("cache");

const api = await builder.addNodeApp("api", "./src/api", "src/index.ts")
  .withRunScript("start")
  .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
  .withReference(cache)
  .withHttpEndpoint({ env: "PORT" });

await builder.addViteApp("web", "./src/web").withReference(api).waitFor(api);
```

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, Node.js 22.18+ (built-in TypeScript
type stripping), Docker. Aspire installs each app's npm packages before starting it.

```bash
cd samples/hono-redis-urls
npm install
aspire run
```

Open **web**, shorten a URL, and click the short link. In the dashboard:

- **Structured logs**: pino records such as `shortened URL` and `redirected`.
- **Traces**: `POST /api/shorten` with a child span for each Redis command.

## How it is wired

- `withReference(cache)` injects `CACHE_URI`; `new Redis(process.env.CACHE_URI)` connects.
- `withReference(api)` gives Vite `API_HTTP`; `vite.config.ts` proxies `/api` and `/r`.
- `npm start` preloads [`src/otel.ts`](src/api/src/otel.ts): the OpenTelemetry Node
  SDK with the HTTP, ioredis, and pino instrumentations, configured entirely by
  Aspire's `OTEL_*` variables. Its loader hook lets them patch ES module imports.

## Checks

```bash
npm run typecheck                                   # the AppHost
(cd src/api && npm install && npm run typecheck)    # the API
(cd src/web && npm install && npm run build)        # the frontend
```
