// TypeScript AppHost for a URL shortener: Hono API + Vite frontend + Redis.
// Both Node.js workloads run natively; only Redis runs in a container.

import { createBuilder, OtlpProtocol } from './.aspire/modules/aspire.mjs';

const builder = await createBuilder();

// Redis with a generated password. Referencing it injects CACHE_URI.
const cache = await builder.addRedis("cache");

// Hono on Node.js. The "start" script preloads OpenTelemetry's zero-code
// instrumentation; OTLP over HTTP avoids gRPC TLS limits for IP endpoints.
const api = await builder.addNodeApp("api", "./src/api", "src/index.ts")
  .withRunScript("start")
  .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
  .withReference(cache)
  .waitFor(cache)
  .withHttpEndpoint({ env: "PORT" })
  .withHttpHealthCheck({ path: "/health" });

// Vite (vanilla TypeScript) frontend; vite.config.ts proxies /api and /r to the API.
await builder.addViteApp("web", "./src/web")
  .withReference(api)
  .waitFor(api)
  .withExternalHttpEndpoints();

await builder.build().run();
