# Project Context

- **Owner:** Chris Ayers (clayers@gmail.com)
- **Project:** Polyglot .NET Aspire samples — service orchestration across Python, JavaScript, Go, Java, C#, and mixed-language applications
- **Stack:** Python (Flask, Django), JavaScript (Vite, React, Svelte), Go, Java (Spring Boot), C# (ASP.NET Core, Angular), .NET Aspire, Docker, Redis, PostgreSQL, CosmosDB, Kafka
- **Created:** 2026-02-20

## Learnings

<!-- Append new learnings below. Each entry is something lasting about the project. -->

### 2026-02-20 — Initial Backend Review Across All 7 Samples

**Architecture patterns observed:**
- Two Aspire integration styles: Python-based `apphost.py` (Flask wiki, Django polls) vs C#-based `apphost.cs` / full `.csproj` AppHost projects (the rest). Both are valid Aspire patterns.
- All non-.NET backends use `PORT` env var for Aspire HTTP endpoint binding.
- Connection strings follow Aspire convention: `ConnectionStrings__<resource_name>`.
- Two Dockerfiles in the repo: Go bookmarks API and Spring Boot Notes API. Both use multi-stage builds.
- .NET samples (dotnet-angular-cosmos, polyglot-event-stream) use the standard ServiceDefaults pattern with OpenTelemetry, health checks, and service discovery.
- Non-.NET samples roll their own `/health` endpoints manually.

**Key file paths:**
- `samples/flask-markdown-wiki/src/main.py` — Flask wiki, SQLite, inline HTML templates
- `samples/django-htmx-polls/src/` — Django polls app with HTMX, Waitress, SQLite
- `samples/vite-react-api/src/api/main.py` — FastAPI todo API with Redis backend
- `samples/svelte-go-bookmarks/go-api/main.go` — Go REST API, in-memory store
- `samples/spring-boot-postgres/src/` — Spring Boot Notes API with JPA/PostgreSQL
- `samples/dotnet-angular-cosmos/Api/Program.cs` — ASP.NET Core minimal API with Cosmos DB
- `samples/polyglot-event-stream/EventProducer/Program.cs` — C# Kafka producer
- `samples/polyglot-event-stream/python-consumer/main.py` — Python Kafka consumer with Flask
- `samples/polyglot-event-stream/node-dashboard/app.js` — Node.js Express Kafka dashboard

**Findings and conventions:**
- Go API has a hardcoded port `:8080` — does not read from `PORT` env var, unlike all other services.
- Flask wiki uses `render_template_string` with inline HTML — works for demo but unconventional.
- Django polls has a race condition on vote increment (`choice.votes += 1` without F() expression).
- Spring Boot connection string env var uses Aspire naming (`ConnectionStrings__notesdb`) which Spring resolves specially.
- FastAPI creates Redis connection at module-level — will crash on import if Redis isn't available.
- Cosmos DB API has solid CRUD with proper 404 handling via CosmosException pattern matching.
- Go API CORS uses wildcard `*` — acceptable for demos but should be noted.
- Python consumer Kafka thread has no reconnection logic.

---

📌 **Team update (2026-02-20):**
- Django vote endpoint must use F() expression for atomic increment (race condition)
- Go API must read PORT from environment instead of hardcoding :8080 (consistency)
- Python dependencies in flask-markdown-wiki and vite-react-api need pinning (reproducibility)
- Flask wiki has XSS vulnerability via |safe without sanitization (bleach needed)
— decided by Wedge, Bossk
