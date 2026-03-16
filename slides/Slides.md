---
marp: true
theme: custom-aspire-light
footer: '@Chris_L_Ayers - https://chris-ayers.com'
---

<!-- _footer: 'https://github.com/codebytes/aspire-polyglot' -->

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

# Aspire CLI Quick Start

Get started in 3 commands:

```bash
# 1. Install Aspire CLI
# macOS/Linux:
curl -sSL https://aka.ms/install-aspire | bash
# Windows:
winget install Microsoft.Aspire.Cli

# 2. Create a new Aspire app
aspire new aspire-starter -n MyApp        # C# starter
aspire new aspire-py-starter -n my-app    # Python + React
aspire new aspire-js-starter -n my-app    # Node.js + React
aspire new aspire-ts-starter -n my-app    # TypeScript AppHost

# 3. Run it!
cd my-app && aspire run
```

**What happens:**
- All services start in correct order
- Dashboard opens with login token
- Connection strings auto-injected
- Logs/traces stream to dashboard

<!-- No .NET SDK required for TypeScript/Python apphosts. The Aspire CLI is standalone. -->

---

# TypeScript AppHost (New in 13.2!)

**Define your entire stack in TypeScript** — no .NET SDK needed:

```typescript
// apphost.ts
import { createBuilder, ContainerLifetime } from "./.modules/aspire.js";

const builder = await createBuilder();

const postgres = await builder
  .addPostgres("db")
  .withDataVolume()
  .withLifetime(ContainerLifetime.Persistent);

const db = await postgres.addDatabase("appdata");

const api = await builder
  .addNodeApp("api", "../api", "server.js")
  .withNpm()
  .withReference(db)
  .waitFor(db);

await builder
  .addViteApp("frontend", "../frontend")
  .withReference(api);

await builder.build().run();
```

**Full access to 40+ integrations** — Redis, Azure, Kafka, MongoDB, and more!

<!-- The TypeScript AppHost uses the same integration packages as C#, auto-generated via [AspireExport] attributes. -->

---

# <!--fit--> Architecture Deep Dive

Understanding how Aspire orchestrates non-.NET services

<!-- Now let's look under the hood to see how Aspire actually works with multiple languages. -->

---

# How Aspire Orchestrates Non-.NET Services

Aspire provides extension methods for different language runtimes:

<div class="columns">
<div>

**Python**
```csharp
builder.AddUvicornApp(
  "api", "../python", "main:app"
).WithUv();

// or classic:
builder.AddPythonApp(
  "service", "../src", "main.py"
);
```

**Node.js / Vite**
```csharp
builder.AddNodeApp(
  "api", "../node", "server.js"
).WithNpm();

builder.AddViteApp(
  "frontend", "../react"
).WithHttpEndpoint(env: "PORT");
```

</div>
<div>

**Go**
```csharp
builder.AddGolangApp(
  "api", "../go"
).WithHttpEndpoint(env: "PORT");
```

**Java (Spring Boot)**
```csharp
builder.AddSpringApp(
  "api", "../java", "app.jar"
).WithHttpEndpoint(port: 8080);
```

**Dockerfile (any language)**
```csharp
builder.AddDockerfile(
  "service", "../rust"
);
```

</div>
</div>

All get the same benefits: service discovery, observability, lifecycle management!

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

# Architecture Overview

<div class="mermaid">
graph TD
    AppHost[AppHost<br/>Orchestrator]
    Dashboard[Aspire Dashboard]
    
    AppHost --> PythonApp[Python Service]
    AppHost --> ReactApp[React/Vite Frontend]
    AppHost --> DotNetAPI[.NET API]
    AppHost --> JavaAPI[Java Spring Boot]
    AppHost --> GoService[Go Service]
    
    AppHost --> Redis[Redis]
    AppHost --> Postgres[PostgreSQL]
    AppHost --> Cosmos[CosmosDB]
    AppHost --> Kafka[Kafka]
    
    PythonApp --> Redis
    ReactApp --> PythonApp
    JavaAPI --> Postgres
    DotNetAPI --> Cosmos
    GoService --> Kafka
    
    PythonApp -.logs/traces.-> Dashboard
    ReactApp -.logs/traces.-> Dashboard
    DotNetAPI -.logs/traces.-> Dashboard
    JavaAPI -.logs/traces.-> Dashboard
    GoService -.logs/traces.-> Dashboard
</div>

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

# Resource Lifecycle Management

Aspire manages the complete lifecycle of your services:

```
┌─────────────┐
│   STARTING  │ ← Dependencies start first (Redis, DB)
└──────┬──────┘
       ↓
┌─────────────┐
│   RUNNING   │ ← Health checks monitor status
└──────┬──────┘
       ↓
┌─────────────┐
│  STOPPING   │ ← Graceful shutdown on Ctrl+C
└──────┬──────┘
       ↓
┌─────────────┐
│   STOPPED   │
└─────────────┘
```

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

# <!--fit--> Live Demo Walkthroughs

7 real-world polyglot samples

<!-- Time to see Aspire in action! We'll walk through 7 samples that showcase different patterns. -->

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

<!-- This demo shows the simplest Python web app pattern with Aspire. -->

---

# Demo 1: Flask Wiki — What You See

**In the Aspire Dashboard:**

- **Resources Tab:** `wiki` service with link to http://localhost:5000
- **Console Logs:** Flask startup messages and request logs
- **Status:** Running, healthy
- **Endpoint:** Clickable link to wiki UI

**Flask app (`app.py`):**
```python
from flask import Flask, request, render_template_string
import markdown
import sqlite3

app = Flask(__name__)

@app.route('/')
def index():
    # ... fetch pages from SQLite ...
    return render_template_string(wiki_template, pages=pages)

@app.route('/page/<title>')
def view_page(title):
    # ... render Markdown page ...
    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
```

**Simple Python web app with SQLite!** Perfect for wikis, docs, internal tools.

<!-- Flask + SQLite is a great pattern for simple CRUD apps. -->

---

# Demo 2: Django + HTMX — Voting Polls

**What:** Django web app with HTMX for server-rendered interactivity + SQLite

**Use Case:** Python web frameworks, server-rendered UIs, progressive enhancement

**Architecture:**
```
┌──────────────┐
│  Django App  │ Python + HTMX
│  Port: 8000  │
└──────┬───────┘
       │
       ↓
┌──────────────┐
│   SQLite     │ Database
└──────────────┘
```

**apphost.py:**
```python
builder.add_python_app("polls", "./src", "run.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()
```

<!-- Django with HTMX shows modern server-rendered patterns without heavy frontend frameworks. -->

---

# Demo 2: Django HTMX — Run It

**Commands:**
```bash
cd samples/django-htmx-polls
aspire run
```

**What happens:**
1. Aspire installs Python dependencies (Django, django-htmx)
2. Runs Django migrations automatically
3. Starts Django dev server on port 8000
4. Dashboard shows clickable link to `http://localhost:8000`

**Key Django + HTMX Code:**
```python
# views.py
from django.shortcuts import render, get_object_or_404
from django.http import HttpResponse

def vote(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    question.votes += 1
    question.save()
    
    if request.htmx:  # HTMX request
        return render(request, 'polls/_results.html', {'question': question})
    return render(request, 'polls/results.html', {'question': question})
```

<!-- HTMX lets you build interactive UIs with server-side rendering, no React needed! -->

---

# Demo 3: Vite + React + FastAPI — TODO App

**What:** Vite + React frontend with FastAPI backend and Redis caching

**Use Case:** Modern JavaScript frontends with Python APIs, Redis caching layer

**Architecture:**
```
┌───────────┐     ┌──────────┐     ┌─────────┐
│  Vite +   │     │ FastAPI  │     │  Redis  │
│  React    │────>│ Backend  │────>│ Cache   │
│ Port: 5173│     │ Port: 8000     │ Port: 6379
└───────────┘     └──────────┘     └─────────┘
```

**apphost.py:**
```python
cache = builder.add_redis("cache")

api = builder.add_python_app("api", "./src/api", "main.py") \
    .with_reference(cache) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

builder.add_npm_app("web", "./src/web", "dev") \
    .with_reference(api) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()
```

**Key Pattern:** Vite's dev server proxies API requests automatically!

<!-- This shows the modern pattern: Vite for fast React dev + Python API backend. -->

---

# Demo 3: Vite React TODO — Code

**FastAPI Backend (`backend/main.py`):**
```python
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import redis
import os

app = FastAPI()
app.add_middleware(CORSMiddleware, allow_origins=["*"])

# Aspire injects Redis connection
redis_conn = os.environ.get('CONNECTIONSTRINGS__cache')
cache = redis.from_url(redis_conn)

@app.get('/api/todos')
async def get_todos():
    cached = cache.get('todos')
    if cached:
        return json.loads(cached)
    todos = [{"id": 1, "title": "Learn Aspire", "done": False}]
    cache.setex('todos', 60, json.dumps(todos))
    return todos
```

**React Frontend (`frontend/src/App.tsx`):**
```typescript
const apiUrl = import.meta.env.VITE_services__api__http__0;

useEffect(() => {
  fetch(`${apiUrl}/api/todos`)
    .then(res => res.json())
    .then(data => setTodos(data));
}, []);
```

**Run:** `cd samples/vite-react-api && aspire run`

<!-- Vite + React with Python backend is a popular stack. Aspire makes it seamless! -->

---

# Demo 4: Spring Boot + PostgreSQL — Notes App

**What:** Java Spring Boot REST API with PostgreSQL database and pgAdmin

**Use Case:** Java microservices, relational databases, enterprise patterns

**Architecture:**
```
┌──────────────┐     ┌────────────┐     ┌─────────┐
│ Spring Boot  │     │ PostgreSQL │     │ pgAdmin │
│   REST API   │────>│ Database   │<────│ Web UI  │
│  Port: 8080  │     │ Port: 5432 │     │ Port: 5050
└──────────────┘     └────────────┘     └─────────┘
```

**AppHost.java:**
```java
var pg = builder.addPostgres("pg", null, null);
pg.withPgAdmin();
var db = pg.addDatabase("notesdb");

builder.addDockerfile("api", "./src")
    .withReference(db)
    .withHttpEndpoint(8080, "PORT")
    .withExternalHttpEndpoints();
```

**Key Pattern:** Java AppHost with `AddDockerfile` builds multi-stage Java container!

<!-- Spring Boot is the dominant Java framework. AddDockerfile lets Aspire run it! -->

---

# Demo 4: Spring Boot Notes — Java Code

**Spring Boot App (`Application.java`):**
```java
@SpringBootApplication
@RestController
public class NotesApplication {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/api/notes")
    public List<Note> getNotes() {
        return jdbcTemplate.query(
            "SELECT id, title, content FROM notes",
            (rs, rowNum) -> new Note(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content")
            )
        );
    }
    
    @PostMapping("/api/notes")
    public Note createNote(@RequestBody Note note) {
        jdbcTemplate.update(
            "INSERT INTO notes (title, content) VALUES (?, ?)",
            note.getTitle(), note.getContent()
        );
        return note;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }
}
```

**Dockerfile (multi-stage build):**
```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Run:** `cd samples/spring-boot-postgres && aspire run`

**Connection string injected** as `spring.datasource.url` environment variable!

<!-- Multi-stage Docker builds keep production images small. Aspire handles Dockerfile orchestration! -->

---
# Demo 5: .NET + Angular + CosmosDB

**What:** ASP.NET Core API + Angular 19 SPA + Azure CosmosDB Emulator

**Use Case:** Modern .NET full-stack apps, NoSQL databases, SPA frontends

**Architecture:**
```
┌────────────┐     ┌─────────────┐     ┌────────────┐
│  Angular   │     │  .NET API   │     │ CosmosDB   │
│  Port: 4200│────>│  Port: 5000 │────>│ Emulator   │
└────────────┘     └─────────────┘     └────────────┘
```

**AppHost/Program.cs:**
```csharp
var builder = DistributedApplication.CreateBuilder(args);

var cosmos = builder.AddAzureCosmosDB("cosmos")
                    .RunAsEmulator();
var db = cosmos.AddCosmosDatabase("recipesdb");

var api = builder.AddProject<Projects.Api>("api")
                 .WithReference(db)
                 .WithExternalHttpEndpoints();

var frontend = builder.AddNpmApp("frontend", "../frontend", "start")
       .WithReference(api)
       .WithHttpEndpoint(env: "PORT")
       .WithExternalHttpEndpoints();

builder.Build().Run();
```

**Run:** `cd samples/dotnet-angular-cosmos && aspire run`

<!-- CosmosDB emulator runs locally via Docker, no Azure account needed! -->

---

# Demo 5: .NET + Angular + CosmosDB — Code

**.NET API (`Program.cs`):**
```csharp
var builder = WebApplication.CreateBuilder(args);

builder.AddServiceDefaults();
builder.AddAzureCosmosClient("cosmos");

var app = builder.Build();

app.MapGet("/products", async (CosmosClient client) =>
{
    var container = client.GetDatabase("products")
                          .GetContainer("items");
    var query = container.GetItemQueryIterator<Product>("SELECT * FROM c");
    var products = new List<Product>();
    while (query.HasMoreResults)
        products.AddRange(await query.ReadNextAsync());
    return products;
});

app.Run();
```

**Angular component:**
```typescript
// products.component.ts
ngOnInit() {
  const apiUrl = environment.services__api__http__0;
  this.http.get(`${apiUrl}/products`).subscribe(data => {
    this.products = data;
  });
}
```

<!-- CosmosDB provides NoSQL document storage with global distribution (emulator for local dev). -->

---

# Demo 6: Go + Svelte — Bookmarks

**What:** Go HTTP server (via AddDockerfile) with Svelte frontend

**Use Case:** Any language via Docker, custom build processes

**Architecture:**
```
┌──────────┐     ┌──────────────┐
│  Svelte  │     │   Go API     │
│ Port: 5173│────>│  Port: 8080  │
└──────────┘     └──────────────┘
```

**apphost.go:**
```go
api, _ := builder.AddDockerfile("api", "./go-api")
api.WithHttpEndpoint(8080, "http")
api.WithExternalHttpEndpoints()

frontend, _ := builder.AddNpmApp("frontend", "./frontend", "dev")
frontend.WithEnvironment(
  "services__api__http__0", api.GetEndpoint("http"),
)
```

**Go AppHost orchestrates Go API via Dockerfile + Svelte via npm!**

<!-- AddDockerfile lets you run ANY language that you can containerize. -->

---

# Demo 6: Bookmarks — Dockerfile & Go Code

**Dockerfile:**
```dockerfile
FROM golang:1.23-alpine
WORKDIR /app
COPY . .
RUN go mod download
RUN go build -o main .
EXPOSE 8080
CMD ["./main"]
```

**Go API (`main.go`):**
```go
package main

import (
    "encoding/json"
    "net/http"
)

func main() {
    http.HandleFunc("/bookmarks", bookmarksHandler)
    http.ListenAndServe(":8080", nil)
}

func bookmarksHandler(w http.ResponseWriter, r *http.Request) {
    bookmarks := []string{"https://aspire.dev", "https://github.com"}
    json.NewEncoder(w).Encode(bookmarks)
}
```

**Run:** `cd samples/svelte-go-bookmarks && aspire run`

<!-- AddDockerfile is the escape hatch for any language Aspire doesn't support natively! -->

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

<!-- This is the most complex demo — it shows how Aspire shines with multiple languages. -->

---

# Demo 7: Polyglot Event Stream — Architecture

<div class="mermaid">
graph LR
    Producer[.NET Producer<br/>Port: 5000] -->|publish events| Kafka[Apache Kafka<br/>Port: 9092]
    Kafka -->|consume events| Consumer[Python Consumer]
    Consumer -->|processed data| Kafka
    Kafka -->|stream updates| Dashboard[Node.js Dashboard<br/>Port: 3000]
    User[User] -->|WebSocket| Dashboard
    
    style Producer fill:#512BD4
    style Consumer fill:#3776ab
    style Dashboard fill:#339933
    style Kafka fill:#231F20
</div>

**Event Flow:**
1. .NET producer generates events (metrics, logs, etc.) → publishes to Kafka
2. Python consumer processes events, enriches data → writes back to Kafka
3. Node.js dashboard streams processed events to browser via WebSocket

<!-- Kafka provides high-throughput event streaming across all three services! -->

---

# Demo 7: Polyglot Event Stream — AppHost

**AppHost/Program.cs:**
```csharp
var builder = DistributedApplication.CreateBuilder(args);

var kafka = builder.AddKafka("messaging")
                   .WithKafkaUI();

var producer = builder.AddProject<Projects.EventProducer>("producer")
                      .WithReference(kafka)
                      .WithExternalHttpEndpoints();

var consumer = builder.AddPythonApp("consumer", "../python-consumer", "main.py")
                      .WithReference(kafka);

var dashboard = builder.AddNpmApp("dashboard", "../node-dashboard", "start")
                       .WithReference(kafka)
                       .WithHttpEndpoint(env: "PORT")
                       .WithExternalHttpEndpoints();

builder.Build().Run();
```

**Run:** `cd samples/polyglot-event-stream && aspire run`

**All three services get:**
- `CONNECTIONSTRINGS__messaging` (Kafka broker URL)
- Service discovery for producer API
- Unified observability in Aspire dashboard

<!-- Look how simple the orchestration code is! Aspire handles all the complexity. -->

---

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

<!-- The producer doesn't wait for consumers. Kafka buffers all events. -->

---

# Demo 7: Event Stream — Python Consumer

**Consumer (`consumer.py`):**
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

<!-- The Python consumer enriches events with ML predictions, analytics, etc. -->

---

# Demo 7: Event Stream — Node.js Dashboard

**Dashboard (`server.js`):**
```javascript
const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const { Kafka } = require('kafkajs');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

const kafka = new Kafka({
  brokers: [process.env.CONNECTIONSTRINGS__messaging]
});

const consumer = kafka.consumer({ groupId: 'dashboard' });

(async () => {
  await consumer.connect();
  await consumer.subscribe({ topic: 'processed-events' });
  
  await consumer.run({
    eachMessage: async ({ message }) => {
      const event = JSON.parse(message.value.toString());
      io.emit('event', event);  // Push to WebSocket clients
    }
  });
})();

server.listen(3000);
```

**Run:** `cd samples/polyglot-event-stream/AppHost && dotnet run`

<!-- Three languages, real-time streaming via Kafka + WebSocket! -->

---

# Demo 7: Live Dashboard Experience

**In the Aspire Dashboard, you'll see:**

**Resources Tab:**
- `producer` (.NET) — Running, port 5000
- `consumer` (Python) — Running
- `dashboard` (Node.js) — Running, port 3000
- `messaging` (Kafka) — Running, port 9092

**Traces Tab:**
- Event flow: HTTP → .NET → Kafka → Python → Kafka → Node.js → WebSocket
- End-to-end latency visible!

**Logs Tab:**
- All services' logs in one place
- Filter by service, level, time range

**This is the power of Aspire!**

<!-- One unified view of everything, regardless of language. This is transformative. -->

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

## Aspire Dashboard Features Walkthrough

**Resources Tab** 📋
- List of all services, containers, executables
- Status: Running, Stopped, Exited
- Endpoints (clickable links)
- Source code location

**Console Logs Tab** 📜
- Raw stdout/stderr from each process
- Real-time streaming
- Filter by resource

**Structured Logs Tab** 📊
- Parsed JSON logs (if emitted as JSON)
- Filter by level, timestamp, message
- Search across all services

<!-- The dashboard is more powerful than just docker-compose logs. -->

---

## Aspire Dashboard Features (continued)

**Traces Tab** 🔍
- Distributed request traces
- Spans across services
- Waterfall visualization
- Click to see span details (tags, events, stack traces)

**Metrics Tab** 📈
- Request counts, durations
- CPU, memory usage
- Custom metrics (if instrumented)
- Time-series graphs

**Environment Tab** ⚙️
- All environment variables per service
- Connection strings
- Service discovery URLs
- Configuration values

<!-- The environment tab is great for debugging "why isn't this service connecting?" -->

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

## Health Check Endpoints

**Aspire can monitor health endpoints to determine service readiness:**

**Configure in AppHost:**
```csharp
builder.AddPythonApp("api", "../python", "app.py")
       .WithHttpEndpoint(port: 5000)
       .WithHttpHealthCheck("/health");
```

**Python Health Endpoint:**
```python
@app.route('/health')
def health():
    # Check database connection, dependencies, etc.
    try:
        db.execute('SELECT 1')
        return {'status': 'healthy'}, 200
    except Exception as e:
        return {'status': 'unhealthy', 'error': str(e)}, 503
```

**Dashboard shows:**
- 💚 Healthy
- ❤️ Unhealthy
- ⚠️ Degraded

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

## Deploying Polyglot Aspire Apps

**Local Development:**
```bash
aspire run
```

**Deploy to Azure Container Apps:**
```bash
aspire deploy           # production
aspire deploy -e test   # staging environment
```

**Publish artifacts (CI/CD):**
```bash
aspire publish           # generates deployment manifests
aspire publish compose   # Docker Compose output
```

**Aspire generates:**
- Container images for all services (Python, Node.js, .NET, Go, Java, etc.)
- Azure Container Apps / Kubernetes resources
- Azure infrastructure (Redis, PostgreSQL, etc.)
- Service connections and environment variables

**Same model, different environments** — local, staging, and production share one AppHost definition.

<!-- Aspire uses the same model for local dev and production. No code changes needed to deploy. -->

---

# <!--fit--> Wrap-Up & Resources

Bringing it all together

<!-- Let's wrap up with key takeaways and resources. -->

---

# Aspire Polyglot Cheat Sheet

<div class="columns3">
<div>

**Language/Framework**
- Python (ASGI/WSGI)
- Python (Uvicorn)
- Node.js app
- Vite/React frontend
- .NET project
- Go app
- Spring Boot (Java)
- Any Dockerfile

</div>
<div>

**Aspire Method**
```csharp
AddPythonApp()
```
```csharp
AddUvicornApp()
```
```csharp
AddNodeApp()
```
```csharp
AddViteApp()
```
```csharp
AddProject<T>()
```
```csharp
AddGolangApp()
```
```csharp
AddSpringApp()
```
```csharp
AddDockerfile()
```

</div>
<div>

**Common Patterns**
```csharp
.WithReference(redis)
.WaitFor(postgres)
.WithHttpEndpoint(env: "PORT")
.WithExternalHttpEndpoints()
.WithUv()  // Python pkg mgr
.WithNpm() // Node.js pkg mgr
.PublishWithContainerFiles()
.WithHttpHealthCheck("/health")
```

</div>
</div>

**Infrastructure:**
- Redis: `AddRedis("name")` — PostgreSQL: `AddPostgres("name").AddDatabase("db")`
- Kafka: `AddKafka("name")` — CosmosDB: `AddAzureCosmosDB("name").RunAsEmulator()`

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

## What's New in 13.2: Polyglot

<div class="columns">
<div>

**TypeScript AppHost** ✨
- Write your AppHost in TypeScript — no .NET SDK required
- Fluent async API with full type safety
- `[AspireExport]` attributes across all 40+ integrations
- Codegen produces typed SDK from .NET hosting packages
- Third-party integrations auto-discovered by attribute name

**Standalone CLI**
- Self-extracting `aspire` binary — no .NET prerequisite
- `aspire run` / `aspire deploy` / `aspire publish`
- `aspire certs` for certificate management
- `aspire doctor` checks only what your apphost language needs

</div>
<div>

**40+ Integration Exports** ✅
- PostgreSQL, Redis, SQL Server, MongoDB, MySQL
- Kafka, RabbitMQ, Nats, Valkey, Garnet
- Azure: Storage, KeyVault, ServiceBus, Functions, CosmosDB, AppInsights, EventHubs, SignalR, and more
- Seq, Qdrant, Milvus, Oracle, Yarp, OpenAI

**Developer Experience**
- Callback handling and `Action`-based dispatch
- Type-safe codegen for complex `AspireDto` types
- Improved error messages for missing `await`
- AssemblyLoadContext isolation for hosting
- Rebuild detection and hot reload for project resources

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

![bg right](./img/dotnet-logo.png)

# DEMOS

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

<!-- Needed for mermaid, can be anywhere in file except frontmatter -->
<script type="module">
  import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';
  mermaid.initialize({ startOnLoad: true });
</script>