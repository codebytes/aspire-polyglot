// Aspire TypeScript AppHost for Vite + React + FastAPI + Redis
// For more information, see: https://aspire.dev

import { createBuilder } from './.modules/aspire.js';

const builder = await createBuilder();

// Redis cache
const cache = builder.addContainer("cache", "redis:latest");

// FastAPI backend (./src/api has a Dockerfile)
const api = builder.addDockerfile("api", "./src/api")
  .withOtlpExporter()
  .withEnvironment("REDIS_HOST", "cache")
  .withEnvironment("REDIS_PORT", "6379")
  .withHttpEndpoint({ targetPort: 8080, env: "PORT" })
  .withExternalHttpEndpoints();

// Vite React frontend (./src/web has a Dockerfile)
const web = builder.addDockerfile("web", "./src/web")
  .withHttpEndpoint({ targetPort: 5173, env: "PORT" })
  .withExternalHttpEndpoints();

await builder.build().run();
