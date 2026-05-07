---
marp: true
theme: custom-aspire-light
footer: '@Chris_L_Ayers - https://chris-ayers.com'
---

<!-- _footer: 'https://github.com/codebytes/aspire-polyglot' -->
<!-- _class: lead -->

# <!--fit--> Polyglot Aspire

## Orchestrating Any Language with Aspire
## Chris Ayers

![bg right fit](./img/aspire-logo.svg)

---

![bg left:40%](./img/portrait.png)

## Chris Ayers

### Principal Software Engineer<br>Azure CXP AzRel<br>Microsoft

<i class="fa-brands fa-bluesky"></i> BlueSky: [@chris-ayers.com](https://bsky.app/profile/chris-ayers.com)
<i class="fa-brands fa-linkedin"></i> LinkedIn: - [chris\-l\-ayers](https://linkedin.com/in/chris-l-ayers/)
<i class="fa fa-window-maximize"></i> Blog: [https://chris-ayers\.com/](https://chris-ayers.com/)
<i class="fa-brands fa-github"></i> GitHub: [Codebytes](https://github.com/codebytes)
<i class="fa-brands fa-mastodon"></i> Mastodon: [@Chrisayers@hachyderm.io](https://hachyderm.io/@Chrisayers)

---

# The Polyglot Problem

> Modern apps are a **big city without a map** — and for polyglot teams,
> the streets are in five different languages.

Your team doesn't use one language — it uses **five**.

<div class="columns">
<div>

**Your stack today**
- Python ML services
- Go microservices
- Java Spring Boot APIs
- TypeScript/React frontends
- .NET backend APIs

</div>
<div>

**Your orchestration pain**
- Docker Compose: manual port wiring, no built-in telemetry, no health-aware startup ordering
- Each language has its own config format (`.env`, YAML, `application.properties`...)
- No unified observability — good luck tracing a request across 4 services
- 15-step README to run locally — "just `docker-compose up`" never works first time

</div>
</div>

**The question:** How do you orchestrate, observe, and wire all of this **from one place**?

<!-- We've all been there: README says "just run docker-compose up" but it never works the first time. Each language has its own logging, its own config format, its own service discovery pattern. You end up with hardcoded URLs everywhere. -->

---

<!-- _class: gradient -->

# <!--fit--> Aspire: The Polyglot Answer

<p style="color:#ffffff; font-weight:500; max-width:1000px; margin:0.5em auto 0;">Aspire is an agent-ready, code-first tool to compose, debug, and deploy any distributed app.</p>

<!-- Use the official positioning sentence verbatim — it sets up everything that follows. Then transition to the four pillars. -->

---

<!-- _class: compact -->

# The Four Pillars

**These are the four parts of Aspire you'll see today — across every language.**

<div class="columns">
<div>

## 🛠 Aspire CLI
**Your control plane**
`aspire init` · `aspire run` · `aspire deploy` — agent-ready, interactive, the same commands for every stack.

## 🗺 Aspire AppHost
**Your stack in code**
One file declares every service and how they connect — C#, TypeScript, Python, or `aspire.config.json`.

</div>
<div>

## 📊 Aspire Dashboard
**Your app at a glance**
Logs, traces, metrics, and health for every resource — powered by OpenTelemetry, surfaced over an MCP server for agents.

## 🧩 Aspire Integrations
**Building blocks, not black boxes**
**100+** prebuilt packages for databases, caches, queues, AI, and clouds — or bring your own container, CLI, or agent.

</div>
</div>

<!-- The official Spring '26 deck names exactly these four pillars. Every slide in the rest of the talk maps back to one of them — call that out. -->

---

<!-- _class: compact code-compact -->

# One Orchestrator for Every Language

**Aspire gives you three things, regardless of language:**

<div class="columns">
<div>

🎯 **Orchestration**
Define your entire stack — Python, Go, Java, TypeScript, .NET — in one AppHost file

📡 **Service Discovery**
Endpoints and connection strings auto-injected as environment variables

📊 **Observability**
One dashboard for logs, traces, and metrics across ALL services via OpenTelemetry

</div>
<div>

```csharp
var builder = DistributedApplication.CreateBuilder(args);

var redis = builder.AddRedis("cache");
var postgres = builder.AddPostgres("db")
                      .AddDatabase("appdata");

builder.AddUvicornApp("ml-service", "../python", "main:app")
       .WithUv()
       .WithReference(redis);

builder.AddViteApp("frontend", "../react")
       .WithHttpEndpoint(env: "PORT")
       .WithReference(postgres);

builder.AddProject<Projects.Api>("api")
       .WithReference(redis)
       .WithReference(postgres);

builder.Build().Run();
```

</div>
</div>

<!-- This is the Aspire AppHost. It's the central brain that starts everything and wires it together. Python, React, .NET — all visible in one dashboard, auto-wired, observable. No .NET SDK required for non-.NET devs if you use a polyglot AppHost. -->

---

<!-- _class: compact -->

# The Dashboard — One View for Everything

**Same dashboard regardless of what language your services use:**

<div class="columns">
<div>

📋 **Resources** — All services, containers, status, endpoints
📜 **Console Logs** — Real-time stdout/stderr from every process
📊 **Structured Logs** — Parsed JSON logs, filter by level
🔍 **Traces** — Distributed request tracing across services
📈 **Metrics** — Latency, CPU/memory, custom metrics

</div>
<div>

![w:520px](./img/aspire-dashboard.png)

Aspire sets `OTEL_EXPORTER_OTLP_ENDPOINT` automatically — add OpenTelemetry to your service and traces flow to the dashboard.

</div>
</div>

<!-- This is the payoff — show it early. One dashboard for everything, regardless of language. Click a trace to see the full waterfall across Python, .NET, and Node.js. Export as .env for local debugging. -->

---

<!-- _class: dense code-compact -->

# Standalone Dashboard — No AppHost Required

### Already on OTEL? Get the dashboard with zero rewrites.

<div class="columns">
<div>

**The Aspire Dashboard ships as a standalone container.** Point any OTLP-emitting app at it and you get logs, traces, and metrics — no AppHost, no .NET, no commitment.

- ✅ **Node.js / Python / Java / Go / Rust** — anything with an OTEL SDK works.
- ✅ **Same UI** as the AppHost-managed dashboard.
- ✅ **Local-only by default** — OTLP endpoint and dashboard auth keys printed at startup.
- ✅ **Use it in CI**, in a Dockerfile, in `docker-compose.yml`, or attached to a Kubernetes pod.

**Migration path:** start with the standalone dashboard for observability today; adopt the AppHost later when you want service discovery, integrations, and `aspire deploy`.

</div>
<div>

**Run it standalone**
```bash
docker run --rm -it -p 18888:18888 -p 4317:18889 \
  -d --name aspire-dashboard \
  mcr.microsoft.com/dotnet/aspire-dashboard:latest
```

**Point your Node.js app at it**
```javascript
// otel.js
import { NodeSDK } from "@opentelemetry/sdk-node";
import {
  OTLPTraceExporter
} from "@opentelemetry/exporter-trace-otlp-grpc";

const sdk = new NodeSDK({
  traceExporter: new OTLPTraceExporter({
    url: "http://localhost:4317"
  })
});
sdk.start();
```

Open `http://localhost:18888` — your traces show up live.

</div>
</div>

<!-- Per aspire.dev/dashboard/standalone-for-{nodejs,python}/. This slide closes a real adoption gap for polyglot teams: "I'm not on .NET, can I still use any of this?" Yes — start here, no AppHost commitment, just OTEL. Once they like the UI, the AppHost story becomes a much easier sell. -->

---

<!-- _class: compact code-compact -->

# OpenTelemetry Setup by Language

**Add OpenTelemetry once per service — Aspire handles the rest:**

<div class="columns">
<div>

**Python:**
```python
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.grpc \
  .trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export \
  import BatchSpanProcessor

provider = TracerProvider()
provider.add_span_processor(
  BatchSpanProcessor(OTLPSpanExporter(
    endpoint=os.environ.get(
      'OTEL_EXPORTER_OTLP_ENDPOINT')
  ))
)
trace.set_tracer_provider(provider)
```

</div>
<div>

**Node.js:**
```javascript
const { NodeSDK } =
  require('@opentelemetry/sdk-node');
const { getNodeAutoInstrumentations } =
  require('@opentelemetry/auto-instrumentations-node');
const { OTLPTraceExporter } =
  require('@opentelemetry/exporter-trace-otlp-grpc');

const sdk = new NodeSDK({
  traceExporter: new OTLPTraceExporter({
    url: process.env.OTEL_EXPORTER_OTLP_ENDPOINT
  }),
  instrumentations: [getNodeAutoInstrumentations()]
});
sdk.start();
```

</div>
</div>

**Aspire sets `OTEL_EXPORTER_OTLP_ENDPOINT` automatically** — add this boilerplate once per service and traces flow to the dashboard.

<!-- This is how you get your service into that dashboard we just saw. Add the OTel SDK, point at the env var Aspire injects, and you're done. -->

---

<!-- _class: compact -->

# Architecture Overview

**AppHost orchestrates everything — DCP manages processes, Dashboard collects telemetry**

![w:980px center](./img/architecture-overview.drawio.svg)

<!-- The AppHost is the central orchestrator. All services report their telemetry to the unified dashboard. -->

---

<!-- _class: invert -->

# <!--fit--> How It Works

The patterns that make polyglot orchestration possible

<!-- Now let's look at the mechanisms under the hood. -->

---

# Service Discovery

**Aspire injects service endpoints as environment variables:**

```bash
# Pattern: services__<name>__<protocol>__<index>
services__api__http__0=http://localhost:5000
services__frontend__http__0=http://localhost:3000
```

**Read them in any language — same pattern everywhere:**

<div class="columns">
<div>

**Python:**
```python
api_url = os.environ['services__api__http__0']
requests.get(f'{api_url}/data')
```

**Go:**
```go
apiURL := os.Getenv("services__api__http__0")
```

</div>
<div>

**Node.js:**
```javascript
const apiUrl = process.env['services__api__http__0'];
await fetch(`${apiUrl}/data`);
```

**Java:**
```java
String apiUrl = System.getenv("services__api__http__0");
```

</div>
</div>

**No hardcoded URLs. No `.env` files. Aspire wires it.**

<!-- This is the magic. The double underscore __ is used because environment variables can't have colons. Every language can read env vars — that's the universal interface. -->

---

<!-- _class: compact code-compact -->

# Connection Strings

**Infrastructure resources get connection strings automatically:**

```bash
# Pattern: CONNECTIONSTRINGS__<resource>
CONNECTIONSTRINGS__cache=localhost:6379
CONNECTIONSTRINGS__db=Host=localhost;Port=5432;Username=postgres;Password=...
CONNECTIONSTRINGS__messaging=localhost:9092
```

**Same pattern in every language:**

```python
# Python — Redis
client = redis.from_url(f"redis://{os.environ['CONNECTIONSTRINGS__cache']}")
```

```javascript
// Node.js — Kafka
const kafka = new Kafka({ brokers: [process.env.CONNECTIONSTRINGS__messaging] });
```

```java
// Java — PostgreSQL via env vars
String url = "jdbc:postgresql://" + System.getenv("PG_HOST") + ":" + System.getenv("PG_PORT") + "/" + System.getenv("PG_DB");
```

**Aspire injects environment variables** — your service reads them using its language's standard env var mechanism.

<!-- Aspire handles the complexity of connection strings so you don't have to manage .env files. For C# AppHosts using AddPostgres, connection strings are auto-generated. For polyglot AppHosts, you wire env vars explicitly — same result, more control. -->

---

<!-- _class: compact -->

# Resource Lifecycle Management

![w:1080px center](./img/resource-lifecycle-management.drawio.svg)

**Dependency Order:** Infrastructure → Backend Services → Frontend
**Health Monitoring:** `WithHttpHealthCheck("/health")` — automatic restarts on failure
**Graceful Shutdown:** Clean termination of all processes
**Backed by 100+ integrations** — Postgres, Redis, Kafka, Cosmos, OpenAI, Ollama, and your own containers all participate in the same lifecycle.

<!-- Aspire manages startup ordering automatically based on WithReference and WaitFor dependencies. Infrastructure comes up first, then backends, then frontends. Over 100 integrations are wired to the same lifecycle so health/dependency rules apply to everything in the AppHost. -->

---

<!-- _class: compact code-compact -->

# The AppHost — Your Stack in Code

**Write your AppHost in the language your team knows:**

<div class="columns">
<div>

**C# AppHost**
```csharp
var builder = DistributedApplication
    .CreateBuilder(args);

var redis = builder.AddRedis("cache");

builder.AddPythonApp("api", "../api", "app.py")
       .WithReference(redis)
       .WithHttpEndpoint(env: "PORT");

builder.Build().Run();
```

</div>
<div>

**TypeScript AppHost**
```typescript
const builder = await createBuilder();

const redis = await builder.addRedis("cache");

await builder
  .addPythonApp("api", "../api", "app.py")
  .withReference(redis)
  .withHttpEndpoint({ env: "PORT" });

await builder.build().run();
```

</div>
</div>

**Same 40+ integrations** — Redis, Azure, Kafka, MongoDB, PostgreSQL — available in both C# and TypeScript.

<!-- The TypeScript AppHost uses the same integration packages as C#, auto-generated via [AspireExport] attributes. A JS/TS team never needs to touch .NET. -->

---

<!-- _class: dense code-compact -->

# AppHost Languages

**Author your AppHost in C# or TypeScript today. Orchestrate workloads in any language.**

<div class="columns">
<div>

💜 **C# (.NET)** — `AppHost.cs` · **Official**
```csharp
var builder = DistributedApplication
    .CreateBuilder(args);

builder.AddProject<Projects.Api>("api")
       .WithHttpEndpoint(env: "PORT");

builder.Build().Run();
```
Best fit: teams already on .NET tooling.

</div>
<div>

🟦 **TypeScript** — `apphost.ts` · **Official**
```typescript
const builder = await createBuilder();

await builder
  .addJavaScriptApp("api", "./src")
  .withHttpEndpoint({ env: "PORT" });

await builder.build().run();
```
Best fit: Node.js / TypeScript workspaces.

</div>
</div>

**Same model, different syntax.** Both produce the same dashboard, service discovery, health checks, and deployment artifacts. The TypeScript SDK is auto-generated from the same .NET hosting integrations via the **Aspire Type System (ATS)** — there's no separate integration surface to maintain.

**Workloads inside the AppHost** can be written in C#, JavaScript, Python, Go, Java, Rust, PowerShell, and more — via `AddProject`, `AddJavaScriptApp`, `AddPythonApp`, `AddDockerfile`, `AddContainer`, or `AddExecutable`. Pick the AppHost language that fits your repo; your services don't need to match.

<!-- Per aspire.dev/languages-and-runtimes: only C# and TypeScript are documented AppHost authoring languages today. Earlier drafts of this slide listed Python/Java/Go AppHost stubs as "experimental" — those aren't documented on the live site, so this version sticks to the two officially-supported AppHost languages and is honest about workload language support being separate. -->

---

<!-- _class: dense code-compact -->

# Multi-Language Integrations

### Write the integration once. Use it from C# or TypeScript.

<div class="columns">
<div>

**Aspire Type System (ATS)** — the contract that bridges .NET and guest languages.

- Author your hosting integration **in C#** as you always have.
- Annotate exported APIs with ATS attributes — `[AspireExport]`, `[AspireExportType]`, `[AspireExportMethod]`.
- Aspire's analyzer validates the export shape at build time.
- The CLI **auto-generates a TypeScript SDK** into `.modules/` when a TS AppHost runs `aspire add <your-package>`.
- TypeScript callers get fluent, typed methods — same model, different syntax.

**The trade-off:** the guest process talks to the .NET host over a local JSON-RPC socket (Unix socket / named pipe), authenticated with a per-session token. One IPC hop, no port exposure, no duplicated integration code per language.

</div>
<div>

**C# author**
```csharp
[AspireExport]
public static class MyIntegrationExtensions
{
    [AspireExportMethod]
    public static IResourceBuilder<MyResource>
        AddMyService(
            this IDistributedApplicationBuilder builder,
            string name) { ... }
}
```

**TypeScript caller — zero hand-written bindings**
```typescript
import { createBuilder } from "./.modules/aspire.js";
import { addMyService } from
    "./.modules/my-integration.js";

const builder = await createBuilder();
const svc = await addMyService(builder, "svc");
```

*Status: preview feature in 13.x.*

</div>
</div>

<!-- Per aspire.dev/extensibility/multi-language-integration-authoring/. The point of this slide: integration authors don't write a TS binding by hand — the analyzer + ATS scanner generates it. That's how 100+ .NET integrations show up automatically in TypeScript AppHosts. Keep this slide BRIEF — it's a teaser for integration authors, not a tutorial. -->

---

<!-- _class: compact code-compact -->

# Aspire Polyglot Cheat Sheet

<div class="columns">
<div>

**Runtime → Method (C# SDK)**
- Python / ASGI → `AddPythonApp()`
- Python / Uvicorn → `AddUvicornApp()`
- Node.js → `AddNodeApp()`
- Vite / React → `AddViteApp()`
- .NET project → `AddProject<T>()`
- JavaScript → `AddJavaScriptApp()`
- Any Dockerfile → `AddDockerfile()`
- Go → `AddGolangApp()` (Community Toolkit)
- Java / Spring Boot → `AddSpringApp()` (Community Toolkit)
- Any executable → `AddExecutable()`

</div>
<div>

**Common Patterns**
```csharp
.WithReference(redis)
.WaitFor(postgres)
.WithHttpEndpoint(env: "PORT")
.WithExternalHttpEndpoints()
.WithUv()
.WithNpm()
.WithBun()
.WithBuildSecret("key", secret)
.WithRunScript("dev")
.WithHttpHealthCheck("/health")
.WithMcpServer("mcp")
```

</div>
</div>

**Infrastructure:** `AddRedis("name")` · `AddPostgres("name").AddDatabase("db")` · `AddKafka("name")` · `AddAzureCosmosDB("name").RunAsEmulator()`

<!-- Keep this slide handy as a quick reference for everything we just covered! -->

---

<!-- _class: dense code-compact -->

# `aspire.config.json` — One Config for Every Language

<div class="columns">
<div>

**This file tells the CLI which language your AppHost uses:**

```json
{
  "appHost": { "path": "apphost.py", "language": "python" },
  "sdk": { "version": "13.2.0" },
  "channel": "stable",
  "features": { "polyglotSupportEnabled": true },
  "profiles": {
    "default": {
      "applicationUrl": "https://localhost:17000"
    }
  }
}
```

</div>
<div>

**What it does:**
- `appHost.path` + `appHost.language` — declares your stack
- `sdk.version` — pins the Aspire SDK version
- `channel` — release channel (`stable`, `preview`)
- `profiles` — dashboard URLs (replaces `apphost.run.json`)
- Feature flags use **boolean `true`** (not string `"true"`)

</div>
<div>

**Manage from CLI:**
- `aspire config list` / `get` / `set`
- `aspire secret set/list/get/delete`
- `aspire certs clean/trust`

**Every sample in this talk** has one at its root.

</div>
</div>

<!-- This is the key file. Drop aspire.config.json in your project root, point it at your AppHost file, set the language, and aspire run just works. The CLI reads this to know how to build and launch your app. -->

---

<!-- _class: compact code-compact -->

# Getting Started

**No .NET SDK required** — [get.aspire.dev](http://get.aspire.dev)

<div class="columns">
<div>

**Install & scaffold**
```bash
curl -sSL https://aspire.dev/install.sh | bash
aspire new aspire-py-starter -n my-app
aspire new aspire-ts-starter -n my-app
aspire new aspire-starter -n my-app
cd my-app && aspire run
```

</div>
<div>

**Day-to-day commands**
```bash
aspire run / aspire start    # Run / background
aspire ps / aspire stop      # List / stop
aspire describe --follow     # Watch resources
aspire doctor                # Environment check
aspire otel / aspire logs    # Telemetry & logs
aspire export                # Capture to zip
```

</div>
</div>

<!-- aspire doctor is great before a talk. aspire start runs in detached mode. aspire new is language-aware — it scaffolds the right AppHost structure for your chosen language. -->

---

<!-- _class: dense code-compact -->

# Agent-Ready CLI

### Two MCP servers. One model. Any agent.

<div class="columns">
<div>

**MCP support out of the box — no plugins, no glue.**

- 🛠 **CLI MCP** — stdio. Agent spawns `aspire agent mcp` as a subprocess. Set up by `aspire agent init`.
- 📊 **Dashboard MCP** — streamable HTTP + API key. Click the **MCP** button in the dashboard top-right.
- 🧠 **Tools agents get**: `list_resources`, `list_console_logs`, `list_traces`, `execute_resource_command`, `search_docs`, `doctor`.
- 🔌 **Clients**: VS Code, Claude Code, Copilot CLI, OpenCode — any MCP-aware client works.

**Polyglot bonus:** the agent sees Python tracebacks, Go panics, Java stack traces, and Node errors through the same OTEL pipeline.

</div>
<div>

```bash
# 1. One-time setup in your AppHost dir
$ aspire agent init
  ◻ Aspire skill file       (recommended)
  ◻ Aspire MCP server
  ◻ Playwright CLI

# 2. Start your stack, open your agent
$ aspire run
$ claude    # reads .mcp.json
$ code .    # reads .vscode/mcp.json
```

```jsonc
// .vscode/mcp.json (auto-generated)
{ "servers": { "aspire": {
    "type": "stdio",
    "command": "aspire",
    "args": ["agent", "mcp"]
} } }
```

</div>
</div>

<!-- Per aspire.dev/get-started/aspire-mcp-server/ and /get-started/ai-coding-agents/. CLI MCP = stdio + `aspire agent init`. Dashboard MCP = streamable HTTP + API key from the dashboard UI. Earlier drafts invented an `http://localhost:15889/mcp` URL — doesn't exist for the CLI server. -->

---

<!-- _class: purple -->

# <!--fit--> Demos

8 samples — live with the Aspire dashboard

<!-- Time to see Aspire in action! -->

---

<!-- _class: compact -->

# 8 Live Demos

<div class="columns">
<div>

**Simple → Full-stack**
1. **ts-starter** — Express + React (TS AppHost)
2. **flask-markdown-wiki** — Flask + Redis (Python AppHost)
3. **vite-react-api** — FastAPI + React + Redis (TS AppHost)
4. **django-htmx-polls** — Django + HTMX + PostgreSQL (Python AppHost)

</div>
<div>

**Multi-runtime → Polyglot**
5. **spring-boot-postgres** — Spring Boot + PostgreSQL (Java AppHost)
6. **svelte-go-bookmarks** — Go API + Svelte + PostgreSQL (Go AppHost)
7. **dotnet-angular-cosmos** — Angular + .NET + CosmosDB (C# AppHost)
8. **polyglot-event-stream** — .NET + Python + Node.js + Kafka (C# AppHost)

</div>
</div>

<!-- Ordered by escalating complexity — same orchestration model, increasing sophistication. -->

---

<!-- _class: compact -->

# Demo: TypeScript Starter

**TypeScript AppHost** → Express API + React Frontend

![w:780px center](./img/ts-starter-architecture.drawio.svg)

<!-- The simplest polyglot demo. TypeScript AppHost with auto-wired API and frontend. -->

---

<!-- _class: compact -->

# Demo: Flask Markdown Wiki

**Python AppHost** → Flask Wiki App + Redis Cache

![w:780px center](./img/flask-markdown-wiki.drawio.svg)

<!-- Python orchestrating Python — the AppHost and the service are both Python. -->

---

<!-- _class: compact -->

# Demo: Vite React + FastAPI

**TypeScript AppHost** → React Frontend + Python FastAPI + Redis

![w:780px center](./img/vite-react-api-architecture.drawio.svg)

<!-- Full-stack TypeScript-orchestrated app with Python backend and Redis caching. -->

---

<!-- _class: compact -->

# Demo: Django HTMX Polls

**Python AppHost** → Django + HTMX + PostgreSQL

![w:780px center](./img/django-htmx-voting-polls.drawio.svg)

<!-- Real-time voting with HTMX partial updates, Django backend, PostgreSQL persistence. -->

---

<!-- _class: compact -->

# Demo: Spring Boot Notes

**Java AppHost** → Spring Boot API + PostgreSQL

![w:780px center](./img/spring-boot-postgres.drawio.svg)

<!-- Java orchestrating Java — experimental Java AppHost with Spring Boot and PostgreSQL. -->

---

<!-- _class: compact -->

# Demo: Svelte + Go Bookmarks

**Go AppHost** → Go REST API + Svelte Frontend + PostgreSQL

![w:780px center](./img/go-svelte-bookmarks.drawio.svg)

<!-- Go orchestrating a full-stack app — Go API backend with Svelte frontend. -->

---

<!-- _class: compact -->

# Demo: Angular + .NET + CosmosDB

**C# AppHost** → Angular Frontend + .NET API + Azure CosmosDB

![w:780px center](./img/dotnet-angular-cosmos.drawio.svg)

<!-- Classic .NET AppHost orchestrating Angular frontend with CosmosDB emulator. -->

---

<!-- _class: compact -->

# Demo: Polyglot Event Stream

**C# AppHost** → .NET Producer + Python Consumer + Node.js Dashboard + Kafka

![w:780px center](./img/event-stream-architecture.drawio.svg)

<!-- The ultimate polyglot demo — three languages, one event pipeline, full distributed tracing. -->

---

<!-- _class: compact code-compact -->

# From Dev to Production

**Same AppHost model** for local, staging, and production — one arc, two commands.

<div class="columns">
<div>

**`aspire run` (local)**
```text
✅ redis (cache)         healthy   :6379
✅ postgres (db)         healthy   :5432
✅ ml-service (python)   running   :8000
✅ frontend (vite)       running   :5173
✅ api (.net)            running   :8080

Dashboard: http://localhost:15888
```

**`aspire deploy` (target)**
```text
→ Building images for python, node, .net
→ Pushing to acrcdbytes.azurecr.io
→ Provisioning Container Apps environment
→ Wiring Postgres + Redis connection strings
✅ Deployed to dev environment in 4m 12s
```

</div>
<div>

**Same model, two commands:**
```bash
aspire run       # Local development
aspire deploy    # Deploy to target (Preview)
aspire publish   # Generate artifacts (Preview)
aspire do        # Pipeline step (Preview)
```

**What Aspire generates from your AppHost:**
- Container images for all languages
- Azure Container Apps / Kubernetes manifests
- Infrastructure wiring (Redis, Postgres, Kafka…)
- Service connections + environment variables

**No separate deploy config.** The AppHost is the contract.

</div>
</div>

<!-- The pivot here is `aspire run → aspire deploy`: same code-first AppHost drives both. The polyglot point: this works whether your services are Python, Go, Java, TypeScript, .NET — you don't need per-language deploy plumbing. -->

---

<!-- _class: gradient -->

# <!--fit--> Wrap-Up

---

# Key Takeaways

<br>

🎯 **One orchestrator for every language** — Define your entire stack in one AppHost file, regardless of runtime

<br>

📊 **Unified observability out of the box** — One dashboard for logs, traces, and metrics across all services via OpenTelemetry

<br>

🚀 **From local dev to production** — Same model, same CLI, same config — `aspire run` to `aspire deploy`

<!-- If your team uses multiple languages, Aspire gives you a single place to define, run, observe, and deploy your entire stack. -->

---

# Resources

<div class="columns">
<div>

## Links

- 🌐 [aspire.dev](https://aspire.dev) — Official website & docs
- 🐙 [github.com/dotnet/aspire](https://github.com/dotnet/aspire) — Source code
- 🐙 [github.com/codebytes/aspire-polyglot](https://github.com/codebytes/aspire-polyglot) — This repo!
- 🛒 [github.com/dotnet/eShop](https://github.com/dotnet/eShop) — eShop sample
- 🧰 [Aspire Community Toolkit](https://github.com/CommunityToolkit/Aspire)
- 💬 [Discord: Aspire channel](https://aka.ms/dotnet-discord)

</div>
<div>

## Follow Chris Ayers

![w:400px](./img/chris_ayers.svg)

</div>
</div>

---

# Questions?

![bg right](./img/owl.png)
