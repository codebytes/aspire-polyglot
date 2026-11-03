# Polyglot Aspire Samples and Development

[Back to the talk](../README.md)

Detailed sample catalog and development notes. Run commands from the repository
root unless a sample's README says otherwise.

## Running the Samples

**Quick Start:** `cd samples/<sample> && aspire run`

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/) 13.6.4,
the .NET 10 SDK, and [Docker](https://docs.docker.com/get-docker/) for backing
services such as Redis and PostgreSQL. Each sample README lists the language
toolchains its workloads need; most workloads run **natively** on those toolchains.

The samples pin the Aspire SDK and integration packages to **13.6.4** (the Go,
Java, and Rust hosting integrations are first-party previews:
`13.6.4-preview.1.26523.11`). Keep every Aspire package on one release. After
changing an AppHost's integrations, run `aspire restore --apphost <entry-point>`
to regenerate its committed `.aspire/modules/` bindings; do not edit generated
bindings by hand.

AppHost launch profiles use dynamically allocated loopback ports for the dashboard,
OTLP collectors, and resource service so multiple samples can run side by side.
Use the URLs printed by Aspire rather than assuming a fixed dashboard port.
The standalone dashboard is the exception: its documented ports are fixed and
bound to IPv4 loopback because the local demo disables authentication.

### Telemetry in every language

Aspire injects the standard `OTEL_*` variables (endpoint, protocol, headers,
service name) plus certificate trust for its development certificate. Each sample
uses its language's ordinary OpenTelemetry setup to read them:

| Language | Setup | Logs |
|----------|-------|------|
| .NET | `ServiceDefaults` (`AddServiceDefaults`) | `ILogger` |
| Python | OpenTelemetry distro: `initialize()` before importing the framework | `logging` |
| Node.js | `NodeSDK` plus explicit instrumentations, preloaded by `npm start` | pino |
| Go | `autoexport` + SDK providers (`telemetry.go`) | `slog` (stdout + OTLP) |
| Java | OpenTelemetry Java agent (`WithOtelAgent`) or the Spring Boot starter | SLF4J / Logback |
| Rust | `tracing` + `tracing-opentelemetry` + `opentelemetry-otlp` | `tracing` events |
| Browser | `WebTracerProvider` with document-load and fetch instrumentation | — |

Every instrumented workload sends **structured logs and traces** to the dashboard;
the smoke suite below checks that for each one.

## Validating Every Sample

Run the runtime smoke suite from the repository root:

```powershell
.\scripts\Test-Samples.ps1
```

This validates all **17 samples**, including the four preview AppHosts and the
standalone Compose stack. On Windows, 15 samples run natively; the Go and Java
preview AppHosts run inside an isolated Linux validation container. No C# fallback
replaces those AppHosts. On Linux/macOS, the wrapper runs all samples natively.
Native AppHosts run with `--isolated`; the Linux previews use their own per-run
Docker daemon, network, and filesystem instead.

The suite waits for resources with `aspire wait`, discovers their assigned URLs
with `aspire describe`, and exercises real HTTP requests, databases, and message
delivery. It then uses `aspire otel logs` and `aspire otel traces` to require
structured logs **and** traces in the dashboard from every instrumented workload.
A failed assertion, startup, or cleanup makes the command exit with a nonzero
status; later samples still run.

Install the prerequisites for the samples being tested: Aspire CLI **13.6.4**,
.NET 10, Docker running Linux containers, Node.js **22.22.3+, 24.15+, or 26+**,
Python 3.12+ with `uv`, Go 1.26+ (or automatic Go toolchain downloads), JDK 25
(the Java AppHost compiles with `--release 25`; JDK 21+ suffices for the Javalin
API), and Rust 1.85+. Maven is not required: the Java samples use their Maven
wrappers. Language packages and container images are restored by Aspire, the
toolchains, and Docker. The first run needs package feed and image-registry access
and can take several minutes per sample.
`--use-system-ca` includes the system certificate store. For native services that
serve HTTPS, the suite also exports the public .NET development certificate into a
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

Aspire 13.6's generated Go and Java clients can hang during Windows named-pipe
authentication. The Go failure also reproduces with `GOARCH=amd64`, so x64
emulation does not fix it. See [microsoft/aspire#20056](https://github.com/microsoft/aspire/issues/20056).
Linux uses Unix sockets instead. Do not hand-patch the generated SDK to hide the
failure.

On Windows, `Test-Samples.ps1` builds the pinned Aspire 13.6.4 Linux tool image
from `scripts/linux-previews.Dockerfile`, with .NET 10, Go 1.27.3, JDK 25, Node 26,
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
| `rust-axum-postgres` | Guestbook page, create/list, the row in PostgreSQL, invalid-entry rejection |
| `java-javalin-redis` | Score submission and Redis leaderboard ordering |
| `hono-redis-urls` | Frontend proxy, short-link redirect, click count, invalid URL rejection |
| `vite-react-api` | Create, read, and delete a Redis-backed TODO through the frontend |
| `dotnet-react-postgres` | Seed data, valid import, transactional invalid-import rejection |
| `dotnet-angular-cosmos` | Seed data, create/read/update/delete, title and ingredient searches through Angular |
| `polyglot-event-stream` | Publish an event; verify Python aggregation, Node readings, and anomaly delivery |
| `preview/flask-markdown-wiki` | Create, render, edit, and invalidate cached Markdown |
| `preview/django-htmx-polls` | CSRF-protected poll creation and voting, the poll in PostgreSQL, the Django tests (including concurrent votes) against PostgreSQL |
| `preview/svelte-go-bookmarks` | Create/search/delete through Svelte; verify the row reached PostgreSQL |
| `preview/spring-boot-postgres` | Create/list/read/delete notes, then verify 404 |
| `standalone-dashboard` | Dashboard HTTP response, four running containers, jobs and telemetry from all three workers via `aspire otel --dashboard-url` |

Every row except `terminal-demo`, `postgres-adminer`, and `standalone-dashboard`
(which checks its workers separately) also requires dashboard logs and traces.

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

Workloads run **natively** on their own toolchains unless marked 🐳. Backing
services (Redis, PostgreSQL, Kafka, Cosmos DB) run as containers.

### C# AppHosts

| Sample | Workloads and how they run | Backing services | Highlights |
|--------|----------------------------|------------------|------------|
| [python-fastapi-docker](../samples/python-fastapi-docker) | Python FastAPI — `AddUvicornApp` | — | Native venv in dev; `aspire publish` generates a Dockerfile + Compose file |
| [go-redis-compose](../samples/go-redis-compose) | Go — `AddGoApp` (`go run .`) | Redis | Native Go in dev; publish generates the Go Dockerfile + Compose file |
| [java-javalin-redis](../samples/java-javalin-redis) | Java (Javalin) — `AddJavaApp` + `WithMavenBuild` + `WithOtelAgent` | Redis | Zero-code telemetry with the OpenTelemetry Java agent |
| [rust-axum-postgres](../samples/rust-axum-postgres) | Rust (Axum) — `AddRustApp` (`cargo run`) | PostgreSQL | `tracing` spans and events as OTLP traces and logs |
| [dotnet-react-postgres](../samples/dotnet-react-postgres) | .NET API — `AddProject`; React — `AddViteApp` | PostgreSQL | Resource commands with arguments and file input, `WithRepl()` |
| [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos) | .NET API — `AddProject`; Angular — `AddJavaScriptApp` | Cosmos DB emulator | `aspire publish` to Azure Container Apps + Cosmos DB Bicep |
| [polyglot-event-stream](../samples/polyglot-event-stream) | .NET producer, Python consumer (`AddPythonApp`), Node.js dashboard (`AddJavaScriptApp`) | Kafka | One event stream across three languages |
| [terminal-demo](../samples/terminal-demo) | Shell + Node.js REPL — `AddExecutable().WithTerminal()` | — | Interactive terminals in the dashboard and CLI |
| [postgres-adminer](../samples/postgres-adminer) | 🐳 Adminer — `AddContainer` | PostgreSQL (`AddContainer`) | Public images only, wired by resource name |

### TypeScript AppHosts

| Sample | Workloads and how they run | Backing services | Highlights |
|--------|----------------------------|------------------|------------|
| [vite-react-api](../samples/vite-react-api) | Python FastAPI — `addUvicornApp`; React — `addViteApp` | Redis | One trace from the browser through FastAPI to Redis |
| [hono-redis-urls](../samples/hono-redis-urls) | Hono — `addNodeApp`; Vite — `addViteApp` | Redis | Node.js runs TypeScript directly; pino logs |
| [ts-starter](../samples/ts-starter) | Express — `addNodeApp`; React — `addViteApp` | — | The official TypeScript starter template |

### Python, Go, and Java AppHosts (preview)

> Experimental guest AppHosts live under [`samples/preview/`](../samples/preview)
> and may change as those languages stabilize.

| Sample | AppHost | Workloads and how they run | Backing services |
|--------|---------|----------------------------|------------------|
| [django-htmx-polls](../samples/preview/django-htmx-polls) | `apphost.py` | Django + HTMX — `add_python_app` | PostgreSQL |
| [flask-markdown-wiki](../samples/preview/flask-markdown-wiki) | `apphost.py` | 🐳 Flask — `add_dockerfile` (the repository's Dockerfile example) | Redis |
| [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) | `apphost.go` | Go API — `AddGoApp`; Svelte — `AddViteApp` | PostgreSQL |
| [spring-boot-postgres](../samples/preview/spring-boot-postgres) | `AppHost.java` | Spring Boot — `addSpringBootApp` | PostgreSQL |

### No AppHost

| Sample | Entry point | Highlights |
|--------|-------------|------------|
| [standalone-dashboard](../samples/standalone-dashboard) | 🐳 `docker-compose.yml` | The dashboard as one container; Node.js, Python, and Go workers send OTLP to it |

### Docker and Compose at a glance

| Approach | Where |
|----------|-------|
| `AddContainer` with public images | Redis/PostgreSQL integrations everywhere, Adminer in [postgres-adminer](../samples/postgres-adminer) |
| `AddDockerfile` | [flask-markdown-wiki](../samples/preview/flask-markdown-wiki) |
| `aspire publish` to Docker Compose (generated Dockerfiles) | [go-redis-compose](../samples/go-redis-compose), [python-fastapi-docker](../samples/python-fastapi-docker) |
| Plain `docker compose`, no AppHost | [standalone-dashboard](../samples/standalone-dashboard) |

## Ways to use multiple languages with Aspire

Aspire is polyglot on **two** independent axes: the language you **author the AppHost in**, and the languages your **workloads** are written in. You mix and match freely — a C# AppHost can orchestrate Python and Go, a TypeScript AppHost can orchestrate .NET, and so on.

### 1. AppHost authoring languages

The orchestrator itself can be written in several languages. C# and TypeScript are generally available; Python, Go, Java, and (since 13.6) Rust are experimental preview (enabled via the `polyglotSupportEnabled` feature — see [`aspire.config.json`](../aspire.config.json)).

| AppHost language | File | Status | Sample(s) |
|------------------|------|--------|-----------|
| **C#** | `apphost.cs` (single-file) or `AppHost.csproj` | GA | [python-fastapi-docker](../samples/python-fastapi-docker), [go-redis-compose](../samples/go-redis-compose), [java-javalin-redis](../samples/java-javalin-redis), [rust-axum-postgres](../samples/rust-axum-postgres), [dotnet-react-postgres](../samples/dotnet-react-postgres), [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos), [polyglot-event-stream](../samples/polyglot-event-stream), [terminal-demo](../samples/terminal-demo), [postgres-adminer](../samples/postgres-adminer) |
| **TypeScript** | `apphost.mts` | GA (13.4) | [vite-react-api](../samples/vite-react-api), [hono-redis-urls](../samples/hono-redis-urls), [ts-starter](../samples/ts-starter) |
| **Python** | `apphost.py` | Preview | [django-htmx-polls](../samples/preview/django-htmx-polls), [flask-markdown-wiki](../samples/preview/flask-markdown-wiki) |
| **Go** | `apphost.go` | Preview | [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) |
| **Java** | `AppHost.java` | Preview | [spring-boot-postgres](../samples/preview/spring-boot-postgres) |
| **Rust** | `apphost.rs` | Experimental (13.6, `experimentalPolyglot:rust` flag) | — |

> The same `.NET` hosting integrations are surfaced to every AppHost language through the **Aspire Type System (ATS)**: the CLI auto-generates a typed SDK into `.aspire/modules/` so guest-language AppHosts call the same integrations without hand-written bindings.

### 2. Adding a workload in any language

Inside the AppHost, each service is added with an `Add*` method. Prefer a typed
integration when one fits; three language-agnostic escape hatches cover **anything else**.

| Mechanism | Adds | Demonstrated in |
|-----------|------|-----------------|
| `AddProject<T>()` / `AddProject(name, path)` | A .NET project | [dotnet-react-postgres](../samples/dotnet-react-postgres), [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos), [polyglot-event-stream](../samples/polyglot-event-stream) |
| `AddUvicornApp()` / `AddPythonApp()` | Python ASGI apps / scripts in an Aspire-managed venv | [python-fastapi-docker](../samples/python-fastapi-docker), [vite-react-api](../samples/vite-react-api), [django-htmx-polls](../samples/preview/django-htmx-polls), [polyglot-event-stream](../samples/polyglot-event-stream) |
| `AddViteApp()` | Vite frontends (React, Vue, Svelte, Angular 17+, Astro) | [dotnet-react-postgres](../samples/dotnet-react-postgres), [vite-react-api](../samples/vite-react-api), [hono-redis-urls](../samples/hono-redis-urls), [ts-starter](../samples/ts-starter), [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) |
| `AddNodeApp()` / `AddJavaScriptApp()` | A Node.js entry file or package script | [hono-redis-urls](../samples/hono-redis-urls), [ts-starter](../samples/ts-starter), [dotnet-angular-cosmos](../samples/dotnet-angular-cosmos), [polyglot-event-stream](../samples/polyglot-event-stream) |
| **`AddGoApp()`** — `Aspire.Hosting.Go` (preview) | A Go module (`go run`, generated Dockerfile on publish) | [go-redis-compose](../samples/go-redis-compose), [svelte-go-bookmarks](../samples/preview/svelte-go-bookmarks) |
| **`AddJavaApp()` / `AddSpringBootApp()` / `AddQuarkusApp()`** — `Aspire.Hosting.Java` (first-party preview in 13.6) | Java apps built with their Maven or Gradle wrapper, or a prebuilt JAR | [java-javalin-redis](../samples/java-javalin-redis), [spring-boot-postgres](../samples/preview/spring-boot-postgres) |
| **`AddRustApp()`** — `Aspire.Hosting.Rust` (first-party preview in 13.6) | A Cargo app (`cargo run`, generated Dockerfile on publish) | [rust-axum-postgres](../samples/rust-axum-postgres) |
| **`AddBunApp()`** / **`AddDenoApp()`** — `Aspire.Hosting.JavaScript` | Bun apps; Deno scripts (experimental, `ASPIREDENO001`) | _discussed — see [Bun](https://aspire.dev/integrations/frameworks/bun-apps/) and [Deno](https://aspire.dev/integrations/frameworks/deno/deno-host/)_ |
| **`AddBlazorWasmProject()` + `AddBlazorGateway()`** (13.4 preview) | Blazor WebAssembly + browser-facing gateway | _discussed — see [Blazor hosting](https://aspire.dev/integrations/dotnet/blazor-hosting/)_ |
| **`AddDockerfile()`** | **Any language** built from a Dockerfile | [flask-markdown-wiki](../samples/preview/flask-markdown-wiki) |
| **`AddContainer()`** | **Any prebuilt public image** | [postgres-adminer](../samples/postgres-adminer) |
| **`AddExecutable()`** | **Any process/CLI** | [terminal-demo](../samples/terminal-demo) (interactive shell + Node.js REPL) |

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
