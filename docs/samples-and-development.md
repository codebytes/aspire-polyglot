# Polyglot Aspire Samples and Development

[Back to the talk](../README.md)

Detailed sample catalog and development notes. Run commands from the repository
root unless a sample's README says otherwise.

## Running the Samples

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/) for container-based samples. Individual sample READMEs list the required language runtimes.

The samples pin Aspire SDK and integration packages to **13.6.1**. After changing
an AppHost's integration versions, run `aspire restore --apphost <entry-point>`
to regenerate its committed `.aspire/modules/` bindings; do not edit generated
bindings by hand.

AppHost launch profiles use dynamically allocated loopback ports for the dashboard,
OTLP collectors, and resource service so multiple samples can run side by side.
Use the URLs printed by Aspire rather than assuming a fixed dashboard port.
The standalone dashboard is the exception: its documented ports are fixed and
bound to IPv4 loopback because the local demo disables authentication.

## Validating Every Sample

Run the runtime smoke suite from the repository root:

```powershell
.\scripts\Test-Samples.ps1
```

This validates all **16 samples**, including the four preview AppHosts and the
standalone Compose stack. On Windows, 14 samples run natively; the original Go
and Java preview AppHosts run inside an isolated Linux validation container.
No C# fallback replaces those AppHosts. On Linux/macOS, the wrapper runs all
samples natively. Native AppHosts run with `--isolated`; the Linux previews use
their own per-run Docker daemon, network, and filesystem instead.
the suite waits for their resources with `aspire wait`, discovers their assigned
URLs with `aspire describe`, and exercises real HTTP requests, databases, and
message delivery. A failed assertion, startup, or cleanup makes the command exit
with a nonzero status; later samples still run.

Install the prerequisites for the samples being tested: Aspire CLI **13.6.1**,
.NET 10, Docker running Linux containers, Node.js **22.22.3+, 24.15+, or 26+**,
Python 3.12+ with `uv`, Go 1.27.2+ (or automatic Go toolchain downloads), and a
JDK for the Java preview AppHost (validated with JDK 25). Language packages and
container images are restored by Aspire and Docker. The first run needs package
feed and image-registry access and can take several minutes per sample.
`--use-system-ca` includes the system certificate store. For the native Python
service, the suite also exports the public .NET development certificate into a
temporary directory and uses it explicitly for local HTTPS requests. This avoids
conflicting older development certificates in Windows stores. No private key is
exported, no machine trust settings are changed, and HTTPS verification remains
enabled. Windows ARM64 also needs the x64 .NET 10 runtime for the Kafka sample.
The Windows Linux-preview path supplies its own Go/JDK/Node toolchains; it needs
Docker Compose and permission to run the documented privileged daemon.

To list or rerun selected samples:

```powershell
node scripts\smoke-samples.mjs --list
.\scripts\Test-Samples.ps1 -Sample go-redis-compose, preview/django-htmx-polls
.\scripts\Test-Samples.ps1 -Sample preview/svelte-go-bookmarks, preview/spring-boot-postgres
```

### Linux validation for Windows preview hosts

Aspire 13.6.1's generated Go and Java clients can hang during Windows named-pipe
authentication. The Go failure also reproduces with `GOARCH=amd64`, so x64
emulation does not fix it. See [microsoft/aspire#20056](https://github.com/microsoft/aspire/issues/20056).
Linux uses Unix sockets instead. Do not hand-patch the generated SDK to hide the
failure.

On Windows, `Test-Samples.ps1` builds the pinned Aspire 13.6.1 Linux tool image
from `scripts/linux-previews.Dockerfile`, with .NET 10, Go 1.27.2, JDK 25, Node 26,
and Docker CLI. The dedicated Docker ignore file excludes Windows dependencies,
build outputs, and unrelated repository files. The existing slide devcontainer
is not changed.

`scripts/linux-previews.compose.yml` starts a private Docker-in-Docker daemon
and the validation runner in the same network namespace. It does not mount the
host Docker socket or require Docker Desktop host networking. The daemon has a
health check and exposes only a private Unix socket. A shared temporary volume
lets the runner and daemon access Aspire's temporary files at the same path.

The preview CLI does not accept `--launch-profile` for Go/Java, so the container
sets the HTTP endpoint variables directly. Fixed internal ports are safe in this
per-run namespace. The dashboard and resource service stay on loopback; the
authenticated OTLP collectors listen on the private daemon's interfaces so
workload containers can reach them. Aspire's extra container tunnel is disabled
inside this already isolated network. No host ports are published.

The Docker-in-Docker service is **privileged**: run only trusted code. Each run
has a unique Compose project. The wrapper removes its containers and temporary
volumes, including the nested daemon's images/data, after completion; only the
outer tool image stays cached for reruns. The host's application containers and
database volumes are not part of that project and are not removed.

The lower-level `node --use-system-ca scripts\smoke-samples.mjs` command remains
available for native runs. It reports unsupported Windows preview starts as
failures rather than silently skipping them.

| Sample | Runtime assertions |
|--------|--------------------|
| `terminal-demo` | Shell output, JavaScript evaluation, and a real Node pseudo-terminal |
| `python-fastapi-docker` | Analyzer page, service identity, exact text statistics |
| `ts-starter` | Frontend proxy, five forecasts, dates and temperature conversion |
| `postgres-adminer` | Adminer page and a real PostgreSQL query |
| `go-redis-compose` | Successive requests increment the Redis counter |
| `java-javalin-redis` | Score submission and Redis leaderboard ordering |
| `hono-redis-urls` | Frontend proxy, short-link redirect, click count, invalid URL rejection |
| `vite-react-api` | Create, read, and delete a Redis-backed TODO through the frontend |
| `dotnet-react-postgres` | Seed data, valid import, transactional invalid-import rejection |
| `dotnet-angular-cosmos` | Seed data, create/read/update/delete, title and ingredient searches through Angular |
| `polyglot-event-stream` | Publish an event; verify Python aggregation, Node readings, and anomaly delivery |
| `preview/flask-markdown-wiki` | Create, render, edit, and invalidate cached Markdown |
| `preview/django-htmx-polls` | CSRF-protected poll creation and voting, PostgreSQL selection, all poll regressions including concurrent votes |
| `preview/svelte-go-bookmarks` | Create/search/delete through Svelte; verify the row actually reached PostgreSQL, not the in-memory fallback |
| `preview/spring-boot-postgres` | Create/list/read/delete notes, then verify 404 |
| `standalone-dashboard` | Dashboard HTTP response, four running containers, jobs processed by all three workers |

The suite refuses to take over an already-running copy of a target AppHost or an
existing `aspire-dashboard` container. Stop those explicitly first. It stops only
the AppHosts and Compose project it starts, preserves named database volumes, and
removes its temporary terminal tapes. Test records are deleted where the API
supports deletion; the quotes test removes only its own inserted row using
`psql`. Other test records live in the samples' disposable, non-volume-backed
containers. The standalone sample still reserves its documented loopback ports.

These are runtime smoke checks, not replacements for the focused unit tests,
frontend production builds/lints, browser interaction checks, or publish-artifact
checks documented in the individual sample READMEs. The standalone check proves
the workers run; inspect the dashboard to verify received telemetry. The suite
does not provision or deploy Azure resources.

## Samples

### No AppHost required 📊

The Aspire **dashboard** is a standalone container — point any OpenTelemetry app at it for logs, traces, and metrics, with no AppHost and no .NET.

| Sample | Entry point | Description |
|--------|-------------|-------------|
| [standalone-dashboard](../samples/standalone-dashboard) | `docker-compose.yml` | Standalone dashboard + **polyglot OTEL emitters** (Node.js + Python + Go) — traces, metrics, and logs from three languages in one dashboard |

### Single-file AppHost ⭐

The whole dev-time orchestrator is a single file — no AppHost project, no `Program.cs`.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [dotnet-react-postgres](../samples/dotnet-react-postgres) | `apphost.cs` | **C#** single-file AppHost — ASP.NET Core + PostgreSQL + React quotes board, with CLI/dashboard seed commands and JSON file import |
| [hono-redis-urls](../samples/hono-redis-urls) | `apphost.mts` | **TypeScript** single-file AppHost — Hono API + Redis + Vite URL shortener |

### Interactive terminals (experimental)

| Sample | AppHost | Description |
|--------|---------|-------------|
| [terminal-demo](../samples/terminal-demo) | `apphost.cs` | **C#** — local shell + Node.js REPL, interactive in the dashboard or CLI via `WithTerminal()`; no Docker required |

### Containers, Compose & publishing

Single-file AppHosts focused on Docker images, `aspire publish`, and polyglot services.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [postgres-adminer](../samples/postgres-adminer) | `apphost.cs` | **C#** — adds two public Docker images directly (PostgreSQL + Adminer) with `AddContainer`; no Dockerfiles |
| [go-redis-compose](../samples/go-redis-compose) | `apphost.cs` | **C# + Go** — `aspire publish` generates a runnable `docker-compose.yaml` (Go hit-counter API + Redis) |
| [python-fastapi-docker](../samples/python-fastapi-docker) | `apphost.cs` | **C# + Python** — FastAPI runs natively (uvicorn) in dev; `aspire publish` auto-generates its Dockerfile |
| [java-javalin-redis](../samples/java-javalin-redis) | `apphost.cs` | **C# + Java** — Javalin leaderboard API (multi-stage Maven build) + Redis via `AddDockerfile` |

### JavaScript / Vite

| Sample | AppHost | Description |
|--------|---------|-------------|
| [vite-react-api](../samples/vite-react-api) | `apphost.mts` | Vite + React + FastAPI + Redis — TODO app with caching |
| [ts-starter](../samples/ts-starter) | `apphost.mts` | Express + React weather forecast — native TypeScript services and browser tracing |

### C# / .NET

| Sample | AppHost | Description |
|--------|---------|-------------|
| [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos) | `AppHost/` | ASP.NET Core + Angular 22 + Cosmos DB vNext emulator — recipe manager; `aspire publish` generates Azure Container Apps and Cosmos DB Bicep |

### Mixed / Polyglot

| Sample | AppHost | Description |
|--------|---------|-------------|
| [polyglot-event-stream](../samples/polyglot-event-stream) | `AppHost/` | C# producer + Python consumer + Node.js dashboard + Kafka — IoT sensor streaming |

### Preview

> Experimental polyglot AppHosts (Python, Go, Java) live under [`samples/preview/`](../samples/preview).
> They rely on Aspire's preview polyglot AppHost support and may change as those languages stabilize.

| Sample | AppHost | Description |
|--------|---------|-------------|
| [flask-markdown-wiki](../samples/preview/flask-markdown-wiki) | `apphost.py` | Flask + SQLite — Markdown wiki with create/edit/render pages |
| [django-htmx-polls](../samples/preview/django-htmx-polls) | `apphost.py` | Django + HTMX — interactive voting polls with real-time bar charts |
| [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) | `apphost.go` | Svelte SPA + Go API via `AddDockerfile` — bookmark manager with tagging |
| [spring-boot-postgres](../samples/preview/spring-boot-postgres) | `AppHost.java` | Spring Boot + PostgreSQL — notes REST API via `AddDockerfile` |

## Ways to use multiple languages with Aspire

Aspire is polyglot on **two** independent axes: the language you **author the AppHost in**, and the languages your **workloads** are written in. You mix and match freely — a C# AppHost can orchestrate Python and Go, a TypeScript AppHost can orchestrate .NET, and so on.

### 1. AppHost authoring languages

The orchestrator itself can be written in several languages. C# and TypeScript are generally available; Python, Go, Java, and (since 13.6) Rust are experimental preview (enabled via the `polyglotSupportEnabled` feature — see [`aspire.config.json`](../aspire.config.json)).

| AppHost language | File | Status | Sample(s) |
|------------------|------|--------|-----------|
| **C#** | `apphost.cs` (single-file) or `AppHost.csproj` | GA | [dotnet-react-postgres](../samples/dotnet-react-postgres), [terminal-demo](../samples/terminal-demo), [postgres-adminer](../samples/postgres-adminer), [go-redis-compose](../samples/go-redis-compose), [python-fastapi-docker](../samples/python-fastapi-docker), [java-javalin-redis](../samples/java-javalin-redis), [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos), [polyglot-event-stream](../samples/polyglot-event-stream) |
| **TypeScript** | `apphost.mts` | GA (13.4) | [hono-redis-urls](../samples/hono-redis-urls), [vite-react-api](../samples/vite-react-api), [ts-starter](../samples/ts-starter) |
| **Python** | `apphost.py` | Preview | [flask-markdown-wiki](../samples/preview/flask-markdown-wiki), [django-htmx-polls](../samples/preview/django-htmx-polls) |
| **Go** | `apphost.go` | Preview | [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) |
| **Java** | `AppHost.java` | Preview | [spring-boot-postgres](../samples/preview/spring-boot-postgres) |
| **Rust** | `apphost.rs` | Experimental (13.6, `experimentalPolyglot:rust` flag) | — |

> The same `.NET` hosting integrations are surfaced to every AppHost language through the **Aspire Type System (ATS)**: the CLI auto-generates a typed SDK into `.aspire/modules/` so guest-language AppHosts call the same integrations without hand-written bindings.

### 2. Adding a workload in any language

Inside the AppHost, each service is added with an `Add*` method. Some are language/runtime-specific; three are language-agnostic escape hatches that cover **anything else**.

| Mechanism | Adds | Demonstrated in |
|-----------|------|-----------------|
| `AddProject<T>()` / `AddProject(name, path)` | A .NET project | [dotnet-react-postgres](../samples/dotnet-react-postgres), [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos), [polyglot-event-stream](../samples/polyglot-event-stream) |
| `AddViteApp()` | Vite frontends (React, Vue, Svelte, Angular 17+, Astro) | [dotnet-react-postgres](../samples/dotnet-react-postgres), [ts-starter](../samples/ts-starter) |
| `AddNodeApp()` | A Node.js entry file | [ts-starter](../samples/ts-starter) |
| `AddJavaScriptApp()` + `.WithYarn()` / `.WithPnpm()` / `.WithBun()` | Any JS/TS app, auto-detecting the package manager | _discussed — see [JavaScript apps in the AppHost](https://aspire.dev/integrations/frameworks/javascript/)_ |
| `AddUvicornApp()` (+ `.WithUv()`) / `AddPythonApp()` | Python ASGI / script apps | [python-fastapi-docker](../samples/python-fastapi-docker) (`AddUvicornApp`), [polyglot-event-stream](../samples/polyglot-event-stream) (`AddPythonApp`) |
| **`AddGoApp()`** — first-class `Aspire.Hosting.Go` (new in 13.4; Community Toolkit `Golang` deprecated) | A Go module (`go run`, auto-Dockerfile on publish) | _discussed — repo currently orchestrates Go via `AddDockerfile`/`AddExecutable`; see [Go apps in the AppHost](https://aspire.dev/integrations/frameworks/go/go-host/)_ |
| **`AddBunApp()`** — `Aspire.Hosting.JavaScript` (now core in 13.4) | A Bun app/script | _discussed — see [Bun integration](https://aspire.dev/integrations/frameworks/bun-apps/)_ |
| **`AddDenoApp()`** — `Aspire.Hosting.JavaScript` (experimental in 13.6, `ASPIREDENO001`); the Community Toolkit integration also offers `AddDenoTask()` | A Deno script (task and serve modes) | _discussed — see [Deno apps in the AppHost](https://aspire.dev/integrations/frameworks/deno/deno-host/)_ |
| **`AddBlazorWasmProject()` + `AddBlazorGateway()`** (13.4 preview) | Blazor WebAssembly + browser-facing gateway | _discussed — see [Blazor hosting](https://aspire.dev/integrations/dotnet/blazor-hosting/)_ |
| **`AddSpringBootApp()` / `AddQuarkusApp()` / `AddJavaApp()`** — `Aspire.Hosting.Java` (first-party preview in 13.6; replaces the Community Toolkit package) | Java apps through Maven or Gradle wrappers, or a prebuilt JAR | _discussed — [spring-boot-postgres](../samples/preview/spring-boot-postgres) and [java-javalin-redis](../samples/java-javalin-redis) orchestrate Java via `AddDockerfile`; see [Java apps in the AppHost](https://aspire.dev/integrations/frameworks/java/java-host/)_ |
| **`AddRustApp()`** — `Aspire.Hosting.Rust` (first-party preview in 13.6) | A Cargo app | _discussed — see [Rust apps in the AppHost](https://aspire.dev/integrations/frameworks/rust/rust-host/)_ |
| **`AddDockerfile()`** | **Any language** built from a Dockerfile | [go-redis-compose](../samples/go-redis-compose) (Go), [java-javalin-redis](../samples/java-javalin-redis) (Java), [hono-redis-urls](../samples/hono-redis-urls) & [vite-react-api](../samples/vite-react-api) (Node/Python) |
| **`AddContainer()`** | **Any prebuilt public image** | [postgres-adminer](../samples/postgres-adminer), and Redis in [java-javalin-redis](../samples/java-javalin-redis) / [hono-redis-urls](../samples/hono-redis-urls) |
| **`AddExecutable()`** | **Any process/CLI** | [terminal-demo](../samples/terminal-demo) (interactive local shell + Node.js REPL), [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) (runs `npm` for the Svelte dev server) |

### 3. The polyglot glue (works in every language, no SDK required)

These wire the services together regardless of language — every runtime reads plain environment variables:

- **Service discovery** — Aspire injects `services__<name>__<protocol>__<index>` into each service.
- **Connection strings** — resources publish `ConnectionStrings__<resource>` to their consumers (`WithReference`).
- **OpenTelemetry** — any app that speaks OTLP shows up in the dashboard; the [standalone dashboard](https://aspire.dev/dashboard/standalone/) needs no AppHost at all.
- **Publishing** — `aspire publish` emits target artifacts such as Docker Compose or Azure Bicep, independent of workload language. `PublishWithContainerFiles` can place SPA assets in a backend image. The [recipe manager's Bicep demo](../samples/dotnet-angular-cosmos#publish-bicep--artifact-only-live-demo) generates and compiles templates without deploying Azure resources.

## Agent tooling

Use shared skills from [codebytes/skills](https://github.com/codebytes/skills) through your agent's installation rather than copying them into this repository. Agent tooling is optional; running the samples and building the slides does not require it.

The repo-local Squad agent, skills, templates, and issue workflows have been retired. Decisions, agent histories, and team context remain under [.squad/](../.squad/) as historical records.

## Slide Publishing

The source deck for this presentation lives in [slides/](../slides/) and is built with [Marp](https://marp.app/).
The [Pages workflow](../.github/workflows/marp-pages.yml) generates and deploys HTML and PDF on pushes to `main` or when run manually.

Motion lives in [aspire-motion.css](../slides/themes/aspire-motion.css). It plays only in the HTML presentation, in browsers that support the View Transition API (best in Chrome or Edge). The PDF and the VS Code preview show each slide's complete, static state, and reduced-motion settings are respected. To author:

- Write `*` bullets to reveal a list one click at a time.
- Use `<!-- _transition: aspire-aperture -->` on the slide before a section divider.
- Mark one element on each of two adjacent slides with the same `data-morph` value, mapped to a `view-transition-name` in `aspire-motion.css` (see `app-model`). Then give the first slide a non-fade transition to animate the element between slides.
- Set a running section label with the `header` directive.

Preview the motion with Marp's server mode, `npx @marp-team/marp-cli --server --html --theme-set slides/themes -I slides`, then open `http://localhost:8080/Slides.md`.
