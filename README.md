# Polyglot Aspire

Polyglot samples for .NET Aspire 13 — demonstrating service orchestration across TypeScript, Python, C#, and mixed-language applications.

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/)

## Samples

### TypeScript / Node.js

| Sample | Description |
|--------|-------------|
| [hono-valkey-ratelimiter](./samples/hono-valkey-ratelimiter) | Hono.js + Valkey — sliding-window API rate limiter with quote endpoint |
| [svelte-go-bookmarks](./samples/svelte-go-bookmarks) | Svelte SPA + Go API via `AddDockerfile` — bookmark manager with tagging |

### Python

| Sample | Description |
|--------|-------------|
| [flask-markdown-wiki](./samples/flask-markdown-wiki) | Flask + SQLite — Markdown wiki with create/edit/render pages |
| [flask-mongo-blog](./samples/flask-mongo-blog) | Flask + MongoDB + Mongo Express — blog engine with Markdown posts and tags |
| [django-htmx-polls](./samples/django-htmx-polls) | Django + HTMX — interactive voting polls with real-time bar charts, no SPA |
| [python-celery-reports](./samples/python-celery-reports) | FastAPI + Celery + Redis — async background report generation |

### C# / .NET

| Sample | Description |
|--------|-------------|
| [dotnet-angular-cosmos](./samples/dotnet-angular-cosmos) | ASP.NET Core + Angular 19 + CosmosDB emulator — recipe manager |

### Mixed / Polyglot

| Sample | Description |
|--------|-------------|
| [polyglot-event-stream](./samples/polyglot-event-stream) | C# producer + Python consumer + Node.js dashboard + Kafka — IoT sensor streaming |

## Slides

Presentation slides are in the [slides/](./slides/) directory, built with [Marp](https://marp.app/).

## Learn More

- [Aspire Documentation](https://aspire.dev/docs/)
- [Aspire VS Code Extension](https://marketplace.visualstudio.com/items?itemName=microsoft-aspire.aspire-vscode)
- [Aspire GitHub](https://github.com/dotnet/aspire)
- [David Fowler's Aspire 13 Samples](https://github.com/davidfowl/aspire-13-samples)
