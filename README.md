# Polyglot Aspire

Polyglot samples for Aspire — demonstrating service orchestration across Python, JavaScript/Vite, Go, Java, C#, and mixed-language applications.

## Slides

The hosted slides for the related talk can be found at:\
[https://chris-ayers.com/aspire-polyglot/](https://chris-ayers.com/aspire-polyglot/)

The source deck for this presentation lives in [slides/](./slides/) and is built with [Marp](https://marp.app/).

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/)

## Samples

### Single-file AppHost ⭐

The whole dev-time orchestrator is a single file — no AppHost project, no `Program.cs`.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [dotnet-react-postgres](./samples/dotnet-react-postgres) | `apphost.cs` | **C#** single-file AppHost — ASP.NET Core Minimal API + PostgreSQL + Vite React quotes board |
| [hono-redis-urls](./samples/hono-redis-urls) | `apphost.ts` | **TypeScript** single-file AppHost — Hono API + Redis + Vite URL shortener |

### Containers, Compose & publishing

Single-file AppHosts focused on Docker images, `aspire publish`, and polyglot services.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [postgres-adminer](./samples/postgres-adminer) | `apphost.cs` | **C#** — adds two public Docker images directly (PostgreSQL + Adminer) with `AddContainer`; no Dockerfiles |
| [go-redis-compose](./samples/go-redis-compose) | `apphost.cs` | **C# + Go** — `aspire publish` generates a runnable `docker-compose.yaml` (Go hit-counter API + Redis) |
| [python-fastapi-docker](./samples/python-fastapi-docker) | `apphost.cs` | **C# + Python** — FastAPI runs natively (uvicorn) in dev; `aspire publish` auto-generates its Dockerfile |
| [java-javalin-redis](./samples/java-javalin-redis) | `apphost.cs` | **C# + Java** — Javalin leaderboard API (multi-stage Maven build) + Redis via `AddDockerfile` |

### JavaScript / Vite

| Sample | AppHost | Description |
|--------|---------|-------------|
| [vite-react-api](./samples/vite-react-api) | `apphost.ts` | Vite + React + FastAPI + Redis — TODO app with caching |

### C# / .NET

| Sample | AppHost | Description |
|--------|---------|-------------|
| [dotnet-angular-cosmos](./samples/dotnet-angular-cosmos) | `AppHost/` | ASP.NET Core + Angular 19 + CosmosDB emulator — recipe manager |

### Mixed / Polyglot

| Sample | AppHost | Description |
|--------|---------|-------------|
| [polyglot-event-stream](./samples/polyglot-event-stream) | `AppHost/` | C# producer + Python consumer + Node.js dashboard + Kafka — IoT sensor streaming |

### Preview

> Experimental polyglot AppHosts (Python, Go, Java) live under [`samples/preview/`](./samples/preview).
> They rely on Aspire's preview polyglot AppHost support and may change as those languages stabilize.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [flask-markdown-wiki](./samples/preview/flask-markdown-wiki) | `apphost.py` | Flask + SQLite — Markdown wiki with create/edit/render pages |
| [django-htmx-polls](./samples/preview/django-htmx-polls) | `apphost.py` | Django + HTMX — interactive voting polls with real-time bar charts |
| [svelte-go-bookmarks](./samples/preview/svelte-go-bookmarks) | `apphost.go` | Svelte SPA + Go API via `AddDockerfile` — bookmark manager with tagging |
| [spring-boot-postgres](./samples/preview/spring-boot-postgres) | `AppHost.java` | Spring Boot + PostgreSQL + pgAdmin — notes REST API via `AddDockerfile` |

## Learn More

- [Aspire Documentation](https://aspire.dev/docs/)
- [Aspire VS Code Extension](https://marketplace.visualstudio.com/items?itemName=microsoft-aspire.aspire-vscode)
- [Aspire GitHub](https://github.com/dotnet/aspire)
- [David Fowler's Aspire 13 Samples](https://github.com/davidfowl/aspire-13-samples)
