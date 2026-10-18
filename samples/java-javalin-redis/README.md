# Java (Javalin) + Redis leaderboard

A single-file C# AppHost orchestrating a **Java** service — a lightweight
[Javalin](https://javalin.io/) REST API backed by a Redis leaderboard. The Java
app is built and shipped as a container via a multi-stage Dockerfile (Maven
build → JRE runtime), so no host JDK is required to run it. Polyglot: **C#
AppHost, Java API, Redis store.**

## Tech Stack
- **AppHost**: single-file `apphost.cs`, base Aspire SDK
- **API**: Java 21 + [Javalin 7](https://javalin.io/) + [Jedis 8](https://github.com/redis/jedis) + Jackson 3, built from a Dockerfile
- **Store**: `redis:7-alpine` (public image) — a sorted-set leaderboard

## Running

```bash
cd samples/java-javalin-redis
aspire run
```

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/).

For a host-only Java build, use JDK 21+ and Maven 3.9+:
`mvn -f src/api/pom.xml clean verify`. Tests exercise the HTTP API with a mocked
Redis client; no Redis server or Docker is needed. Javalin 7 registers all routes
before startup, and the app explicitly selects its Jackson 3 mapper.

Aspire builds the Java image (Maven multi-stage build), starts Redis, and opens
the dashboard. Try the API:

```bash
# Submit some scores
curl -X POST http://<api-endpoint>/api/scores \
  -H 'Content-Type: application/json' -d '{"player":"Alice","score":100}'
curl -X POST http://<api-endpoint>/api/scores \
  -H 'Content-Type: application/json' -d '{"player":"Dave","score":300}'

# Top 10, highest first
curl http://<api-endpoint>/api/leaderboard
```

## What It Demonstrates
- A **C# AppHost orchestrating a Java service** with no host JDK — the
  multi-stage Dockerfile builds the jar with Maven and runs it on a JRE base.
- `AddDockerfile` for the Java app + `AddContainer` for Redis, wired with
  **container-to-container networking by resource name** (`REDIS_HOST=cache`).
- A real Redis data structure (a **sorted set**) driving the leaderboard ranking.
- `WithOtlpExporter()` configures the OpenTelemetry Java agent to export HTTP and
  Redis traces, runtime metrics, and Logback structured logs to the dashboard.
  The entrypoint trusts Aspire's mounted development certificate without disabling TLS.
- Because the API is already a Dockerfile, `aspire publish` ships it as a
  container image with no extra work.

## Project Layout

```
apphost.cs            single-file C# AppHost
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
src/api/
  pom.xml             Maven build (Javalin + Jedis, shaded fat jar)
  src/main/java/...    App.java — the Javalin leaderboard API
  Dockerfile          multi-stage: Maven build → JRE runtime
```
