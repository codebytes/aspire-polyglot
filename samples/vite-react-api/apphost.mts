// TypeScript AppHost: React (Vite) + Python FastAPI + Redis.
// Both workloads run natively; only Redis runs in a container.

import { createBuilder, OtlpProtocol } from './.aspire/modules/aspire.mjs';

const builder = await createBuilder();

// Redis with a generated password. Referencing it injects CACHE_URI.
const cache = await builder.addRedis("cache");

// FastAPI via uvicorn. Aspire creates a venv from src/api/requirements.txt.
const api = await builder.addUvicornApp("api", "./src/api", "main:app")
  .withReference(cache)
  .waitFor(cache)
  .withHttpHealthCheck({ path: "/api/health" });

// Vite dev server. It proxies /api to the API, and the browser sends its own
// traces to the dashboard over OTLP/HTTP (browsers cannot speak gRPC).
await builder.addViteApp("web", "./src/web")
  .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
  .withReference(api)
  .waitFor(api)
  .withExternalHttpEndpoints();

await builder.build().run();
