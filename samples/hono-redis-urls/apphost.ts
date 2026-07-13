// Aspire TypeScript AppHost for a URL shortener: Hono API + Redis + Vite (vanilla TS).
// The entire dev-time orchestrator is this single file. For more information,
// see: https://aspire.dev

import { createBuilder } from './.modules/aspire.js';

const builder = await createBuilder();

// Redis stores the short-code -> URL mappings and click counts.
const cache = builder.addContainer("cache", "redis:latest");

// Hono (Node/TS) API. `./src/api` has a Dockerfile, so it runs in a container
// on the same network as Redis and reaches it by the container hostname "cache".
const api = builder.addDockerfile("api", "./src/api")
  .withOtlpExporter()
  .withEnvironment("REDIS_HOST", "cache")
  .withEnvironment("REDIS_PORT", "6379")
  .withHttpEndpoint({ targetPort: 8080, env: "PORT" })
  .withExternalHttpEndpoints();

// Vite vanilla-TS frontend. `./src/web` has a Dockerfile; vite.config.ts reads
// the API endpoint Aspire injects (services__api__*) and proxies "/api" to it.
builder.addDockerfile("web", "./src/web")
  .withReference(api.getEndpoint("http"))
  .waitFor(api)
  .withHttpEndpoint({ targetPort: 5173, env: "PORT" })
  .withExternalHttpEndpoints();

await builder.build().run();
