# Go + Redis → Docker Compose (`aspire publish`)

A single-file C# AppHost whose publish target is a **Docker Compose
environment**. Run it locally with `aspire run`, then run **`aspire publish`** to
turn the exact same app model into a `docker-compose.yaml` you can `docker
compose up` anywhere. Polyglot: the AppHost is **C#**, the API is **Go**, the
store is **Redis**.

## Tech Stack
- **AppHost**: single-file `apphost.cs` with `Aspire.Hosting.Docker`
- **API**: Go (`net/http` + `go-redis`) built from a Dockerfile — a page-hit counter
- **Store**: `redis:7-alpine` (public image)
- **Publish target**: `AddDockerComposeEnvironment("compose")`

## Run locally

```bash
cd samples/go-redis-compose
aspire run
```

Aspire builds the Go image, starts Redis, and opens the dashboard. `GET /api/hits`
increments a counter stored in Redis and returns the running total.

## Publish to Docker Compose

```bash
aspire publish -o ./compose-output
```

This generates `compose-output/docker-compose.yaml` (plus a `.env`) describing
every resource as a Compose service — the Go `api` (built image), `redis`, an
Aspire dashboard, and the shared network. To run the generated stack:

```bash
# Build the API image and point the compose file at it
docker build -t hitcounter-api:local src/api
cd compose-output
API_IMAGE=hitcounter-api:local docker compose up -d

# Hit the counter, then tear down
curl http://localhost:$(docker compose port api 8080 | cut -d: -f2)/api/hits
API_IMAGE=hitcounter-api:local docker compose down -v
```

> `aspire publish` parameterizes the built image as `${API_IMAGE}` so you can push
> it to any registry. For a fully automated build-and-push flow, configure a
> container registry and use `aspire deploy`.

## What It Demonstrates
- `AddDockerComposeEnvironment("compose")` as a **publish-time compute
  environment** — when present, every resource is emitted as a Compose service.
- One app model, two targets: `aspire run` for local dev and `aspire publish` for
  a portable `docker-compose.yaml`.
- A polyglot mix — C# AppHost orchestrating a Go service and a Redis container —
  with container-to-container networking by resource name (`REDIS_HOST=cache`).

## Project Layout

```
apphost.cs            single-file C# AppHost (Docker Compose environment)
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
src/api/              Go hit-counter API + Dockerfile
```
