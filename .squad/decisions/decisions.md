# Decision: Java Connection String Format & Properties

**Author:** Thor (Java Dev)  
**Date:** 2026-03-22  
**Status:** RESOLVED  

## Summary

Aspire does NOT need a connection string parser for Java. When `.withReference(db)` is used, Aspire automatically injects individual connection properties including a ready-to-use JDBC connection string as a separate environment variable.

## Problem Statement

The Aspire-provided `ConnectionStrings__notesdb` is in .NET semicolon-delimited format (`Host=...;Port=...;Username=...;Password=...;Database=notesdb`), not JDBC format. Spring Boot cannot use this directly.

## Solution

**No code changes required.** Use the Aspire-injected properties:

```properties
server.port=${PORT:8080}
spring.datasource.url=${NOTESDB_JDBCCONNECTIONSTRING:jdbc:postgresql://localhost:5432/notesdb}
spring.datasource.username=${NOTESDB_USERNAME:postgres}
spring.datasource.password=${NOTESDB_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Key Findings

| Environment Variable | Value | Format |
|---|---|---|
| `NOTESDB_JDBCCONNECTIONSTRING` | `jdbc:postgresql://<host>:<port>/notesdb` | JDBC format |
| `NOTESDB_USERNAME` | `<username>` | From `GetConnectionProperties()` |
| `NOTESDB_PASSWORD` | `<password>` | From `GetConnectionProperties()` |
| `NOTESDB_HOST` | `<hostname>` | Individual property |
| `NOTESDB_PORT` | `<port>` | Individual property |

The JDBC connection string does NOT include credentials; username and password must be provided separately.

## Verification

- All 5 research questions answered with source evidence
- Test file: `tests/Aspire.Hosting.PostgreSQL.Tests/ConnectionPropertiesTests.cs` verifies these properties
- Applied to: spring-boot-postgres sample (application.properties, demo slide, README)

---

# Decision: .NET 10 + Aspire 13.1.3 Upgrade

**Author:** Rogers (C#/.NET Dev)  
**Date:** 2026-03-22  
**Status:** Implemented  

## Summary

Upgraded both C# samples (dotnet-angular-cosmos, polyglot-event-stream) from .NET 9 / Aspire 9.5.2 to .NET 10 / Aspire 13.1.3. This is a major framework upgrade spanning two .NET major versions and four Aspire major versions.

## Changes Made

### All 6 .csproj files
- `<TargetFramework>` net9.0 → net10.0
- Aspire.AppHost.Sdk 9.5.2 → 13.1.3
- All `Aspire.*` packages → 13.1.3
- `Microsoft.Extensions.Http.Resilience` 9.10.0 → 10.4.0
- `Microsoft.Extensions.ServiceDiscovery` 9.5.2 → 10.4.0

### Breaking Changes Encountered and Fixed
1. **Package rename**: `Aspire.Hosting.NodeJs` → `Aspire.Hosting.JavaScript` — the old package stops at 9.5.2
2. **API rename**: `AddNpmApp()` → `AddJavaScriptApp()` — same signature, new method name
3. **NoWarn cleanup**: Removed `ASPIREHOSTINGNODEJS001` suppression (obsolete warning code)

### No Changes Required
- OpenTelemetry packages (already at 1.15.x — latest)
- Microsoft.Azure.Cosmos (already at 3.58.0 — latest)
- No other deprecated APIs found (WithSecretBuildArg, AIFoundry not used)

### README Updates
- Fixed code snippets: `AddNpmApp` → `AddJavaScriptApp` in both sample READMEs
- Updated version reference: "Aspire 9.5.2" → "Aspire 13.1.3" in dotnet-angular-cosmos README

## Verification
- `dotnet restore` ✅ both samples
- `dotnet build` ✅ both samples — 0 warnings, 0 errors

## Impact on Team
- **Slide deck**: References to Aspire versions and API names (AddNpmApp) may need updating (Strange/Scribe)
- **Polyglot samples**: Other AppHosts (Python, Go, Java, JS) are unaffected — they don't use .NET
- **NuGet.config decision (#7)**: Still pending — not affected by this upgrade

---

# Decision: vite-react-api TypeScript AppHost Migration

**Author:** Parker (JS/TS Dev)  
**Date:** 2026-03-22  
**Status:** ✅ COMPLETED

## Context

The `vite-react-api` sample used a Python AppHost (`apphost.py`) to orchestrate a Vite+React frontend, FastAPI backend, and Redis cache. Since the frontend is JavaScript/TypeScript and Aspire 13.2 has production-ready TypeScript AppHost support, a TS AppHost is a better fit and showcases the polyglot story more effectively.

## Decision

Migrate vite-react-api from Python AppHost to TypeScript AppHost.

## Changes Made

1. **Created `apphost.ts`** — TypeScript AppHost using `createBuilder()` from `.modules/aspire.js`
2. **Created `package.json`** — Node.js project with `vscode-jsonrpc`, `tsx`, `typescript` deps
3. **Created `tsconfig.json`** — ES2022/NodeNext configuration
4. **Updated `aspire.config.json`** — `language: "typescript/nodejs"`, removed Python experimental flags and package refs
5. **Removed Python scaffolding** — `apphost.py`, `requirements.txt`, `uv-install.py`, `.venv/`, Python `.modules` files
6. **Updated READMEs** — Both sample and root README now reference `apphost.ts`

## Validation

- `aspire restore` — SDK codegen succeeded ✅
- `aspire run` — Dashboard launched, resources registered ✅

## Impact

- **Root README**: Updated AppHost column for vite-react-api row from `apphost.py` → `apphost.ts`
- **Slides**: May need updating if demo code references `apphost.py` for vite-react-api
- **Decision #1 (Config Migration)**: Format unchanged; only language/path values changed
- **No Python dependency needed** for this sample anymore (no `uv`, no `.venv`)

## Owner

Parker (JS/TS Dev) — ✅ COMPLETED

---

# Decision: TypeScript Starter Sample (`ts-starter`)

**Author:** Parker (JS/TS Dev)  
**Date:** 2026-03-22  
**Status:** ✅ COMPLETED

## Context

Chris requested a new TypeScript starter sample using the official Aspire TS template (`aspire-ts-starter`). This demonstrates that the AppHost itself can be TypeScript — a first-class citizen in Aspire 13.2+.

## Decision

Scaffolded the sample using `aspire new aspire-ts-starter --channel daily` instead of the stable channel.

**Why daily?** The stable template references `addNodeApp` and `addViteApp` APIs that only exist in `Aspire.Hosting.JavaScript@13.3.0-preview.1`, not in the stable `13.1.3`. This is a known template-SDK version mismatch that will resolve when 13.3.0 goes stable.

## What Was Created

- `/samples/ts-starter/` — full working sample
- `apphost.ts` — TypeScript AppHost using `addNodeApp` + `addViteApp`
- `api/` — Express API with weather forecast endpoint + OpenTelemetry
- `frontend/` — React 19 + Vite frontend
- `README.md` — documentation following repo sample pattern

## Validation

- `aspire restore` — SDK code generated ✅
- `aspire run` — dashboard launched, both services started ✅
- Clean stop ✅

## Risk

- Uses preview SDK (`13.3.0-preview.1`) — may need version bump when stable 13.3.0 ships
- Template-generated code is untouched; any upstream fixes will require re-scaffold or manual patch

## Impact

- Adds the simplest possible "TypeScript-all-the-way" Aspire sample to the repo
- Complements `vite-react-api` (which uses TypeScript AppHost with Dockerfiles) by showing the native `addNodeApp`/`addViteApp` developer experience

---

# Decision: Major Slide Deck Restructure — Polyglot-First Narrative

**Author:** Strange (Content Dev)  
**Date:** 2026-03-22  
**Status:** ✅ COMPLETED

## Context

Chris requested a major restructure of `slides/Slides.md`. The existing deck spent too much time on generic "What is Aspire?" content and 4 slides of 13.2 release notes that read like a changelog. The audience already knows what Aspire is — they need to hear the **polyglot story**.

## Decision

Restructured the entire deck from "Aspire intro + 13.2 changelog" into a 5-act polyglot narrative:

1. **Act 1: The Problem** (2 slides) — Title, speaker intro, "The Polyglot Problem" with Docker Compose pain points
2. **Act 2: Aspire as the Polyglot Answer** (5 slides) — One orchestrator, AppHost in C# vs TypeScript, Five AppHost languages, aspire.config.json
3. **Act 3: How It Works** (5 slides) — Service discovery, connection strings, dashboard, CLI, aspire new
4. **Act 4: Demos** (9 slides for 8 samples) — Simple → complex, one slide per sample
5. **Act 5: Wrap-Up** (3 slides) — Takeaways, resources, questions
6. **Appendix** — Cheat sheet, OTel setup, lifecycle, deployment, commented-out detailed walkthroughs

## What Was Removed

- "What is Aspire?" generic slide (audience knows)
- "Key Aspire Concepts" 6-box overview (too basic)
- 4 separate "What's New in 13.2" slides — content woven into narrative instead
- "Observability & Service Discovery" deep dive section (6 slides of redundant content)

## What Was Added

- "Five AppHost Languages" slide showing Python, TypeScript, Go, Java, C# side by side
- Demo 1: ts-starter (TypeScript AppHost template — new sample)
- Combined dashboard slide with inline cross-language trace example
- `aspire new` language-aware scaffolding slide
- Demo 4 updated to show TypeScript AppHost (vite-react-api converted from Python)

## What Was Preserved

- Marp frontmatter and custom-aspire-light theme
- All speaker notes (HTML comments)
- All image references (9 images, all verified)
- Appendix: commented-out detailed demo walkthroughs (unchanged)
- Cheat sheet content (moved to appendix)

## Impact

- **Before:** ~33 active slides with redundant content, ~30 appendix slides
- **After:** 25 focused slides + 8 appendix slides
- **Pacing:** Better fit for 30-45 min talk with demos
- **13.2 coverage:** Still comprehensive, but woven into the polyglot narrative where each feature naturally belongs

## Team Impact

- Demo slide code for vite-react-api now shows TypeScript AppHost — verified by Stark to match converted sample
- ts-starter demo added — verified by Stark to match scaffolded sample
- All other demo AppHost code verified accurate against actual samples

---

# Decision: Slide Deck Polish Pass & Accuracy Review

**Reviewer:** Stark (Lead)  
**Date:** 2026-03-22  
**Verdict:** ✅ APPROVED — Ready for 30-minute delivery

## Summary

Complete review and polish of `slides/Slides.md` for conference talk delivery. Found and fixed significant code accuracy issues across all polyglot demo slides.

## Critical Fixes Applied

### 1. Demo Code Accuracy (ALL 8 DEMOS)

**Root cause:** Slides were written using C# SDK API names, but polyglot SDKs (Python, Go, Java) only have low-level primitives (`add_container`, `add_dockerfile`). TypeScript SDK has some typed helpers (`addNodeApp`, `addViteApp`) but not all.

**All 8 demo slides updated to match actual sample code:**

| Demo | Fix |
|------|-----|
| ts-starter | `addViteApp("frontend", "./frontend")` (not `addNpmApp`) |
| flask-markdown-wiki | `add_container("cache", "redis:latest")`, `add_dockerfile()` (not idealized APIs) |
| django-htmx-polls | `add_container("pg", "postgres:latest")`, `add_dockerfile()` (not idealized APIs) |
| vite-react-api | `addContainer()`, `addDockerfile()` for all 3 services (not `addRedis`, `addPythonApp`, `addNpmApp`) |
| spring-boot-postgres | `addContainer("pg", "postgres:16")`, manual env vars (not `addPostgres` + `.withReference`) |
| svelte-go-bookmarks | `AddContainer()`, `AddDockerfile()`, `AddExecutable()` (not `AddPostgres`, `AddNpmApp`) |
| dotnet-angular-cosmos | `AddJavaScriptApp()` (not `AddNpmApp`) |
| polyglot-event-stream | `AddJavaScriptApp()` (not `AddNpmApp`) |

### 2. aspire.config.json Example
- Version was "9.5.2" with non-existent "channel" field
- Fixed to "13.2.0", removed "channel"

### 3. Connection Strings Slide
- Java example updated to explicit env var pattern

### 4. Five Languages Slide
- Python example updated to `add_dockerfile()` (actual API)
- Added qualifier: "C# has the richest typed integrations; other SDKs use addDockerfile/addContainer"

### 5. Cheat Sheet
- Removed non-existent `AddGolangApp()` and `AddSpringApp()`
- Added actual methods: `AddJavaScriptApp()`, `AddExecutable()`

### 6. TypeScript Debugging
- Added speaker note about Aspire 13.2 VS Code extension improvements

## Timing Verification

| Act | Content | Est. Time |
|-----|---------|-----------|
| 1 - Problem | 1 slide | ~3 min |
| 2 - Aspire's Answer | 5 slides | ~5 min |
| 3 - How It Works | 6 slides | ~5 min |
| 4 - Demos | 9 slides | ~15 min |
| 5 - Wrap-Up | 4 slides | ~2 min |
| **Total** | **~26 slides** | **~30 min** |

✅ Timing is tight and appropriate.

## Outstanding Items

1. **Theme decision (#4):** Still using `custom-aspire-light` vs charter's `custom-default`. Pending Chris's approval.
2. **Commented-out demo slides:** Still contain old API references. Not a problem since they don't render, but if uncommented, they'll need the same accuracy pass.

## Architecture Note

The key insight: **polyglot SDK maturity varies significantly.** C# SDK is rich and typed; Python/Go/Java SDKs use `add_container` + `add_dockerfile` for everything. TypeScript SDK is in between. Slides now honestly reflect this, showing what works TODAY rather than aspirational APIs that don't exist yet.
