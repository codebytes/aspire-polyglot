# Polyglot Aspire

Polyglot samples for .NET Aspire — demonstrating service orchestration across Python, JavaScript/Vite, Go, Java, C#, and mixed-language applications.

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/)

## Samples

### Python

| Sample | AppHost | Description |
|--------|---------|-------------|
| [flask-markdown-wiki](./samples/flask-markdown-wiki) | `apphost.py` | Flask + SQLite — Markdown wiki with create/edit/render pages |
| [django-htmx-polls](./samples/django-htmx-polls) | `apphost.py` | Django + HTMX — interactive voting polls with real-time bar charts |

### JavaScript / Vite

| Sample | AppHost | Description |
|--------|---------|-------------|
| [vite-react-api](./samples/vite-react-api) | `apphost.cs` | Vite + React + FastAPI + Redis — TODO app with caching |

### Go

| Sample | AppHost | Description |
|--------|---------|-------------|
| [svelte-go-bookmarks](./samples/svelte-go-bookmarks) | `apphost.cs` | Svelte SPA + Go API via `AddDockerfile` — bookmark manager with tagging |

### Java

| Sample | AppHost | Description |
|--------|---------|-------------|
| [spring-boot-postgres](./samples/spring-boot-postgres) | `apphost.cs` | Spring Boot + PostgreSQL + pgAdmin — notes REST API via `AddDockerfile` |

### C# / .NET

| Sample | AppHost | Description |
|--------|---------|-------------|
| [dotnet-angular-cosmos](./samples/dotnet-angular-cosmos) | `AppHost/` | ASP.NET Core + Angular 19 + CosmosDB emulator — recipe manager |

### Mixed / Polyglot

| Sample | AppHost | Description |
|--------|---------|-------------|
| [polyglot-event-stream](./samples/polyglot-event-stream) | `AppHost/` | C# producer + Python consumer + Node.js dashboard + Kafka — IoT sensor streaming |

## Slides

Presentation slides are in the [slides/](./slides/) directory, built with [Marp](https://marp.app/).

## Learn More

- [Aspire Documentation](https://aspire.dev/docs/)
- [Aspire VS Code Extension](https://marketplace.visualstudio.com/items?itemName=microsoft-aspire.aspire-vscode)
- [Aspire GitHub](https://github.com/dotnet/aspire)
- [David Fowler's Aspire 13 Samples](https://github.com/davidfowl/aspire-13-samples)
