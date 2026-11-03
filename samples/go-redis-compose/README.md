# Go + Redis: native in dev, Docker Compose on publish

A single-file C# AppHost runs a **Go** hit-counter API **natively** (`go run .`)
next to a Redis container. `aspire publish` turns the same model into a
`docker-compose.yaml` and **generates the Go Dockerfile for you**.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `api` | native Go process | `AddGoApp` (`Aspire.Hosting.Go`, preview) |
| `cache` | Redis container | `AddRedis` |
| `compose` | publish target | `AddDockerComposeEnvironment` |

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, Go 1.26+, Docker.

```bash
cd samples/go-redis-compose
aspire run
```

Open the `api` URL and click **Count a hit**, or call `/api/hits` directly.
In the dashboard you will see:

- **Structured logs** from Go's `slog` (`hit counted hits=N`), correlated to traces.
- **Traces** for each request with a child span for the Redis `INCR`.
- **Metrics** for HTTP requests and the Redis connection pool.

## How it is wired

- `WithReference(cache)` injects `CACHE_URI` (`redis://:password@host:port`);
  `redis.ParseURL` turns it into a client, so there is no hand-built connection string.
- `WithHttpEndpoint(env: "PORT")` tells the Go server which port to bind.
- [`telemetry.go`](src/api/telemetry.go) uses `autoexport`, which reads the standard
  `OTEL_*` variables Aspire injects. Logs go to stdout **and** OTLP via `slog.NewMultiHandler`.

## Publish to Docker Compose

```bash
aspire publish -o compose-output
```

This writes `compose-output/docker-compose.yaml`, `.env`, and `api.Dockerfile`
(a multi-stage Go build). Build the image and start the stack:

```bash
docker build -f compose-output/api.Dockerfile -t hitcounter-api:local src/api
cd compose-output
API_IMAGE=hitcounter-api:local CACHE_PASSWORD=choose-a-password docker compose up -d
docker compose port api 8000
```

Open `/api/hits` on the reported host port and refresh to watch the counter grow.
The published stack also includes an Aspire dashboard container. Tear it down with
`docker compose down`.

## Layout

```text
apphost.cs           single-file C# AppHost (Go + Redis + Compose publish target)
src/api/main.go      HTTP API: /api/hits, /health, and a one-button page
src/api/telemetry.go OpenTelemetry traces, metrics, and slog logs
```
