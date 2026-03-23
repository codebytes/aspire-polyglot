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

# <!--fit--> Aspire: The Polyglot Answer

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

<!-- _class: compact code-compact -->

# Five AppHost Languages

**Pick the language your team already knows:**

<div class="columns">
<div>

🐍 **Python** — `apphost.py`
```python
api = builder.add_dockerfile("api", "./src")
api.with_http_endpoint(target_port=8080, env="PORT")
```

🟦 **TypeScript** — `apphost.ts`
```typescript
await builder.addNodeApp("api", "./src", "server.js")
  .withHttpEndpoint({ env: "PORT" });
```

🟢 **Go** — `apphost.go`
```go
api, _ := builder.AddDockerfile("api", "./src", nil, nil)
api.WithHttpEndpoint(nil, float64Ptr(8080), stringPtr("http"), nil, nil)
```

</div>
<div>

☕ **Java** — `AppHost.java`
```java
builder.addDockerfile("api", "./src", null, null)
  .withExternalHttpEndpoints();
```

💜 **C# (.NET)** — `AppHost.cs`
```csharp
builder.AddProject<Projects.Api>("api")
       .WithHttpEndpoint(env: "PORT");
```

**All five produce the same result:**
Dashboard, service discovery, and telemetry work **identically**. C# has the richest typed integrations; other SDKs use `addDockerfile`/`addContainer` as universal building blocks.

</div>
</div>

<!-- Each language has its own idioms — Python uses snake_case, TypeScript uses camelCase, Go returns errors — but the Aspire model is the same everywhere. The C# SDK has the most typed integrations, but all five languages can orchestrate any service via Dockerfile or container. -->

---

<!-- _class: compact code-compact -->

# `aspire.config.json` — One Config for Every Language

**This file tells the CLI which language your AppHost uses:**

```json
{
  "appHost": { "path": "apphost.py", "language": "python" },
  "sdk": { "version": "13.2.0" },
  "features": { "polyglotSupportEnabled": true }
}
```

<div class="columns">
<div>

**What it does:**
- `appHost.path` + `appHost.language` — declares your stack
- `sdk.version` — pins the Aspire SDK version (e.g. `13.2.0`)
- Feature flags use **boolean `true`** (not string `"true"`)
- Replaces old `.aspire/settings.json` split config

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

# <!--fit--> How It Works

The three patterns that make polyglot orchestration possible

<!-- Now let's look at the three mechanisms that power everything: service discovery, connection strings, and the dashboard. These work the same regardless of AppHost language. -->

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

```properties
# Java Spring Boot — via explicit env vars from AppHost
spring.datasource.url=jdbc:postgresql://${PG_HOST:localhost}:${PG_PORT:5432}/${PG_DB:notesdb}
spring.datasource.username=${PG_USER:postgres}
```

**Aspire injects environment variables** — your service reads them using its language's standard env var mechanism.

<!-- Aspire handles the complexity of connection strings so you don't have to manage .env files. For C# AppHosts using AddPostgres, connection strings are auto-generated. For polyglot AppHosts, you wire env vars explicitly — same result, more control. -->

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

**Cross-language tracing example:**
```
Trace ID: abc123...
└─ POST /api/process (Python, 245ms)
   ├─ POST /data (.NET, 200ms)
   │  ├─ SQL SELECT (PostgreSQL, 50ms)
   │  └─ POST /notify (Node.js, 100ms)
   │     └─ Redis SET (25ms)
   └─ Render response (40ms)
```

Aspire sets `OTEL_EXPORTER_OTLP_ENDPOINT` automatically — add OpenTelemetry to your service and traces flow to the dashboard.

</div>
</div>

<!-- This is the killer feature. One dashboard for everything, regardless of language. Click a trace to see the full waterfall across Python, .NET, and Node.js. Export as .env for local debugging. -->

---

<!-- _class: compact code-compact -->

# CLI for Every Language

**No .NET SDK required** — the Aspire CLI is a standalone binary.

```bash
# Install
winget install Microsoft.Aspire.Cli          # Windows
curl -sSL https://aspire.dev/install.sh | bash  # macOS / Linux
```

<div class="columns">
<div>

**Day-to-day workflow**
```bash
aspire run              # Start everything
aspire run --detach     # Background mode
aspire ps               # List running apps
aspire stop             # Stop everything
aspire describe --follow  # Watch resources live
```

</div>
<div>

**Diagnostics & maintenance**
```bash
aspire doctor           # Check SDK, certs, Docker, WSL2
aspire restore          # Regenerate SDK code
aspire export           # Capture telemetry to zip
aspire update --self    # Upgrade CLI
aspire docs search "redis"  # Docs from terminal
```

</div>
</div>

**Works identically** whether your AppHost is Python, TypeScript, Go, Java, or C#.

<!-- The standalone CLI bundles DCP — the Developer Control Plane. A Python or Java developer uses the exact same commands as a .NET developer. aspire doctor is great for troubleshooting environment issues before a talk or demo. -->

---

<!-- _class: compact code-compact -->

# `aspire new` — Language-Aware Scaffolding

**Start a new project in your team's language:**

```bash
aspire new aspire-ts-starter -n my-app    # TypeScript AppHost
aspire new aspire-py-starter -n my-app    # Python AppHost
aspire new aspire-starter -n my-app       # C# AppHost (classic)
```

```bash
cd my-app && aspire run   # Runs immediately — no extra setup
```

**What you get:**
- AppHost file in your chosen language
- `aspire.config.json` pre-configured
- Sample service wired up with service discovery
- Dashboard ready on first run

<!-- aspire new is language-aware — it scaffolds the right AppHost structure for your chosen language. This is the fastest way to start a polyglot project. -->

---

# <!--fit--> Demos — The Samples

8 polyglot samples across 5 AppHost languages, simple → complex

<!-- Time to see Aspire in action! We'll walk through 8 samples — each running live with the Aspire dashboard. Watch for: service startup ordering, cross-language logs, and the unified trace view. -->

---

<!-- _class: compact code-compact -->

# Demo 1: ts-starter — TypeScript AppHost Template

**AppHost language:** TypeScript · **Stack:** Express API + Vite/React frontend

The official `aspire-ts-starter` template — zero .NET required.

```typescript
const builder = await createBuilder();

const app = await builder
  .addNodeApp("app", "./api", "src/index.ts")
  .withHttpEndpoint({ env: "PORT" })
  .withExternalHttpEndpoints();

const frontend = await builder
  .addViteApp("frontend", "./frontend")
  .withReference(app)
  .waitFor(app);

await builder.build().run();
```

```bash
aspire new aspire-ts-starter -n my-app && cd my-app && aspire run
```

**Polyglot highlight:** A JavaScript/TypeScript team can adopt Aspire without learning C# or installing .NET. Full dashboard, service discovery, and hot reload from day one.

<!-- This is the fastest on-ramp for JS/TS teams. aspire new gives you a working project with dashboard in under a minute. Note: Aspire 13.2 improved JS/TS AppHost debugging — the VS Code extension now includes an Activity Bar panel for managing Aspire apps and auto-detects your AppHost language. Great for stepping through TypeScript orchestration code. -->

---

<!-- _class: compact code-compact -->

# Demo 2: flask-markdown-wiki — Python AppHost

**AppHost language:** Python · **Stack:** Flask (Dockerfile) + Redis container

```python
cache = builder.add_container("cache", "redis:latest")

wiki = builder.add_dockerfile("wiki", "./src")
wiki.with_external_http_endpoints()
wiki.with_http_endpoint(target_port=8080, env="PORT")
```

```bash
cd samples/flask-markdown-wiki && aspire run
```

**Polyglot highlight:** Python AppHost uses `add_container` and `add_dockerfile` — the universal building blocks. Redis, Flask, PostgreSQL — anything with a Docker image works.

<!-- This demo shows the Python SDK's current approach: add_container for infrastructure, add_dockerfile for services. The Python SDK uses snake_case — it feels native to Python developers. Even with lower-level primitives, you get full dashboard, logs, and health monitoring. -->

---

<!-- _class: compact code-compact -->

# Demo 3: django-htmx-polls — Python AppHost

**AppHost language:** Python · **Stack:** Django + HTMX + PostgreSQL

![w:500px center](./img/django-htmx-voting-polls.drawio.svg)

```python
postgres = builder.add_container("pg", "postgres:latest")
postgres.with_environment("POSTGRES_DB", "pollsdb")
postgres.with_environment("POSTGRES_PASSWORD", "postgres")

polls = builder.add_dockerfile("polls", "./src")
polls.with_external_http_endpoints()
polls.with_http_endpoint(target_port=8080, env="PORT")
```

**Polyglot highlight:** Aspire manages the PostgreSQL container lifecycle — spins it up, injects environment variables, and Django connects on startup.

<!-- Django with HTMX shows modern server-rendered patterns. PostgreSQL runs as an Aspire-managed container — no manual docker run needed. The Dockerfile approach means any Python framework works the same way. -->

---

<!-- _class: compact code-compact -->

# Demo 4: vite-react-api — TypeScript AppHost

**AppHost language:** TypeScript · **Stack:** FastAPI (Dockerfile) + Vite/React (Dockerfile) + Redis

```typescript
const builder = await createBuilder();

const cache = builder.addContainer("cache", "redis:latest");

const api = builder.addDockerfile("api", "./src/api")
  .withHttpEndpoint({ targetPort: 8080, env: "PORT" })
  .withExternalHttpEndpoints();

const web = builder.addDockerfile("web", "./src/web")
  .withHttpEndpoint({ targetPort: 5173, env: "PORT" })
  .withExternalHttpEndpoints();

await builder.build().run();
```

**Polyglot highlight:** TypeScript AppHost orchestrating a **Python** backend + **React** frontend + **Redis** — all via Dockerfiles. Three ecosystems, one TypeScript file.

<!-- This sample shows the Dockerfile-based approach: any service with a Dockerfile can be orchestrated. The TypeScript SDK's addDockerfile and addContainer are universal escape hatches. -->

---

<!-- _class: compact code-compact -->

# Demo 5: spring-boot-postgres — Java AppHost

**AppHost language:** Java · **Stack:** Spring Boot (Dockerfile) + PostgreSQL container

```java
var pg = builder.addContainer("pg", "postgres:16");
pg.withEnvironment("POSTGRES_USER", "postgres");
pg.withEnvironment("POSTGRES_PASSWORD", "postgres");
pg.withEnvironment("POSTGRES_DB", "notesdb");

var api = builder.addDockerfile("api", "./src", null, null);
api.withExternalHttpEndpoints();
```

**Polyglot highlight:** Java teams write their AppHost in Java. `addDockerfile` builds and runs the Spring Boot container; environment variables wire PostgreSQL credentials.

```bash
cd samples/spring-boot-postgres && aspire run
```

<!-- Spring Boot is the dominant Java framework. The Java SDK uses addContainer for infrastructure and addDockerfile for services — same low-level primitives as Go and Python. Aspire handles the container lifecycle. -->

---

<!-- _class: compact code-compact -->

# Demo 6: svelte-go-bookmarks — Go AppHost

**AppHost language:** Go · **Stack:** Go API (Dockerfile) + Svelte frontend + PostgreSQL

![w:500px center](./img/go-svelte-bookmarks.drawio.svg)

```go
pg, _ := builder.AddContainer("pg", "postgres:16")
pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")

api, _ := builder.AddDockerfile("api", "./go-api", nil, nil)
pgEndpoint, _ := pg.GetEndpoint("tcp")
api.WithEnvironmentEndpoint("PG_HOST", pgEndpoint)
api.WithExternalHttpEndpoints()

frontend, _ := builder.AddExecutable("frontend", "npm",
    "./frontend", []string{"run", "dev"})
apiEndpoint, _ := api.GetEndpoint("http")
frontend.WithEnvironmentEndpoint("services__api__http__0", apiEndpoint)
```

**Polyglot highlight:** Go AppHost uses `AddExecutable` for npm and `AddDockerfile` for the Go API — Go's explicit error-handling style works naturally.

<!-- Go's (_, err) pattern works naturally with the Aspire Go SDK. AddDockerfile is the escape hatch for any containerized service. AddExecutable runs npm directly without Docker. -->

---

<!-- _class: compact code-compact -->

# Demo 7: dotnet-angular-cosmos — .NET AppHost

**AppHost language:** C# · **Stack:** ASP.NET Core + Angular + CosmosDB Emulator

```csharp
var cosmos = builder.AddAzureCosmosDB("cosmos").RunAsEmulator();
var db = cosmos.AddCosmosDatabase("recipesdb");

var api = builder.AddProject<Projects.Api>("api").WithReference(db);
builder.AddJavaScriptApp("frontend", "../frontend", "start")
       .WithReference(api)
       .WithHttpEndpoint(env: "PORT");
```

**Polyglot highlight:** The traditional .NET AppHost — but it's still polyglot because it orchestrates an **Angular** frontend via `AddJavaScriptApp`. CosmosDB emulator runs locally via Docker.

```bash
cd samples/dotnet-angular-cosmos && aspire run
```

<!-- This shows the classic Aspire pattern — C# AppHost with .NET project. But even here, Angular is orchestrated as a polyglot service. -->

---

<!-- _class: compact code-compact -->

# Demo 8: polyglot-event-stream — The Grand Finale

**AppHost language:** C# · **Stack:** .NET + Python + Node.js + Kafka

![w:700px center](./img/event-stream-architecture.drawio.svg)

```csharp
var kafka = builder.AddKafka("messaging").WithKafkaUI();

builder.AddProject<Projects.EventProducer>("producer")
       .WithReference(kafka);

builder.AddPythonApp("consumer", "../python-consumer", "main.py")
       .WithReference(kafka);

builder.AddJavaScriptApp("dashboard", "../node-dashboard", "start")
       .WithReference(kafka).WithHttpEndpoint(env: "PORT");
```

**Polyglot highlight:** Three languages sharing Kafka via `CONNECTIONSTRINGS__messaging`. End-to-end traces flow across .NET → Kafka → Python → Node.js in one dashboard.

<!-- This is the payoff. Three languages, one event bus, one dashboard. The AppHost is 10 lines and orchestrates the entire system. -->

---

# <!--fit--> Wrap-Up

---

# Key Takeaways

🌍 **Aspire is a polyglot orchestrator** — not just for .NET

🗣️ **Five AppHost languages** — C#, TypeScript, Python, Go, Java — pick yours

📡 **Universal patterns** — `services__name__http__0` and `CONNECTIONSTRINGS__resource` work in every language

📊 **One dashboard** — Logs, traces, metrics across all services via OpenTelemetry

🛠️ **Standalone CLI** — `aspire run` / `aspire doctor` / `aspire new` — no .NET SDK required

📦 **40+ integrations** — Redis, Kafka, PostgreSQL, CosmosDB — richest in C#, growing in all SDKs

🚀 **Deploy anywhere** — Same AppHost model for local dev, staging, and production

🔓 **Open source** — [github.com/dotnet/aspire](https://github.com/dotnet/aspire) — MIT license

<!-- The key message: if your team uses multiple languages, Aspire gives you a single place to define, run, observe, and deploy your entire stack. The AppHost language is a choice, not a constraint. -->

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

---

# <!--fit--> Appendix

---

<!-- _class: compact -->

# Architecture Overview

![w:980px center](./img/architecture-overview.drawio.svg)

<!-- The AppHost is the central orchestrator. All services report their telemetry to the unified dashboard. -->

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
.WithHttpHealthCheck("/health")
.WithMcpServer("mcp")
```

</div>
</div>

**Infrastructure:** `AddRedis("name")` · `AddPostgres("name").AddDatabase("db")` · `AddKafka("name")` · `AddAzureCosmosDB("name").RunAsEmulator()`

<!-- Keep this slide handy as a quick reference! -->

---

<!-- _class: compact code-compact -->

# OpenTelemetry Setup by Language

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
const { NodeTracerProvider } =
  require('@opentelemetry/sdk-trace-node');
const { OTLPTraceExporter } =
  require('@opentelemetry/exporter-trace-otlp-grpc');
const { BatchSpanProcessor } =
  require('@opentelemetry/sdk-trace-base');

const provider = new NodeTracerProvider();
provider.addSpanProcessor(
  new BatchSpanProcessor(new OTLPTraceExporter({
    url: process.env.OTEL_EXPORTER_OTLP_ENDPOINT
  }))
);
provider.register();
```

</div>
</div>

**Aspire sets `OTEL_EXPORTER_OTLP_ENDPOINT` automatically** — add this boilerplate once per service and traces flow to the dashboard.

---

<!-- _class: compact -->

# Resource Lifecycle Management

![w:1080px center](./img/resource-lifecycle-management.drawio.svg)

**Dependency Order:** Infrastructure → Backend Services → Frontend
**Health Monitoring:** `WithHttpHealthCheck("/health")` — automatic restarts on failure
**Graceful Shutdown:** Clean termination of all processes

---

<!-- _class: compact code-compact -->

## Deploying Polyglot Aspire Apps

```bash
aspire run       # Local development
aspire deploy    # Deploy to Azure Container Apps
aspire publish   # Generate deployment artifacts
```

**What Aspire generates:**
- Container images for Python, Node.js, .NET, Go, Java
- Azure Container Apps / Kubernetes manifests
- Infrastructure wiring for Redis, Postgres, CosmosDB, Kafka
- Service connections + environment variables

**Same AppHost model** for local, staging, and production.

<!--
============================================================
  DEMO SLIDES (commented out — uncomment to include in deck)
============================================================

---

# <!~~fit~~> Live Demo Walkthroughs

8 real-world polyglot samples

<!~~ Time to see Aspire in action! We'll walk through 8 samples that showcase different AppHost languages and patterns. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo: ts-starter — TypeScript AppHost Template

**AppHost language:** TypeScript · **Stack:** Express API + React frontend

The official `aspire-ts-starter` template — zero .NET required.

```typescript
const builder = await createBuilder();

await builder
  .addNodeApp("api", "../api", "server.js")
  .withNpm()
  .withHttpEndpoint({ env: "PORT" });

await builder
  .addNpmApp("web", "../web", "dev")
  .withHttpEndpoint({ env: "PORT" })
  .withExternalHttpEndpoints();

await builder.build().run();
```

```bash
aspire new aspire-ts-starter -n my-app && cd my-app && aspire run
```

**Polyglot highlight:** A JavaScript/TypeScript team can adopt Aspire without learning C# or installing .NET.

<!~~ This is the fastest on-ramp for JS/TS teams. aspire new gives you a working project with dashboard in under a minute. ~~>

---

# Demo 1: Python Web App — Markdown Wiki

**What:** Flask web app with SQLite database + **Redis cache**

**Use Case:** Python web services, simple CRUD apps, Markdown rendering with caching

**Key Code (apphost.py):**
```python
cache = builder.add_redis("cache")

builder.add_python_app("wiki", "./src", "main.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints() \
    .with_reference(cache)
```

**That's it!**

**Run it:**
```bash
cd samples/flask-markdown-wiki
aspire run
```

**Dashboard:** Shows Flask app + Redis cache with clickable link to `http://localhost:5000`

<!~~ This demo shows Python web app + Redis caching pattern with Aspire. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 1: Flask Wiki — What You See

**Dashboard view**
- `wiki` resource with a clickable local URL
- `cache` (Redis) resource showing connection status
- Flask console logs + health status
- Request history for each page load

**Flask app (`app.py`):**
```python
app = Flask(__name__)

@app.route('/')
def index():
    return render_template_string(wiki_template, pages=pages)

@app.route('/page/<title>')
def view_page(title):
    return render_page(title)
```

**Pattern:** Python web app + SQLite + Redis cache, ideal for high-traffic internal tools.

<!~~ Flask + SQLite + Redis caching is a great pattern for performance-critical CRUD apps. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 2: Django + HTMX — Voting Polls

**What:** Django web app with HTMX for server-rendered interactivity + **PostgreSQL**

**Use Case:** Python web frameworks, server-rendered UIs, progressive enhancement

**Architecture:**
![w:860px center](./img/django-htmx-voting-polls.drawio.svg)

**apphost.py:**
```python
postgres = builder.add_postgres("pg")
db = postgres.add_database("pollsdb")

builder.add_python_app("polls", "./src", "run.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints() \
    .with_reference(db)
```

<!~~ Django with HTMX + PostgreSQL shows modern server-rendered patterns without heavy frontend frameworks. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 2: Django HTMX — Run It

**Command**
```bash
cd samples/django-htmx-polls
aspire run
```

**What Aspire does**
- Installs Python deps
- Runs migrations
- Starts Django on `:8000`
- Exposes a clickable dashboard link

**HTMX view logic**
```python
def vote(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    question.votes += 1
    question.save()
    template = 'polls/_results.html' if request.htmx else 'polls/results.html'
    return render(request, template, {'question': question})
```

<!~~ HTMX lets you build interactive UIs with server-side rendering, no React needed! ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 3: Vite + React + FastAPI — TODO App

**Stack**
- Vite + React frontend
- FastAPI backend
- Redis cache

**apphost.ts (TypeScript AppHost)**
```typescript
const builder = await createBuilder();
const cache = await builder.addRedis("cache");
const api = await builder.addPythonApp("api", "./src/api", "main.py")
  .withReference(cache);
await builder.addNpmApp("web", "./src/web", "dev")
  .withReference(api);
await builder.build().run();
```

**Pattern:** TypeScript AppHost orchestrating Python + React + Redis.

<!~~ This sample was converted from Python to TypeScript AppHost. The AppHost language is independent of service languages. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 3: FastAPI + Redis — Code

```python
app = FastAPI()
redis_conn = os.environ['CONNECTIONSTRINGS__cache']
cache = redis.from_url(redis_conn)

@app.get('/api/todos')
async def get_todos():
    if cached := cache.get('todos'):
        return json.loads(cached)
    todos = [{"id": 1, "title": "Learn Aspire", "done": False}]
    cache.setex('todos', 60, json.dumps(todos))
    return todos
```

---

<!~~ _class: compact code-compact ~~>

# Demo 3: React Client — Code

```typescript
const apiUrl = import.meta.env.VITE_services__api__http__0;

useEffect(() => {
  fetch(`${apiUrl}/api/todos`)
    .then(res => res.json())
    .then(setTodos);
}, []);
```

**Takeaway:** the frontend reads the Aspire-injected API URL from Vite env.

<!~~ Vite + React with Python backend is a popular stack. Aspire makes it seamless! ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 4: Spring Boot + PostgreSQL — Notes App

**Stack**
- Spring Boot REST API
- PostgreSQL database
- pgAdmin UI

**AppHost.java**
```java
var pg = builder.addPostgres("pg", null, null);
var db = pg.addDatabase("notesdb");

builder.addDockerfile("api", "./src")
  .withReference(db)
  .withHttpEndpoint(8080, "PORT")
  .withExternalHttpEndpoints();
```

**Pattern:** use `AddDockerfile` when the service already has a container build.

<!~~ Spring Boot is the dominant Java framework. AddDockerfile lets Aspire run it! ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 4: Connection String Handling

**Aspire auto-generates JDBC URLs** for polyglot apps — no parsing needed:

```properties
spring.datasource.url=${NOTESDB_JDBCCONNECTIONSTRING:jdbc:postgresql://localhost:5432/notesdb}
spring.datasource.username=${NOTESDB_USERNAME:postgres}
spring.datasource.password=${NOTESDB_PASSWORD:}
```

**How it works:**
- `NOTESDB_JDBCCONNECTIONSTRING` — Aspire injects a proper JDBC URL (`jdbc:postgresql://host:port/notesdb`)
- `NOTESDB_USERNAME` / `NOTESDB_PASSWORD` — credentials injected as separate env vars
- Fallback defaults enable standalone `./mvnw spring-boot:run` without Aspire
- Named after the `addDatabase("notesdb")` reference in AppHost

<!~~ Aspire auto-generates JDBC URLs — Spring devs just point at the env var. No parsing needed. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 4: Spring Boot API — Code

```java
@RestController
class NotesController {
  @GetMapping("/api/notes")
  List<Note> getNotes(JdbcTemplate db) {
    return db.query(
      "SELECT id, title, content FROM notes",
      (rs, _) -> new Note(rs.getLong("id"), rs.getString("title"), rs.getString("content"))
    );
  }
}
```

---

<!~~ _class: compact code-compact ~~>

# Demo 4: Dockerfile — Code

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

**Aspire injects** the datasource settings at runtime.

<!~~ Multi-stage Docker builds keep production images small. Aspire handles Dockerfile orchestration! ~~>

---
<!~~ _class: compact dense code-compact ~~>

# Demo 5: .NET + Angular + CosmosDB

**Stack**
- ASP.NET Core API
- Angular SPA
- Cosmos DB Emulator

**AppHost**
```csharp
var cosmos = builder.AddAzureCosmosDB("cosmos").RunAsEmulator();
var db = cosmos.AddCosmosDatabase("recipesdb");

var api = builder.AddProject<Projects.Api>("api").WithReference(db);
builder.AddNpmApp("frontend", "../frontend", "start")
       .WithReference(api)
       .WithHttpEndpoint(env: "PORT");
```

**Run:** `cd samples/dotnet-angular-cosmos && aspire run`

<!~~ CosmosDB emulator runs locally via Docker, no Azure account needed! ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 5: .NET API — Code

```csharp
var builder = WebApplication.CreateBuilder(args);
builder.AddServiceDefaults();
builder.AddAzureCosmosClient("cosmos");

var app = builder.Build();
app.MapGet("/products", async (CosmosClient client) =>
{
    var container = client.GetDatabase("products").GetContainer("items");
    return await container.GetItemQueryIterator<Product>("SELECT * FROM c").ReadNextAsync();
});
```

---

<!~~ _class: compact code-compact ~~>

# Demo 5: Angular Client — Code

```typescript
ngOnInit() {
  const apiUrl = environment.services__api__http__0;
  this.http.get(`${apiUrl}/products`).subscribe(data => {
    this.products = data;
  });
}
```

**Pattern:** Aspire injects the backend URL into the SPA environment.

<!~~ CosmosDB provides NoSQL document storage with global distribution (emulator for local dev). ~~>

---

<!~~ _class: compact dense code-compact ~~>

# Demo 6: Go + Svelte — Bookmarks

**What:** Go HTTP server (via AddDockerfile) with Svelte frontend + **PostgreSQL**

**Use Case:** Any language via Docker, custom build processes with database

**Architecture:**
![w:680px center](./img/go-svelte-bookmarks.drawio.svg)

**apphost.go:**
```go
pg, _ := builder.AddPostgres("pg")
db, _ := pg.AddDatabase("bookmarksdb")

api, _ := builder.AddDockerfile("api", "./go-api")
api.WithReference(db)
api.WithHttpEndpoint(8080, "http")
api.WithExternalHttpEndpoints()

frontend, _ := builder.AddNpmApp("frontend", "./frontend", "dev")
frontend.WithEnvironment("services__api__http__0", api.GetEndpoint("http"))
```

**Go AppHost orchestrates Dockerized Go + npm-based Svelte + PostgreSQL together.**

<!~~ AddDockerfile lets you run ANY language that you can containerize. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 6: Bookmarks — Dockerfile & Go Code

```dockerfile
FROM golang:1.23-alpine
WORKDIR /app
COPY . .
RUN go build -o main .
CMD ["./main"]
```

```go
func bookmarksHandler(w http.ResponseWriter, r *http.Request) {
    bookmarks := []string{"https://aspire.dev", "https://github.com"}
    json.NewEncoder(w).Encode(bookmarks)
}
```

**Run:** `cd samples/svelte-go-bookmarks && aspire run`

<!~~ AddDockerfile is the escape hatch for any language Aspire doesn't support natively! ~~>

---

# Demo 7: Polyglot Event Stream

**The Grand Finale:** C# + Python + Node.js + Kafka

**What:** Multi-language microservices with Kafka event streaming

**Use Case:** Event-driven architectures, real-world microservices

**Architecture:** (see next slide)

**Components:**
- .NET Producer — generates events
- Python Consumer — processes events
- Node.js Dashboard — real-time WebSocket UI
- Apache Kafka — event streaming platform

<!~~ This is the most complex demo — it shows how Aspire shines with multiple languages. ~~>

---

<!~~ _class: compact ~~>

# Demo 7: Polyglot Event Stream — Architecture

![w:900px center](./img/event-stream-architecture.drawio.svg)

**Event Flow:**
1. .NET producer publishes events into Kafka
2. Python consumer enriches them and writes processed events back
3. Node.js dashboard streams updates to the browser over WebSocket

<!~~ Kafka provides high-throughput event streaming across all three services! ~~>

---

<!~~ _class: compact dense code-compact ~~>

# Demo 7: Polyglot Event Stream — AppHost

```csharp
var kafka = builder.AddKafka("messaging").WithKafkaUI();

builder.AddProject<Projects.EventProducer>("producer")
       .WithReference(kafka);

builder.AddPythonApp("consumer", "../python-consumer", "main.py")
       .WithReference(kafka);

builder.AddNpmApp("dashboard", "../node-dashboard", "start")
       .WithReference(kafka)
       .WithHttpEndpoint(env: "PORT");
```

**Shared wiring:** `CONNECTIONSTRINGS__messaging`, service discovery, unified telemetry.

<!~~ Look how simple the orchestration code is! Aspire handles all the complexity. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 7: Event Stream — .NET Producer

**Program.cs:**
```csharp
var builder = WebApplication.CreateBuilder(args);
builder.AddServiceDefaults();
builder.AddKafkaProducer<string, string>("messaging");

var app = builder.Build();

app.MapPost("/publish", async (string message, IProducer<string, string> producer) =>
{
    var kafkaMessage = new Message<string, string>
    {
        Key = Guid.NewGuid().ToString(),
        Value = JsonSerializer.Serialize(new { text = message, timestamp = DateTime.UtcNow })
    };
    
    await producer.ProduceAsync("events", kafkaMessage);
    
    return Results.Accepted();
});

app.Run();
```

**Publishes to Kafka, returns immediately** — fire and forget!

<!~~ The producer doesn't wait for consumers. Kafka buffers all events. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 7: Event Stream — Python Consumer
```python
from kafka import KafkaConsumer, KafkaProducer
import json
import os

# Aspire injects Kafka connection
kafka_url = os.environ['CONNECTIONSTRINGS__messaging']

consumer = KafkaConsumer(
    'events',
    bootstrap_servers=kafka_url,
    value_deserializer=lambda m: json.loads(m.decode('utf-8'))
)

producer = KafkaProducer(
    bootstrap_servers=kafka_url,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

for message in consumer:
    event = message.value
    
    # Process event (e.g., enrich with ML prediction)
    processed = {
        **event,
        'sentiment': analyze_sentiment(event['text']),
        'processed_at': datetime.utcnow().isoformat()
    }
    
    producer.send('processed-events', processed)
```

<!~~ The Python consumer enriches events with ML predictions, analytics, etc. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 7: Event Stream — Node.js Dashboard

```javascript
const io = new Server(http.createServer(app));
const kafka = new Kafka({ brokers: [process.env.CONNECTIONSTRINGS__messaging] });
const consumer = kafka.consumer({ groupId: 'dashboard' });

await consumer.subscribe({ topic: 'processed-events' });
await consumer.run({
  eachMessage: async ({ message }) =>
    io.emit('event', JSON.parse(message.value.toString()))
});
```

**Run:** `cd samples/polyglot-event-stream/AppHost && dotnet run`

<!~~ Three languages, real-time streaming via Kafka + WebSocket! ~~>

---

<!~~ _class: compact ~~>

# Demo 7: Live Dashboard Experience

**You can watch**
- `producer`, `consumer`, `dashboard`, and `messaging` start together
- End-to-end traces: HTTP → .NET → Kafka → Python → Node.js → browser
- Unified logs per service with filters by level and time

**This is the payoff:** one dashboard across every language in the stack.

<!~~ One unified view of everything, regardless of language. This is transformative. ~~>


============================================================
  END DEMO SLIDES
============================================================
-->
