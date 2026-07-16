# Hono + Redis URL Shortener (single-file TypeScript AppHost)

A URL shortener orchestrated by a **single-file TypeScript AppHost** — the entire
dev-time orchestrator is one `apphost.ts`, with no C# project in sight.

## Tech Stack
- **Backend**: [Hono](https://hono.dev/) (Node + TypeScript) API, containerized via a Dockerfile
- **Store**: Redis (container) holding short-code → URL mappings and click counts
- **Frontend**: Vite + vanilla TypeScript, containerized via a Dockerfile
- **AppHost**: single-file `apphost.ts` using the Aspire JavaScript SDK (`./.modules`)

## Running

```bash
cd samples/hono-redis-urls
aspire run
```

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/), and Node 20+.

Aspire builds the API and web images, starts Redis, and opens the dashboard. Open the
`web` endpoint, paste a long URL, and it returns a short link backed by Redis. Following
a short link redirects and bumps its click counter.

## What It Demonstrates
- A **single-file `apphost.ts`** that wires three resources with a fluent builder and
  no scaffolding project.
- `addContainer("cache", "redis:latest")` for a backing service, plus `addDockerfile`
  for the API and web apps so they share a network — the API reaches Redis by the
  container hostname `cache`.
- `withReference(api.getEndpoint("http"))` to inject the API endpoint into the web
  container as a service-discovery variable, which `vite.config.ts` uses to proxy `/api`.
- `withOtlpExporter()` + the OpenTelemetry Node SDK (`src/api/src/instrumentation.ts`)
  streaming traces, metrics, and logs to the Aspire dashboard.
- `withExternalHttpEndpoints()` and `withHttpEndpoint({ env: "PORT" })` for endpoint
  wiring.

## Project Layout

```
apphost.ts            single-file TypeScript AppHost (the orchestrator)
.modules/             Aspire JavaScript SDK (imported by apphost.ts)
aspire.config.json    points aspire at apphost.ts
apphost.run.json      launch profiles (dashboard/OTLP ports)
src/api/              Hono API + Dockerfile (Redis-backed shortener)
src/web/              Vite vanilla-TS frontend + Dockerfile
```
