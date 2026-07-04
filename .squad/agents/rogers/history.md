# Rogers — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** C#, .NET, ASP.NET Core, Angular 19, CosmosDB, Aspire
- **Primary samples:** dotnet-angular-cosmos, C# parts of polyglot-event-stream, C# AppHosts

## Core Context

**Baseline (2025-07-18):** Both samples on Aspire 9.2.1; identified version lag (13.1.3 available), key 13.x API changes (NodeJs→JavaScript, AddNpmApp→AddJavaScriptApp), and .NET version opportunities.

**Aspire 9.5.2 Upgrade (2026-03-22):** All 11 NuGet references upgraded across 6 .csproj files. Fixed dotnet-angular-cosmos README (AddDatabase→AddCosmosDatabase). Zero breaking changes.

**Major Upgrade: .NET 10 + Aspire 13.1.3 (2026-03-22):** Framework jumped net9.0→net10.0, all Aspire packages 9.5.2→13.1.3 (11 refs across 4 files). Breaking changes: NodeJs→JavaScript package rename, AddNpmApp→AddJavaScriptApp API. NoWarn cleanup (ASPIREHOSTINGNODEJS001). Both samples build clean.

**Config & SDK Upgrades (2026-03-23 through 2026-07-02):** Attempted 13.2.0 upgrade (blocked — not published), all samples aligned to 13.4.6 via team coordination. Aspire 13.x feature: aspire.config.json (samples/*/aspire.config.json, not .aspire/settings.json). .gitignore updated: **/.aspire/integrations/, samples/svelte-go-bookmarks/apphost (Go binary), **/.aspire/modules/.codegen-hash.

## Learnings

### 2026-07-04 — Angular Proxy Pattern & macOS Port 5000 Gotcha

- **Framework**: Projects remain on net10.0 (no change needed)
- **Aspire SDK**: Upgraded `Aspire.AppHost.Sdk` from 13.2.4 → 13.4.6 (both AppHosts)
- **Aspire packages**: All 11 Aspire.* package references upgraded 13.2.4 → 13.4.6 across 4 .csproj files
- **Breaking changes**: NONE — pure version bump, no API changes required
- **Files updated**:
  - `samples/dotnet-angular-cosmos/AppHost/AppHost.csproj` (SDK + 2 packages)
  - `samples/dotnet-angular-cosmos/Api/Api.csproj` (1 package)
  - `samples/polyglot-event-stream/AppHost/AppHost.csproj` (SDK + 3 packages)
  - `samples/polyglot-event-stream/EventProducer/EventProducer.csproj` (1 package)
- **NuGet cache cleanup**: Untracked `samples/polyglot-event-stream/.nugetpackages/` (stale 13.2.0 cache, ~3169 files) via `git rm -r --cached`; kept on disk per .gitignore rule
- **Build verification**: Both samples build clean (0 warnings, 0 errors)
  - dotnet-angular-cosmos: Build succeeded (7.67s)
  - polyglot-event-stream: Build succeeded (4.10s)
- **Key learnings**:
  - Aspire 13.4.6 is a drop-in replacement for 13.2.4 (no code changes required)
  - All Aspire.Hosting.* packages exist at exactly 13.4.6 on NuGet
  - ServiceDefaults projects untouched (no Aspire packages)
  - .NET 10 SDK 10.0.301 handles 13.4.6 without issues

### 2026-07-02 — Aspire 13.4.6 .gitignore Hygiene

- **New Aspire CLI artifacts** requiring .gitignore rules:
  - `**/.aspire/integrations/` — Transient per-sample restore/package caches (6 samples: vite-react-api, ts-starter, django-htmx-polls, flask-markdown-wiki, spring-boot-postgres, svelte-go-bookmarks)
  - `samples/svelte-go-bookmarks/apphost` — Compiled Go binary (4MB Mach-O, built output of apphost.go)
- **Rules added to .gitignore**: Both rules placed in Aspire section
- **Important**: Repo-root `.aspire/settings.json` remains tracked (not a build artifact)
- **Verification**: `git status --short` clean for all new artifacts; apphost.go source still tracked
- **Key learning**: Aspire 13.4+ CLI now generates per-sample integration caches during restore operations

### 2026-07-02 — Aspire 13.4.6 Binding Location Migration (.gitignore)

- **Context**: Go/Python/Java samples migrating generated Aspire bindings from `.modules/` → `.aspire/modules/` (Aspire 13.4.6+ pattern)
- **Rule added**: `**/.aspire/modules/.codegen-hash` — mirrors existing `**/.modules/.codegen-hash` rule
- **Important**: `.aspire/modules/` directory itself MUST remain tracked (same as `.modules/`) — only .codegen-hash is transient
- **Comment updated**: Clarified split between TypeScript samples (keep `.modules/`) and Go/Python/Java samples (new `.aspire/modules/`)
- **Philosophy preserved**: Generated bindings tracked in git so `aspire run` works out of the box without rebuild
- **Key learning**: Aspire 13.4.6 introduces new binding layout while maintaining backward compatibility for TypeScript samples

### 2026-07-04 — Angular Proxy Pattern & macOS Port 5000 Gotcha

**Context**: Coordinator diagnosed and fixed "can't add ingredients/recipes" in dotnet-angular-cosmos sample.

**Root Cause**: Hard-coded proxy target `http://localhost:5000` in `proxy.conf.json`. On macOS, port 5000 is occupied by AirPlay Receiver (Control Center, server AirTunes/950.7.1). The Aspire AppHost assigns dynamic ports; the API never listens on 5000. Proxied requests through ng-serve all returned 403.

**Solution**: Replaced `proxy.conf.json` with `proxy.conf.cjs` that resolves the API URL from Aspire service-discovery env vars (`services__api__https__0` / `services__api__http__0`) at serve time, with fallback patterns and localhost:5000 default for standalone ng serve.

**Key Learning for Angular Samples**: Always resolve API proxy targets dynamically from Aspire env vars, never hard-code. This pattern works cross-platform (macOS, Linux, Windows) without manual proxy config.

**Files Changed**:
- Deleted: `samples/dotnet-angular-cosmos/frontend/proxy.conf.json`
- Created: `samples/dotnet-angular-cosmos/frontend/proxy.conf.cjs` (Aspire-aware)
- Modified: `samples/dotnet-angular-cosmos/frontend/angular.json` (proxyConfig reference)
- Modified: `samples/dotnet-angular-cosmos/frontend/src/app/app.component.ts` (error handling in saveRecipe())

**Commit**: a931e4a "fix(dotnet-angular-cosmos): resolve API proxy target from Aspire env"

**Apply to Future Angular Samples**: This pattern should be the standard for any Angular samples in Aspire demos.

### 2026-07-04 — Polyglot Event Stream Producer HTTP Endpoint Fix

**Context**: Runtime QA revealed polyglot-event-stream failed in some environments: producer crashed, consumer/dashboard saw no data.

**Diagnosis Technique**: Used `lsof -nP -iTCP -sTCP:LISTEN | grep 5000` to identify AirPlay Receiver (ControlCe pid 778) holding port 5000 on macOS. Producer's AppHost called `.WithExternalHttpEndpoints()` only, which marks existing endpoints external but does NOT create one. Kestrel fell back to hard-coded default port 5000 → bind failure → crash.

**Root Cause**: Same class of bug as earlier dotnet-angular-cosmos proxy fix — misunderstanding that Aspire port allocation is opt-in. `.WithExternalHttpEndpoints()` is a marker, not an allocator. Required `.WithHttpEndpoint()` to come first.

**Solution**: 
- Added `.WithHttpEndpoint()` to producer before `.WithExternalHttpEndpoints()` (mirrors dotnet-angular-cosmos API pattern)
- Added `.WaitFor(kafka)` to producer, consumer, and dashboard to eliminate cold-start Kafka reconnect races

**Files Changed**:
- Modified: `samples/polyglot-event-stream/AppHost/Program.cs` (producer, consumer, dashboard resources)

**Verification**: Relaunched via `aspire run`. Producer bound dynamic port 55569. Full pipeline healthy: producer /api/sensors → 200; consumer /api/aggregates → readingCount climbing (55→59); dashboard /api/readings → live data; browser rendered all 5 sensor cards with sparklines + live timestamps.

**Commit**: 0ba9dc1 "fix(polyglot-event-stream): add WithHttpEndpoint() to producer, WaitFor(kafka) to all resources"

**Key Learning**: The pattern `.WithHttpEndpoint().WithExternalHttpEndpoints()` is the standard for any Aspire resource serving HTTP. First call allocates (Aspire picks a dynamic port + injects env), second call marks it external in the dashboard. Never assume framework defaults — on macOS, port 5000 is taken by AirPlay. This principle applies equally to .NET, Node, Python, Go apps in polyglot Aspire setups.
