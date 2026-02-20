# Project Context

- **Owner:** Chris Ayers (clayers@gmail.com)
- **Project:** Polyglot .NET Aspire samples — service orchestration across Python, JavaScript, Go, Java, C#, and mixed-language applications
- **Stack:** Python (Flask, Django), JavaScript (Vite, React, Svelte), Go, Java (Spring Boot), C# (ASP.NET Core, Angular), .NET Aspire, Docker, Redis, PostgreSQL, CosmosDB, Kafka
- **Created:** 2026-02-20

## Learnings

<!-- Append new learnings below. Each entry is something lasting about the project. -->

### 2026-02-20 — Full Architecture Review of All 7 Samples

**AppHost patterns in use:**
- **Python-only samples** (`flask-markdown-wiki`, `django-htmx-polls`): Use `apphost.py` (Python Aspire SDK) with `add_python_app()`, `with_http_endpoint(env="PORT")`, `with_external_http_endpoints()`. Minimal, single-service orchestration.
- **Standalone C# apphost** (`vite-react-api`, `svelte-go-bookmarks`, `spring-boot-postgres`): Use `apphost.cs` with `#:sdk Aspire.AppHost.Sdk@9.2.1` directive. Multi-service orchestration without full .csproj/.sln.
- **Full .NET project AppHost** (`dotnet-angular-cosmos`, `polyglot-event-stream`): Use `AppHost/` directory with `Program.cs` + `AppHost.csproj` + solution file. Include `ServiceDefaults/` project with OpenTelemetry, health checks, and service discovery.

**Key file paths and structures:**
- Python apphost pattern: `samples/<name>/apphost.py` + `.aspire/settings.json`
- Standalone CS apphost pattern: `samples/<name>/apphost.cs` + `.aspire/settings.json`
- Full .NET apphost pattern: `samples/<name>/AppHost/Program.cs` + `AppHost.csproj` + `ServiceDefaults/`
- All `.aspire/settings.json` files point to the apphost via `appHostPath`

**Aspire resource APIs demonstrated:**
- `AddPythonApp` — flask-markdown-wiki, django-htmx-polls, vite-react-api, polyglot-event-stream
- `AddNpmApp` — vite-react-api, svelte-go-bookmarks, dotnet-angular-cosmos, polyglot-event-stream
- `AddDockerfile` — svelte-go-bookmarks (Go), spring-boot-postgres (Java)
- `AddProject<T>` — dotnet-angular-cosmos, polyglot-event-stream
- `AddRedis` — vite-react-api
- `AddPostgres` + `WithPgAdmin` — spring-boot-postgres
- `AddAzureCosmosDB` + `RunAsEmulator` — dotnet-angular-cosmos
- `AddKafka` + `WithKafkaUI` — polyglot-event-stream

**Consistency observations:**
- All 7 samples have README.md files
- Health checks: Present in flask-markdown-wiki (`/health`), vite-react-api (`/api/health`), svelte-go-bookmarks (`/health`), dotnet-angular-cosmos (`/health` + `/alive`), polyglot-event-stream (all 3 services have `/health`). **Missing** from django-htmx-polls.
- Health endpoint path inconsistency: `/health` vs `/api/health` (vite-react-api).
- ServiceDefaults only in the two full .NET samples — identical code in both.
- `apphost.py` samples use `sys.path.insert(0, ...)` to load `.modules` — consistent pattern.
- Aspire SDK version `9.2.1` consistent across all samples.

**Architectural decisions encountered:**
- Each sample teaches exactly ONE primary Aspire pattern (good).
- Samples progress in complexity: single Python app → multi-service → Dockerfile integration → full .NET + cloud resources → polyglot event streaming.
- `svelte-go-bookmarks` uses manual `WithEnvironment("services__api__http__0", ...)` instead of `WithReference()` — intentional to demonstrate Dockerfile service wiring.

---

📌 **Team update (2026-02-20):** 
- Health endpoint standardization required across all samples (missing in `django-htmx-polls`, inconsistent path in `vite-react-api`)
- Aspire SDK version 9.2.1 must be updated across all samples simultaneously (no version drift)
- Aspire integration patterns (WithReference vs manual wiring, WithHttpEndpoint) need consistency review
— decided by Lando
