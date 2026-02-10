---
marp: true
theme: custom-default
footer: '@Chris_L_Ayers - https://chris-ayers.com'
---

<!-- _footer: 'https://github.com/codebytes/aspire-polyglot' -->

# <!--fit--> Polyglot Aspire

## Orchestrating Any Language with .NET Aspire
## Chris Ayers

![bg right](./img/dotnet-logo.png)

---

![bg left:40%](./img/portrait.png)

## Chris Ayers

### Senior Software Engineer<br>Azure CXP AzRel<br>Microsoft

<i class="fa-brands fa-bluesky"></i> BlueSky: [@chris-ayers.com](https://bsky.app/profile/chris-ayers.com)
<i class="fa-brands fa-linkedin"></i> LinkedIn: - [chris\-l\-ayers](https://linkedin.com/in/chris-l-ayers/)
<i class="fa fa-window-maximize"></i> Blog: [https://chris-ayers\.com/](https://chris-ayers.com/)
<i class="fa-brands fa-github"></i> GitHub: [Codebytes](https://github.com/codebytes)
<i class="fa-brands fa-mastodon"></i> Mastodon: [@Chrisayers@hachyderm.io](https://hachyderm.io/@Chrisayers)
~~<i class="fa-brands fa-twitter"></i> Twitter: @Chris_L_Ayers~~

---

# Agenda

<div class="columns">
<div>

**Part 1: Introduction** (10 min)
- What is .NET Aspire?
- The Polyglot Problem
- Key Aspire Concepts

**Part 2: Architecture** (10 min)
- How Aspire Works
- Service Discovery & Config
- OpenTelemetry Integration

</div>
<div>

**Part 3: Live Demos** (25 min)
- 8 Sample Applications
- Python, Node.js, Go, .NET
- Real-World Patterns

**Part 4: Observability** (10 min)
- Dashboard Deep Dive
- Distributed Tracing
- Deployment Strategies

**Part 5: Wrap-Up** (5 min)

</div>
</div>

<!-- This is a hands-on session. We'll see real code and live demos throughout. -->

---

# What is .NET Aspire?

**A cloud-native orchestration framework for building distributed applications**

- **Orchestration** — Start and manage multiple services
- **Service Discovery** — Connect services automatically
- **Observability** — Unified logs, traces, and metrics
- **Configuration** — Centralized environment management
- **Container-Ready** — Seamless Docker integration

**Key Insight:** Aspire isn't just for .NET apps — it can orchestrate **any language**!

<!-- Aspire provides the missing orchestration layer for local development and production. Think Docker Compose meets Kubernetes meets observability. -->

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
- Valkey
- MongoDB

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
var postgres = builder.AddPostgres("db");

builder.AddPythonApp("ml-service", "../python", "app.py")
       .WithReference(redis);

builder.AddNpmApp("frontend", "../react")
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
# 1. Install Aspire workload
dotnet workload install aspire

# 2. Create a new Aspire app (optional - or use existing)
aspire init

# 3. Run any sample
cd samples/flask-markdown-wiki
aspire run
```

**What happens:**
- All services start in correct order
- Dashboard opens at `http://localhost:15888`
- Connection strings auto-injected
- Logs/traces stream to dashboard

<!-- The dashboard URL may vary, but Aspire will open it in your browser automatically. -->

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
builder.AddPythonApp(
  "service", 
  "../src", 
  "main.py"
);
```

**Node.js**
```csharp
builder.AddNpmApp(
  "frontend",
  "../react",
  "dev"
);
```

</div>
<div>

**Dockerfile**
```csharp
builder.AddDockerfile(
  "go-api",
  "../go"
);
```

**Any Executable**
```csharp
builder.AddExecutable(
  "rust-cli",
  "cargo",
  "../rust",
  "run"
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
    AppHost --> NodeApp[Node.js Frontend]
    AppHost --> DotNetAPI[.NET API]
    AppHost --> GoService[Go Service]
    
    AppHost --> Valkey[Valkey]
    AppHost --> MongoDB[MongoDB]
    AppHost --> Kafka[Kafka]
    
    PythonApp --> MongoDB
    NodeApp --> Valkey
    DotNetAPI --> Valkey
    DotNetAPI --> MongoDB
    GoService --> Kafka
    
    PythonApp -.logs/traces.-> Dashboard
    NodeApp -.logs/traces.-> Dashboard
    DotNetAPI -.logs/traces.-> Dashboard
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

**Runs on:** `http://localhost:15888` (by default)

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
       .WithHealthCheck("/health");
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

8 real-world polyglot samples

<!-- Time to see Aspire in action! We'll walk through 8 samples that showcase different patterns. -->

---

# Demo 1: Python Web App — Markdown Wiki

**What:** Flask web app with SQLite database

**Use Case:** Python web services, simple CRUD apps, Markdown rendering

**Key Code (apphost.py):**
```python
from aspire import DistributedApplication

app = DistributedApplication()

wiki = app.add_python_app("wiki", "../src", "app.py") \
          .with_http_endpoint(port=5000)

app.build().run()
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
app.add_python_app("polls", "../src", "manage.py", "runserver", "0.0.0.0:8000") \
   .with_http_endpoint(port=8000)
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

# Demo 3: Flask + MongoDB — Blog

**What:** Flask blog with MongoDB database and Mongo Express admin UI

**Use Case:** Python + NoSQL databases, document storage, admin interfaces

**Architecture:**
```
┌─────────────┐         ┌──────────┐         ┌───────────────┐
│  Flask Blog │         │ MongoDB  │         │ Mongo Express │
│  Port: 5000 │ ───────>│ Port: 27017       │  Port: 8081   │
└─────────────┘         └──────────┘         └───────────────┘
```

**apphost.py:**
```python
mongo = app.add_mongodb("mongo") \
           .add_database("blog")

app.add_python_app("blog", "../src", "app.py") \
   .with_http_endpoint(port=5000) \
   .with_reference(mongo)
```

**Magic:** `add_mongodb()` provides MongoDB connection string automatically!

<!-- MongoDB is a popular choice for Python web apps. Aspire makes it trivial to add. -->

---

# Demo 3: Flask MongoDB Blog — Python Code

**Flask App (`app.py`):**
```python
from flask import Flask, request, render_template_string
from pymongo import MongoClient
import os

app = Flask(__name__)

# Aspire injects MongoDB connection string
mongo_conn = os.environ['CONNECTIONSTRINGS__blog']
client = MongoClient(mongo_conn)
db = client.blog

@app.route('/')
def index():
    posts = list(db.posts.find().sort('created_at', -1))
    return render_template_string(blog_template, posts=posts)

@app.route('/post', methods=['POST'])
def create_post():
    db.posts.insert_one({
        'title': request.form['title'],
        'content': request.form['content'],
        'created_at': datetime.now()
    })
    return redirect('/')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
```

**Run:** `cd samples/flask-mongo-blog && aspire run`

<!-- MongoDB document model is perfect for blogs with flexible schemas. -->

---

# Demo 4: Hono.js + Valkey — Rate Limiter

**What:** Hono.js API with Valkey-backed sliding window rate limiter

**Use Case:** Node.js services, API rate limiting, Redis-compatible caching

**Architecture:**
```
┌─────────────┐         ┌─────────┐
│  Hono.js    │         │  Valkey │
│  Port: 3000 │ ───────>│ Port: 6379
└─────────────┘         └─────────┘
```

**apphost.cs:**
```csharp
var valkey = builder.AddValkey("cache");

builder.AddNpmApp("api", "../src", "dev")
       .WithHttpEndpoint(port: 3000)
       .WithReference(valkey);
```

**Magic:** `AddValkey()` provides Valkey (Redis fork) connection string!

<!-- Valkey is the open-source Redis fork. Hono.js is a fast Node.js framework. -->

---

# Demo 4: Hono Rate Limiter — Node.js Code

**Hono.js App (`index.ts`):**
```typescript
import { Hono } from 'hono';
import { createClient } from 'redis';

const app = new Hono();

// Aspire injects Valkey connection string
const valkeyUrl = `redis://${process.env.CONNECTIONSTRINGS__cache}`;
const redis = createClient({ url: valkeyUrl });
await redis.connect();

// Sliding window rate limiter middleware
app.use('*', async (c, next) => {
  const ip = c.req.header('x-forwarded-for') || 'unknown';
  const key = `ratelimit:${ip}`;
  
  const count = await redis.incr(key);
  if (count === 1) await redis.expire(key, 60); // 60 second window
  
  if (count > 100) return c.json({ error: 'Rate limit exceeded' }, 429);
  
  await next();
});

app.get('/api/data', (c) => c.json({ data: 'success' }));
app.listen(3000);
```

<!-- Sliding window rate limiting is a common API protection pattern. -->

---

# Demo 4: Hono Rate Limiter — Run It

**Commands:**
```bash
cd samples/hono-valkey-ratelimiter
aspire run
```

**What you'll see:**
1. Valkey container starts first
2. Node.js (Hono) dev server starts
3. Dashboard shows both resources
4. API endpoint at `http://localhost:3000`

**Test the rate limiter:**
```bash
# Works fine (within limit)
curl http://localhost:3000/api/data

# Spam requests (will hit rate limit)
for i in {1..150}; do curl http://localhost:3000/api/data; done
# After ~100 requests, you'll see: {"error":"Rate limit exceeded"}
```

**Valkey tracks request counts** per IP with sliding window!

<!-- Rate limiting prevents API abuse and protects your backend services. -->

---

# Demo 5: FastAPI + Celery — Background Reports

**What:** FastAPI API with Celery workers processing background tasks via Redis

**Use Case:** Async job processing, background workers, task queues

**Architecture:**
```
┌───────────┐     ┌─────────┐     ┌──────────────┐
│ FastAPI   │     │  Redis  │     │ Celery Worker│
│ Port: 8000│────>│ Port: 6379<────│  (no HTTP)   │
└───────────┘     └─────────┘     └──────────────┘
```

**apphost.py:**
```python
redis = app.add_redis("broker")

app.add_python_app("api", "../api", "main.py") \
   .with_http_endpoint(port=8000) \
   .with_reference(redis)

app.add_python_app("worker", "../worker", "celery_worker.py") \
   .with_reference(redis)
```

<!-- Both Python processes share the same Redis broker via Aspire! -->

---

# Demo 5: Celery Reports — FastAPI Code

**FastAPI App (`main.py`):**
```python
from fastapi import FastAPI
from celery_app import generate_report_task
import os

app = FastAPI()

@app.post('/generate-report')
async def generate_report(report_type: str):
    # Enqueue task asynchronously
    task = generate_report_task.delay(report_type)
    return {'task_id': task.id, 'status': 'queued'}

@app.get('/report/{task_id}')
async def get_report_status(task_id: str):
    task = generate_report_task.AsyncResult(task_id)
    if task.ready():
        return {'status': 'completed', 'result': task.result}
    return {'status': task.state}

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host='0.0.0.0', port=8000)
```

**Run:** `cd samples/python-celery-reports && aspire run`

<!-- FastAPI returns immediately; Celery processes in background. -->

---

# Demo 6: .NET + Angular + CosmosDB

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
                    .RunAsEmulator()
                    .AddDatabase("products");

var api = builder.AddProject<Projects.ProductApi>("api")
                 .WithReference(cosmos);

builder.AddNpmApp("frontend", "../frontend", "start")
       .WithReference(api);

builder.Build().Run();
```

**Run:** `cd samples/dotnet-angular-cosmos/AppHost && dotnet run`

<!-- CosmosDB emulator runs locally via Docker, no Azure account needed! -->

---

# Demo 6: .NET + Angular + CosmosDB — Code

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

# Demo 7: Go + Svelte — Bookmarks

**What:** Go HTTP server (via AddDockerfile) with Svelte frontend

**Use Case:** Any language via Docker, custom build processes

**Architecture:**
```
┌──────────┐     ┌──────────────┐
│  Svelte  │     │   Go API     │
│ Port: 5173│────>│  Port: 8080  │
└──────────┘     └──────────────┘
```

**apphost.cs:**
```csharp
var api = builder.AddDockerfile("api", "../go-api")
                 .WithHttpEndpoint(port: 8080);

builder.AddNpmApp("frontend", "../frontend", "dev")
       .WithReference(api);
```

**Go isn't natively supported, so we use AddDockerfile!**

<!-- AddDockerfile lets you run ANY language that you can containerize. -->

---

# Demo 7: Bookmarks — Dockerfile & Go Code

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

# Demo 8: Polyglot Event Stream

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

# Demo 8: Polyglot Event Stream — Architecture

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

# Demo 8: Polyglot Event Stream — AppHost

**AppHost/Program.cs:**
```csharp
var builder = DistributedApplication.CreateBuilder(args);

var kafka = builder.AddKafka("messaging");

var producer = builder.AddProject<Projects.EventProducer>("producer")
                      .WithReference(kafka);

var consumer = builder.AddPythonApp("consumer", "../consumer", "consumer.py")
                      .WithReference(kafka);

var dashboard = builder.AddNpmApp("dashboard", "../dashboard", "dev")
                       .WithReference(kafka)
                       .WithReference(producer);

builder.Build().Run();
```

**Run:** `cd samples/polyglot-event-stream/AppHost && dotnet run`

**All three services get:**
- `CONNECTIONSTRINGS__messaging` (Kafka broker URL)
- Service discovery for producer API
- Unified observability in Aspire dashboard

<!-- Look how simple the orchestration code is! Aspire handles all the complexity. -->

---

# Demo 8: Event Stream — .NET Producer

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

# Demo 8: Event Stream — Python Consumer

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

# Demo 8: Event Stream — Node.js Dashboard

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

# Demo 8: Live Dashboard Experience

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
       .WithHealthCheck("/health");
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
dotnet run  # or aspire run
```

**Azure Container Apps (recommended):**
```bash
# Install Azure Developer CLI
azd init
azd up
```

**Aspire generates:**
- Container images for all services (Python, Node.js, .NET, etc.)
- Azure Container Apps resources
- Azure infrastructure (Redis, PostgreSQL, etc.)
- Service connections and environment variables

**Alternative: aspirate (community tool):**
```bash
dotnet tool install -g aspirate
aspirate generate
kubectl apply -f aspirate-output/
```

Generates Kubernetes manifests!

<!-- Aspire isn't just for local dev. It deploys to production too! -->

---

# <!--fit--> Wrap-Up & Resources

Bringing it all together

<!-- Let's wrap up with key takeaways and resources. -->

---

# Aspire Polyglot Cheat Sheet

<div class="columns3">
<div>

**Language/Framework**
- Python script
- Python web app
- Node.js app
- .NET project
- Go / Rust / Java
- Any executable

</div>
<div>

**Aspire Method**
```csharp
AddPythonApp()
```
```csharp
AddPythonApp()
```
```csharp
AddNpmApp()
```
```csharp
AddProject<T>()
```
```csharp
AddDockerfile()
```
```csharp
AddExecutable()
```

</div>
<div>

**Connection Pattern**
```csharp
.WithReference(redis)
```
```csharp
.WithReference(db)
```
```csharp
.WithReference(api)
```
```csharp
.WithReference(redis)
```
```csharp
.WithReference(postgres)
```
```csharp
.WithReference(rabbit)
```

</div>
</div>

**Infrastructure:**
- Valkey: `AddValkey("name")` (Redis fork)
- MongoDB: `AddMongoDB("name").AddDatabase("db")`
- Kafka: `AddKafka("name")`
- CosmosDB: `AddAzureCosmosDB("name").RunAsEmulator()`
- Redis: `AddRedis("name")` (for Celery sample)

<!-- Keep this slide handy as a quick reference! -->

---

## Resources & Links

**Official Docs:**
- 🌐 [aspire.dev](https://aspire.dev) — Official website
- 📖 [learn.microsoft.com/dotnet/aspire](https://learn.microsoft.com/dotnet/aspire) — Docs
- 🐙 [github.com/dotnet/aspire](https://github.com/dotnet/aspire) — Source code

**Sample Code:**
- 🐙 [github.com/codebytes/aspire-polyglot](https://github.com/codebytes/aspire-polyglot) — This repo!

**Community:**
- 💬 [Discord: .NET Aspire channel](https://aka.ms/dotnet-discord)
- 🐦 [Twitter: #dotnetaspire](https://twitter.com/search?q=%23dotnetaspire)

**Tools:**
- [aspirate](https://github.com/prom3theu5/aspirate) — Kubernetes deployment
- [azd](https://learn.microsoft.com/azure/developer/azure-developer-cli/) — Azure deployment

<!-- Join the community! Aspire is open source and very active. -->

---

## What's Next: Aspire Roadmap

**Current (Aspire 8.0):**
- Python, Node.js support
- Dockerfile support (any language)
- Azure deployment via azd
- Kubernetes via aspirate

**Coming Soon:**
- More language integrations (Java, Ruby)
- Better IDE support (Visual Studio, VS Code)
- Production dashboard (not just local)
- Service mesh integration
- More cloud providers (AWS, GCP)

**Community Contributions Welcome!**

GitHub: [github.com/dotnet/aspire](https://github.com/dotnet/aspire)

<!-- Aspire is evolving rapidly. The roadmap is community-driven. -->

---

# Key Takeaways

**Aspire orchestrates ANY language** — Python, Node.js, Go, Rust, Java, not just .NET

**Unified observability** — One dashboard for logs, traces, metrics across all services

**Automatic service discovery** — No hardcoded URLs via environment variables

**Connection string injection** — Redis, PostgreSQL, RabbitMQ connections auto-wired

**Simple AppHost code** — 10 lines to orchestrate 5 services

**Deploy to production** — Azure, Kubernetes, anywhere containers run

**Open source** — MIT license, community-driven

**Start today:** `dotnet workload install aspire`

<!-- Aspire solves real problems: local dev complexity, observability fragmentation, deployment. -->

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
- [Aspire Docs](https://learn.microsoft.com/en-us/dotnet/aspire/get-started/aspire-overview)
- [Aspire Samples](https://github.com/dotnet/aspire-samples)
- [Polyglot Aspire Samples](https://github.com/codebytes/aspire-polyglot)
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
