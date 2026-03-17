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
~~<i class="fa-brands fa-twitter"></i> Twitter: @Chris_L_Ayers~~

---

# What is Aspire?

**A polyglot orchestration platform for building, running, and deploying distributed applications**

- **Orchestration** — Start and manage multiple services in any language
- **Service Discovery** — Connect services automatically via environment variables
- **Observability** — Built-in OpenTelemetry: logs, traces, and metrics
- **Configuration** — Connection strings and endpoints auto-injected
- **Container-Ready** — Docker, Dockerfile, and container lifecycle management
- **Polyglot AppHost** — Define your stack in C# or TypeScript

**Key Insight:** Aspire orchestrates **any language** — C#, Python, JavaScript, TypeScript, Go, Java, and more!

<!-- The AppHost is the single place that defines your entire distributed application. Any language can be orchestrated. -->

---

# The Polyglot Problem

Modern applications use multiple languages:

<div class="columns3">
<div>

**Backend**
- .NET APIs
- Python ML
- Go services

</div>
<div>

**Frontend**
- React
- Vue
- Svelte

</div>
<div>

**Infrastructure**
- Kafka
- Redis
- PostgreSQL
- CosmosDB

</div>
</div>

**Challenges:**
- Service discovery chaos (hardcoded URLs everywhere)
- Configuration fragmentation (`.env` files, YAML, JSON...)
- Observability gaps (separate dashboards per language)
- Complex local setup (15-step README to run locally)

<!-- We've all been there: README says "just run docker-compose up" but it never works the first time. -->

---

# Aspire's Answer

**One orchestrator to rule them all** 🎯

```csharp
var builder = DistributedApplication.CreateBuilder(args);

var redis = builder.AddRedis("cache");
var postgres = builder.AddPostgres("db").AddDatabase("appdata");

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

**Result:** All services visible in one dashboard, auto-wired, observable!

<!-- This is the Aspire AppHost. It's the central brain that starts everything and wires it together. -->

---

# Key Aspire Concepts

<div class="columns">
<div>

**AppHost**
- Central orchestrator
- Defines resources
- Manages lifecycle

**ServiceDefaults**
- Shared configuration
- OpenTelemetry setup
- Health checks

**Resources**
- Services (APIs, apps)
- Infrastructure (Redis, Postgres)
- Executables, containers

</div>
<div>

**Dashboard**
- Real-time logs
- Distributed traces
- Metrics & health

**References**
- Automatic service discovery
- Connection string injection
- Environment variables

**Lifecycle**
- Start dependencies first
- Monitor health
- Graceful shutdown

</div>
</div>

<!-- Think of AppHost as the conductor of an orchestra, and each service as an instrument. -->

---

<!-- _class: compact code-compact -->

# Aspire CLI Quick Start

3 steps to a running app:

```bash
winget install Microsoft.Aspire.Cli

aspire new aspire-ts-starter -n my-app
cd my-app && aspire run
```

**Other starters:** `aspire-starter`, `aspire-py-starter`, `aspire-js-starter`

**What you get:**
- Correct startup order
- Dashboard with login token
- Auto-injected endpoints + connection strings
- Live logs, traces, and health

<!-- No .NET SDK required for TypeScript/Python apphosts. The Aspire CLI is standalone. -->

---

<!-- _class: compact code-compact -->

# TypeScript AppHost (New in 13.2!)

**Define the whole stack in TypeScript** — no .NET SDK needed:

```typescript
const builder = await createBuilder();

const postgres = await builder.addPostgres("db");
const db = await postgres.addDatabase("appdata");

await builder
  .addNodeApp("api", "../api", "server.js")
  .withNpm()
  .withReference(db);

await builder.build().run();
```

**Same 40+ integrations as C#** — Redis, Azure, Kafka, MongoDB, and more.

<!-- The TypeScript AppHost uses the same integration packages as C#, auto-generated via [AspireExport] attributes. -->

---

# <!--fit--> Architecture Deep Dive

Understanding how Aspire orchestrates non-.NET services

<!-- Now let's look under the hood to see how Aspire actually works with multiple languages. -->

---

<!-- _class: compact -->

# How Aspire Orchestrates Non-.NET Services

Aspire ships runtime-specific builders for the common cases:

<div class="columns">
<div>

- **Python** — `AddUvicornApp("api", "../python", "main:app").WithUv()`
- **Node.js** — `AddNodeApp("api", "../node", "server.js").WithNpm()`
- **Vite** — `AddViteApp("frontend", "../react").WithHttpEndpoint(env: "PORT")`

</div>
<div>

- **Go** — `AddGolangApp("api", "../go").WithHttpEndpoint(env: "PORT")`
- **Java** — `AddSpringApp("api", "../java", "app.jar").WithHttpEndpoint(port: 8080)`
- **Any language** — `AddDockerfile("service", "../rust")`

</div>
</div>

**Shared benefits:** service discovery, health, lifecycle, logs, traces, metrics.

<!-- Behind the scenes, Aspire launches these processes and monitors them just like .NET projects. -->

---

# Service Discovery via Environment Variables

Aspire injects service endpoints as environment variables:

```bash
# Pattern: services__<name>__<protocol>__<index>
services__api__http__0=http://localhost:5000
services__api__https__0=https://localhost:5001

services__frontend__http__0=http://localhost:3000
```

**In your Python code:**
```python
import os

api_url = os.environ.get('services__api__http__0')
response = requests.get(f'{api_url}/data')
```

**In your Node.js code:**
```javascript
const apiUrl = process.env['services__api__http__0'];
const response = await fetch(`${apiUrl}/data`);
```

No hardcoded URLs!

<!-- This is the magic that makes service discovery work across any language. -->

---

# Connection String Injection

Infrastructure resources get connection strings automatically:

```bash
# Pattern: CONNECTIONSTRINGS__<resource>
CONNECTIONSTRINGS__cache=localhost:6379
CONNECTIONSTRINGS__db=Host=localhost;Port=5432;Username=postgres;Password=...
CONNECTIONSTRINGS__queue=amqp://localhost:5672
```

**In Python:**
```python
import os
import redis

redis_conn = os.environ['CONNECTIONSTRINGS__cache']
client = redis.from_url(f'redis://{redis_conn}')
```

**In Node.js:**
```javascript
const redis = require('redis');
const client = redis.createClient({
  url: `redis://${process.env.CONNECTIONSTRINGS__cache}`
});
```

<!-- Aspire handles the complexity of connection strings so you don't have to manage .env files. -->

---

<!-- _class: compact -->

# Architecture Overview

![w:980px center](./img/architecture-overview.drawio.svg)

<!-- The AppHost is the central orchestrator. All services report their telemetry to the unified dashboard. -->

---

# The Aspire Dashboard

**A unified control center for ALL your services**

Features:
- **Resources Tab** — See all services, containers, and their status
- **Console Logs** — Real-time stdout/stderr from every service
- **Structured Logs** — Searchable, filterable logs with levels
- **Traces** — Distributed request tracing across services
- **Metrics** — CPU, memory, request counts, custom metrics
- **Health** — Status of health check endpoints

**Runs on:** `https://localhost:<port>/login?t=<token>` (dynamic port with login token)

**Works with:** Python, Node.js, Go, Rust, Java, .NET — any language!

<!-- This is the killer feature. One dashboard for everything, regardless of language. -->

---

<!-- _class: compact -->

# Resource Lifecycle Management

Aspire manages the complete lifecycle of your services:

![w:1080px center](./img/resource-lifecycle-management.drawio.svg)

**Dependency Order:** Infrastructure → Backend Services → Frontend

**Health Monitoring:** Automatic restarts on failure (configurable)

**Graceful Shutdown:** Clean termination of all processes

<!-- Aspire ensures services start in the right order and shut down cleanly. -->

---

# Health Checks Across Languages

Aspire can monitor health endpoints in any language:

**Python (Flask):**
```python
@app.route('/health')
def health():
    return {'status': 'healthy'}, 200
```

**Node.js (Express):**
```javascript
app.get('/health', (req, res) => {
  res.json({ status: 'healthy' });
});
```

**Configure in AppHost:**
```csharp
builder.AddPythonApp("api", "../python", "app.py")
       .WithHttpHealthCheck("/health");
```

Dashboard shows health status in real-time!

<!-- Health checks help Aspire know when a service is ready to receive traffic. -->

---

# OpenTelemetry Integration

Aspire uses **OpenTelemetry** for unified observability:

<div class="columns">
<div>

**What is OpenTelemetry?**
- Industry-standard observability
- Logs, traces, metrics
- Language-agnostic
- Vendor-neutral

**Aspire Provides:**
- OTLP endpoint (dashboard)
- Auto-instrumentation (.NET)
- Easy manual setup (other languages)

</div>
<div>

**Python Example:**
```python
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

provider = TracerProvider()
processor = BatchSpanProcessor(
    OTLPSpanExporter(
        endpoint=os.environ.get('OTEL_EXPORTER_OTLP_ENDPOINT')
    )
)
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)
```

</div>
</div>

<!-- OpenTelemetry is the secret sauce that makes cross-language observability possible. -->

---

# <!--fit--> Demos

<!-- Live demos of 7 real-world polyglot samples -->

---

# <!--fit--> Observability & Service Discovery

Deep dive into cross-language tracing and configuration

<!-- Now let's dive deeper into how observability works across all these languages. -->

---

## OpenTelemetry Across Languages

**Unified traces require each service to export telemetry:**

<div class="columns">
<div>

**Python:**
```python
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

endpoint = os.environ.get(
  'OTEL_EXPORTER_OTLP_ENDPOINT',
  'http://localhost:4317'
)

provider = TracerProvider()
processor = BatchSpanProcessor(
  OTLPSpanExporter(endpoint=endpoint)
)
provider.add_span_processor(processor)
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
const exporter = new OTLPTraceExporter({
  url: process.env.OTEL_EXPORTER_OTLP_ENDPOINT
});
provider.addSpanProcessor(
  new BatchSpanProcessor(exporter)
);
provider.register();
```

</div>
</div>

**Aspire sets `OTEL_EXPORTER_OTLP_ENDPOINT` automatically!**

<!-- This is boilerplate you'd add once per service. Then all traces flow to Aspire. -->

---

<!-- _class: compact -->

## Aspire Dashboard Features

<div class="columns">
<div>

**Resources** 📋
Services, containers, status, endpoints

**Console Logs** 📜
Real-time stdout/stderr per process

**Structured Logs** 📊
Parsed JSON logs, filter by level

</div>
<div>

**Traces** 🔍
Distributed request traces, waterfall view

**Metrics** 📈
Latency, CPU/memory, custom metrics

**Environment** ⚙️
Env vars, connection strings, discovery URLs

</div>
</div>

<!-- The dashboard is more powerful than just docker-compose logs. -->

---

## Distributed Tracing Example

**Request Flow: Python → .NET → Node.js**

```
Trace ID: abc123...
└─ Span: HTTP POST /api/process (Python, 245ms)
   ├─ Span: HTTP POST /data (calling .NET, 200ms)
   │  ├─ Span: SQL SELECT (PostgreSQL, 50ms)
   │  └─ Span: HTTP POST /notify (calling Node.js, 100ms)
   │     └─ Span: Redis SET (25ms)
   └─ Span: Render response (40ms)
```

**In Aspire Dashboard:**
- Click a trace to see the full waterfall
- See which service/language owns each span
- Identify bottlenecks (SQL query took 50ms)
- Debug failures (which span errored?)

**TraceID propagates automatically** via HTTP headers (W3C Trace Context)

<!-- This is the holy grail of microservices debugging. -->

---

<!-- _class: compact code-compact -->

## Structured Logging Aggregation

**Emit JSON logs for best dashboard experience:**

**Python:**
```python
import logging
import json

logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)

def log_structured(level, message, **kwargs):
    log_data = {'message': message, **kwargs}
    logger.log(level, json.dumps(log_data))

log_structured(logging.INFO, 'User logged in', user_id=123, ip='1.2.3.4')
```

**Node.js (using pino):**
```javascript
const pino = require('pino');
const logger = pino();

logger.info({ userId: 123, ip: '1.2.3.4' }, 'User logged in');
```

**Aspire Dashboard** parses JSON logs into searchable fields!

<!-- Structured logs are much more useful than plain text logs. -->

---

<!-- _class: compact code-compact -->

## Health Check Endpoints

**Aspire can monitor readiness in any language:**

```csharp
builder.AddPythonApp("api", "../python", "app.py")
       .WithHttpEndpoint(port: 5000)
       .WithHttpHealthCheck("/health");
```

```python
@app.route('/health')
def health():
    db.execute('SELECT 1')
    return {'status': 'healthy'}, 200
```

**Dashboard states:** 💚 Healthy, ❤️ Unhealthy, ⚠️ Degraded

<!-- Health checks help Aspire know when to restart a failed service. -->

---

## Service Discovery Deep Dive

**Environment Variable Naming Convention:**

```
services__<resource-name>__<scheme>__<index>
```

**Examples:**
- `services__api__http__0` = `http://localhost:5000`
- `services__api__https__0` = `https://localhost:5001`
- `services__frontend__http__0` = `http://localhost:3000`

**Multiple Endpoints:**
If a service has multiple endpoints, index increments:
- `services__api__http__0` = `http://localhost:5000`
- `services__api__http__1` = `http://localhost:5002` (second HTTP endpoint)

**Access in any language:**
```python
api_url = os.environ['services__api__http__0']
```

<!-- The double underscore __ is used because environment variables can't have colons. -->

---

## Connection Strings Deep Dive

**Naming Convention:**
```
CONNECTIONSTRINGS__<resource-name>
```

**Redis Example:**
```bash
CONNECTIONSTRINGS__cache=localhost:6379
```

**PostgreSQL Example:**
```bash
CONNECTIONSTRINGS__db=Host=localhost;Port=5432;Username=postgres;Password=...
```

**Kafka Example:**
```bash
CONNECTIONSTRINGS__messaging=localhost:9092
```

**Aspire auto-generates these** based on resource type!

**Custom connection strings:**
```csharp
builder.AddConnectionString("custom", "Server=myserver;...");
```

<!-- Aspire knows the correct format for each resource type (Redis, Postgres, etc). -->

---

<!-- _class: compact code-compact -->

## Deploying Polyglot Aspire Apps

```bash
aspire run
aspire deploy
aspire publish
```

**What Aspire generates**
- Images for Python, Node.js, .NET, Go, Java, and more
- Azure Container Apps / Kubernetes artifacts
- Infrastructure wiring for Redis, Postgres, CosmosDB, Kafka, etc.
- Service connections + environment variables

**Same AppHost model** for local, staging, and production.

<!-- Aspire uses the same model for local dev and production. No code changes needed to deploy. -->

---

# <!--fit--> Wrap-Up & Resources

Bringing it all together

<!-- Let's wrap up with key takeaways and resources. -->

---

<!-- _class: compact code-compact -->

# Aspire Polyglot Cheat Sheet

<div class="columns">
<div>

**Runtime**
- Python / ASGI
- Python / Uvicorn
- Node.js
- Vite / React
- .NET project
- Go
- Spring Boot
- Any Dockerfile

</div>
<div>

**Method**
```csharp
AddPythonApp()
AddUvicornApp()
AddNodeApp()
AddViteApp()
AddProject<T>()
AddGolangApp()
AddSpringApp()
AddDockerfile()
```

</div>
</div>

---

<!-- _class: compact code-compact -->

## Aspire Cheat Sheet — Common Patterns

```csharp
.WithReference(redis)
.WaitFor(postgres)
.WithHttpEndpoint(env: "PORT")
.WithExternalHttpEndpoints()
.WithUv()
.WithNpm()
.PublishWithContainerFiles()
.WithHttpHealthCheck("/health")
```

**Infrastructure**
- Redis → `AddRedis("name")`
- PostgreSQL → `AddPostgres("name").AddDatabase("db")`
- Kafka → `AddKafka("name")`
- CosmosDB → `AddAzureCosmosDB("name").RunAsEmulator()`

<!-- Keep this slide handy as a quick reference! -->

---

## Resources & Links

**Official Docs:**
- 🌐 [aspire.dev](https://aspire.dev) — Official website & docs
- 🐙 [github.com/dotnet/aspire](https://github.com/dotnet/aspire) — Source code

**Sample Code:**
- 🐙 [github.com/codebytes/aspire-polyglot](https://github.com/codebytes/aspire-polyglot) — This repo!
- 🛒 [github.com/dotnet/eShop](https://github.com/dotnet/eShop) — eShop polyglot sample

**Community:**
- 💬 [Discord: Aspire channel](https://aka.ms/dotnet-discord)

**CLI Reference:**
- [aspire.dev/reference/cli](https://aspire.dev/reference/cli/) — Full CLI docs
- `aspire new` / `aspire run` / `aspire deploy` / `aspire publish`

<!-- Join the community! Aspire is open source and very active. -->

---

<!-- _class: compact -->

## What's New in 13.2: Polyglot

<div class="columns">
<div>

**TypeScript AppHost** ✨
- Write the AppHost in TypeScript — no .NET SDK required
- Async fluent API with typed codegen
- Same integration surface as the C# AppHost

**Standalone CLI**
- Self-extracting `aspire` binary
- `run`, `deploy`, `publish`, `doctor`, and `certs`

</div>
<div>

**40+ Integration Exports** ✅
- Databases: Postgres, Redis, SQL Server, MongoDB, MySQL
- Messaging + cloud: Kafka, RabbitMQ, CosmosDB, Service Bus, Storage, Functions
- DX: better errors, callback dispatch, hot reload, hosting isolation

</div>
</div>

<!-- Aspire 13.2 is the first release where TypeScript AppHost has full integration support. -->

---

# Key Takeaways

**Aspire orchestrates ANY language** — Python, Node.js, Go, Java, TypeScript, and more

**TypeScript AppHost** — Write your AppHost in TypeScript, no .NET SDK required

**40+ integrations exported** — All major hosting packages work from TypeScript or C#

**Unified observability** — One dashboard for logs, traces, metrics across all services

**Automatic service discovery** — Endpoints and connection strings injected via env vars

**Standalone CLI** — `aspire run` / `aspire deploy` / `aspire publish` without .NET SDK

**Deploy anywhere** — Same AppHost model for local dev, staging, and production

**Open source** — [github.com/dotnet/aspire](https://github.com/dotnet/aspire) — MIT license

<!-- Aspire 13.2 polyglot: TypeScript AppHost, 40+ integrations, standalone CLI. -->

---

# Questions?

![bg right](./img/owl.png)

---

# Resources

<div class="columns">
<div>

## Links

- [Aspire](https://aspire.dev)
- [Aspire Docs](https://aspire.dev/get-started/first-app/)
- [Aspire Samples](https://github.com/dotnet/aspire-samples)
- [Polyglot Aspire Samples](https://github.com/codebytes/aspire-polyglot)
- [eShop Polyglot Sample](https://github.com/dotnet/eShop)
- [Aspire Community Toolkit](https://github.com/CommunityToolkit/Aspire)

</div>
<div>

## Follow Chris Ayers

![w:400px](./img/chris_ayers.svg)

</div>
</div>


<!--
============================================================
  DEMO SLIDES (commented out — uncomment to include in deck)
============================================================

---

# <!~~fit~~> Live Demo Walkthroughs

7 real-world polyglot samples

<!~~ Time to see Aspire in action! We'll walk through 7 samples that showcase different patterns. ~~>

---

# Demo 1: Python Web App — Markdown Wiki

**What:** Flask web app with SQLite database

**Use Case:** Python web services, simple CRUD apps, Markdown rendering

**Key Code (apphost.py):**
```python
builder.add_python_app("wiki", "./src", "main.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()
```

**That's it!**

**Run it:**
```bash
cd samples/flask-markdown-wiki
aspire run
```

**Dashboard:** Shows Flask app with clickable link to `http://localhost:5000`

<!~~ This demo shows the simplest Python web app pattern with Aspire. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 1: Flask Wiki — What You See

**Dashboard view**
- `wiki` resource with a clickable local URL
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

**Pattern:** tiny Python web app + SQLite, ideal for docs and internal tools.

<!~~ Flask + SQLite is a great pattern for simple CRUD apps. ~~>

---

<!~~ _class: compact code-compact ~~>

# Demo 2: Django + HTMX — Voting Polls

**What:** Django web app with HTMX for server-rendered interactivity + SQLite

**Use Case:** Python web frameworks, server-rendered UIs, progressive enhancement

**Architecture:**
![w:860px center](./img/django-htmx-voting-polls.drawio.svg)

**apphost.py:**
```python
builder.add_python_app("polls", "./src", "run.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()
```

<!~~ Django with HTMX shows modern server-rendered patterns without heavy frontend frameworks. ~~>

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

**apphost.py**
```python
cache = builder.add_redis("cache")

api = builder.add_python_app("api", "./src/api", "main.py").with_reference(cache)
builder.add_npm_app("web", "./src/web", "dev").with_reference(api)
```

**Pattern:** Vite proxies API calls while Aspire wires service discovery.

<!~~ This shows the modern pattern: Vite for fast React dev + Python API backend. ~~>

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
<!~~ _class: compact code-compact ~~>

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

**What:** Go HTTP server (via AddDockerfile) with Svelte frontend

**Use Case:** Any language via Docker, custom build processes

**Architecture:**
![w:680px center](./img/go-svelte-bookmarks.drawio.svg)

**apphost.go:**
```go
api, _ := builder.AddDockerfile("api", "./go-api")
api.WithHttpEndpoint(8080, "http")
api.WithExternalHttpEndpoints()

frontend, _ := builder.AddNpmApp("frontend", "./frontend", "dev")
frontend.WithEnvironment("services__api__http__0", api.GetEndpoint("http"))
```

**Go AppHost orchestrates Dockerized Go + npm-based Svelte together.**

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
