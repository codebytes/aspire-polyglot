# Python FastAPI → Docker on publish

A single-file C# AppHost orchestrating a **Python FastAPI** service. In local
development the app runs **natively** — Aspire creates a virtual environment from
`requirements.txt` and launches `uvicorn` on your machine, no Dockerfile
required. When you run **`aspire publish`**, Aspire **auto-generates a
Dockerfile** for the Python app and emits a `docker-compose.yaml`, so the same
code ships as a container image. Polyglot: **C# AppHost, Python service.**

## Tech Stack
- **AppHost**: single-file `apphost.cs` with `Aspire.Hosting.Python` + `Aspire.Hosting.Docker`
- **Service**: Python [FastAPI](https://fastapi.tiangolo.com/) text-analyzer, served by `uvicorn`
- **Dev runtime**: native (venv on the host) — `AddUvicornApp`
- **Publish artifact**: an auto-generated Dockerfile + `docker-compose.yaml`

## Run locally (native Python)

```bash
cd samples/python-fastapi-docker
aspire run
```

Aspire creates `app/.venv`, installs `requirements.txt`, and starts `uvicorn`
on the host. Try it:

```bash
curl -k https://<api-endpoint>/api/info
curl -k -X POST https://<api-endpoint>/api/analyze \
  -H 'Content-Type: application/json' \
  -d '{"text":"Aspire makes polyglot apps easy. Easy!"}'
```

## Publish as a Docker image

```bash
aspire publish -o ./compose-output
```

Aspire writes `compose-output/api.Dockerfile` (a production-ready, non-root
Python image) and `compose-output/docker-compose.yaml`. Build and run it:

```bash
docker build -f compose-output/api.Dockerfile -t text-analyzer:local ./app
docker run -d -p 8080:8000 text-analyzer:local main:app --host 0.0.0.0 --port 8000
curl http://localhost:8080/api/info
```

## What It Demonstrates
- `AddUvicornApp("api", "app", "main:app")` — a **native Python dev experience**
  (auto venv + `uvicorn`) with health checks and external endpoints.
- **No hand-written Dockerfile**: `aspire publish` generates one tailored to the
  detected Python version and dependencies, then references it from a compose file.
- One app model, two runtimes: native `uvicorn` for dev, a container image for
  publish/deploy.

## Project Layout

```
apphost.cs            single-file C# AppHost (Python + Docker Compose publish)
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
app/
  main.py             FastAPI text-analyzer
  requirements.txt    fastapi + uvicorn (Aspire builds the venv from this)
```
