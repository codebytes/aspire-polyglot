# Polyglot Aspire

Polyglot samples for Aspire — demonstrating service orchestration across Python, JavaScript/Vite, Go, Java, C#, and mixed-language applications.

## Slides

The hosted slides for the related talk can be found at:\
[https://chris-ayers.com/aspire-polyglot/](https://chris-ayers.com/aspire-polyglot/)

The source deck for this presentation lives in [slides/](./slides/) and is built with [Marp](https://marp.app/).

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/)

## Samples

### No AppHost required 📊

The Aspire **dashboard** is a standalone container — point any OpenTelemetry app at it for logs, traces, and metrics, with no AppHost and no .NET.

| Sample | Entry point | Description |
|--------|-------------|-------------|
| [standalone-dashboard](./samples/standalone-dashboard) | `docker-compose.yml` | Standalone dashboard + **polyglot OTEL emitters** (Node.js + Python + Go) — traces, metrics, and logs from three languages in one dashboard |

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

## Ways to use multiple languages with Aspire

Aspire is polyglot on **two** independent axes: the language you **author the AppHost in**, and the languages your **workloads** are written in. You mix and match freely — a C# AppHost can orchestrate Python and Go, a TypeScript AppHost can orchestrate .NET, and so on.

### 1. AppHost authoring languages

The orchestrator itself can be written in several languages. C# and TypeScript are generally available; Python, Go, and Java are experimental preview (enabled via the `polyglotSupportEnabled` feature — see [`aspire.config.json`](./aspire.config.json)).

| AppHost language | File | Status | Sample(s) |
|------------------|------|--------|-----------|
| **C#** | `apphost.cs` (single-file) or `AppHost.csproj` | GA | [dotnet-react-postgres](./samples/dotnet-react-postgres), [postgres-adminer](./samples/postgres-adminer), [go-redis-compose](./samples/go-redis-compose), [python-fastapi-docker](./samples/python-fastapi-docker), [java-javalin-redis](./samples/java-javalin-redis), [dotnet-angular-cosmos](./samples/dotnet-angular-cosmos), [polyglot-event-stream](./samples/polyglot-event-stream) |
| **TypeScript** | `apphost.ts` (legacy) / `apphost.mts` | GA (13.4) | [hono-redis-urls](./samples/hono-redis-urls), [vite-react-api](./samples/vite-react-api), [ts-starter](./samples/ts-starter) |
| **Python** | `apphost.py` | Preview | [flask-markdown-wiki](./samples/preview/flask-markdown-wiki), [django-htmx-polls](./samples/preview/django-htmx-polls) |
| **Go** | `apphost.go` | Preview | [svelte-go-bookmarks](./samples/preview/svelte-go-bookmarks) |
| **Java** | `AppHost.java` | Preview | [spring-boot-postgres](./samples/preview/spring-boot-postgres) |
| **Rust** | `apphost.rs` | Planned (SDK codegen groundwork exists) | — |

> The same `.NET` hosting integrations are surfaced to every AppHost language through the **Aspire Type System (ATS)**: the CLI auto-generates a typed SDK (into `.aspire/modules/`, or `.modules/` for legacy `apphost.ts`) so guest-language AppHosts call the same integrations without hand-written bindings.

### 2. Adding a workload in any language

Inside the AppHost, each service is added with an `Add*` method. Some are language/runtime-specific; three are language-agnostic escape hatches that cover **anything else**.

| Mechanism | Adds | Demonstrated in |
|-----------|------|-----------------|
| `AddProject<T>()` | A .NET project | [dotnet-react-postgres](./samples/dotnet-react-postgres), [dotnet-angular-cosmos](./samples/dotnet-angular-cosmos), [polyglot-event-stream](./samples/polyglot-event-stream) |
| `AddViteApp()` | Vite frontends (React, Vue, Svelte, Angular 17+, Astro) | [dotnet-react-postgres](./samples/dotnet-react-postgres), [ts-starter](./samples/ts-starter) |
| `AddNodeApp()` | A Node.js entry file | [ts-starter](./samples/ts-starter) |
| `AddJavaScriptApp()` + `.WithYarn()` / `.WithPnpm()` / `.WithBun()` | Any JS/TS app, auto-detecting the package manager | _discussed — see [JavaScript apps in the AppHost](https://aspire.dev/integrations/frameworks/javascript/)_ |
| `AddUvicornApp()` (+ `.WithUv()`) / `AddPythonApp()` | Python ASGI / script apps | [python-fastapi-docker](./samples/python-fastapi-docker) (`AddUvicornApp`), [polyglot-event-stream](./samples/polyglot-event-stream) (`AddPythonApp`) |
| **`AddGoApp()`** — first-class `Aspire.Hosting.Go` (new in 13.4; Community Toolkit `Golang` deprecated) | A Go module (`go run`, auto-Dockerfile on publish) | _discussed — repo currently orchestrates Go via `AddDockerfile`/`AddExecutable`; see [Go apps in the AppHost](https://aspire.dev/integrations/frameworks/go/go-host/)_ |
| **`AddBunApp()`** — `Aspire.Hosting.JavaScript` (now core in 13.4) | A Bun app/script | _discussed — see [Bun integration](https://aspire.dev/integrations/frameworks/bun-apps/)_ |
| **`AddDenoApp()` / `AddDenoTask()`** — Community Toolkit | A Deno `run` script or `deno task` | _discussed — see [Deno apps in the AppHost](https://aspire.dev/integrations/frameworks/deno/deno-host/)_ |
| **`AddBlazorWasmProject()` + `AddBlazorGateway()`** (13.4 preview) | Blazor WebAssembly + browser-facing gateway | _discussed — see [Blazor hosting](https://aspire.dev/integrations/dotnet/blazor-hosting/)_ |
| `AddSpringApp()` — Community Toolkit | A Java Spring Boot app | _discussed — [spring-boot-postgres](./samples/preview/spring-boot-postgres) orchestrates Spring via `AddDockerfile`_ |
| **`AddDockerfile()`** | **Any language** built from a Dockerfile | [go-redis-compose](./samples/go-redis-compose) (Go), [java-javalin-redis](./samples/java-javalin-redis) (Java), [hono-redis-urls](./samples/hono-redis-urls) & [vite-react-api](./samples/vite-react-api) (Node/Python) |
| **`AddContainer()`** | **Any prebuilt public image** | [postgres-adminer](./samples/postgres-adminer), and Redis in [java-javalin-redis](./samples/java-javalin-redis) / [hono-redis-urls](./samples/hono-redis-urls) |
| **`AddExecutable()`** | **Any process/CLI** | [svelte-go-bookmarks](./samples/preview/svelte-go-bookmarks) (runs `npm` for the Svelte dev server) |

### 3. The polyglot glue (works in every language, no SDK required)

These wire the services together regardless of language — every runtime reads plain environment variables:

- **Service discovery** — Aspire injects `services__<name>__<protocol>__<index>` into each service.
- **Connection strings** — resources publish `ConnectionStrings__<resource>` to their consumers (`WithReference`).
- **OpenTelemetry** — any app that speaks OTLP shows up in the dashboard; the [standalone dashboard](https://aspire.dev/dashboard/standalone/) needs no AppHost at all.
- **Publishing** — `aspire publish` targets Docker Compose (`AddDockerComposeEnvironment`), Kubernetes, or bakes SPAs into a container (`PublishWithContainerFiles`), independent of workload language.

## Learn More

- [Aspire Documentation](https://aspire.dev/docs/)
- [Aspire VS Code Extension](https://marketplace.visualstudio.com/items?itemName=microsoft-aspire.aspire-vscode)
- [Aspire GitHub](https://github.com/microsoft/aspire)
- [David Fowler's Aspire 13 Samples](https://github.com/davidfowl/aspire-13-samples)
