// Aspire TypeScript AppHost for Vite + React + FastAPI + Redis
// For more information, see: https://aspire.dev

import { createBuilder, OtlpProtocol } from './.modules/aspire.js';

const builder = await createBuilder();

// Redis cache
const cache = builder.addContainer("cache", "redis:latest");

// FastAPI backend (./src/api has a Dockerfile) — OTel via gRPC (default).
const api = builder.addDockerfile("api", "./src/api")
  .withOtlpExporter()
  .withEnvironment("REDIS_HOST", "cache")
  .withEnvironment("REDIS_PORT", "6379")
  .withHttpEndpoint({ targetPort: 8080, env: "PORT" })
  .withExternalHttpEndpoints();

// Vite React frontend (./src/web has a Dockerfile).
//
// We OPT INTO HTTP/protobuf for OTel here so that the SAME endpoint URL the
// container uses for server-side telemetry can also be re-exposed to client
// code (the browser cannot speak gRPC). vite.config.ts re-exports the OTEL_*
// env vars as VITE_OTEL_* and rewrites host.docker.internal -> localhost so
// the running SPA can POST traces to the dashboard from the user's machine.
//
// The dashboard's HTTP/protobuf listener must be enabled before this works.
// `apphost.run.json` sets ASPIRE_DASHBOARD_OTLP_HTTP_ENDPOINT_URL on both
// launch profiles so plain `aspire run` works out of the box. See
// .squad/decisions/inbox/parker-vite-react-otel.md for the full design.
const web = builder.addDockerfile("web", "./src/web")
  .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
  .withHttpEndpoint({ targetPort: 5173, env: "PORT" })
  .withExternalHttpEndpoints();

await builder.build().run();
