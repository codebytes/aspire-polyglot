# Rogers — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** C#, .NET, ASP.NET Core, Angular 19, CosmosDB, Aspire
- **Primary samples:** dotnet-angular-cosmos, C# parts of polyglot-event-stream, C# AppHosts

## Learnings

### 2025-07-18 — Full C#/.NET Code Review
- Both samples (dotnet-angular-cosmos, polyglot-event-stream) are on Aspire 9.2.1; latest 9.x is 9.5.2, latest overall is 13.1.3
- No deprecated APIs found (WithSecretBuildArg, AIFoundry not used) — clean upgrade path
- **Key rename in Aspire 13.x**: `Aspire.Hosting.NodeJs` → `Aspire.Hosting.JavaScript`
- ServiceDefaults are identical across both samples — keep in sync during upgrades
- OpenTelemetry packages behind (1.10.x → 1.15.0 available)
- Microsoft.Azure.Cosmos behind (3.47.2 → 3.58.0)
- dotnet-angular-cosmos README has a code snippet bug: shows `.AddDatabase()` but code uses `.AddCosmosDatabase()`
- polyglot-event-stream uses modern .slnx format, dotnet-angular-cosmos uses legacy .sln
- Both AppHosts could benefit from `WaitFor()` on infrastructure resources (CosmosDB emulator, Kafka)
- No C# AppHost files exist in the other 5 samples (django, flask, spring-boot, svelte-go, vite-react)

### 2026-03-22 — Team Review Session Complete
- Team confirmed Aspire version lag as high-priority fix (packages 3+ versions behind).
- **Action items routed to Rogers:**
  - Upgrade all Aspire packages: 9.2.1 → 9.5.2 (both samples, all 6+ packages per sample)
  - Upgrade OpenTelemetry: 1.10.x → 1.15.0
  - Upgrade Microsoft.Azure.Cosmos: 3.47.2 → 3.58.0
  - Fix dotnet-angular-cosmos README code snippet (`.AddDatabase()` → `.AddCosmosDatabase()`)
  - Update README version references from "9.2.1" to current
  - Test both samples after version bump
- **Deliverable:** All .NET packages on latest 9.x before talk

### 2026-03-22 — NuGet Package Upgrades Complete
- Upgraded all Aspire packages from 9.2.1 → 9.5.2 across both samples (6 .csproj files)
- Upgraded Microsoft.Azure.Cosmos: 3.47.2 → 3.58.0
- Upgraded Microsoft.Extensions.Http.Resilience: 9.0.0 → 9.10.0
- Upgraded Microsoft.Extensions.ServiceDiscovery: 9.2.1 → 9.5.2
- Upgraded all OpenTelemetry packages: 1.10.x → 1.15.x (Exporter 1.15.0, Hosting 1.15.0, AspNetCore 1.15.1, Http 1.15.0, Runtime 1.15.0)
- Fixed dotnet-angular-cosmos README: `.AddDatabase()` → `.AddCosmosDatabase()` to match actual AppHost code
- Updated README version reference from "Aspire 9.2.1" to "Aspire 9.5.2"
- Both ServiceDefaults kept in sync (identical package versions)
- `dotnet restore` succeeded on both samples with no errors
- No API changes required — pure version bump, no code modifications needed
- NuGet flat container API requires fully lowercase package IDs (case matters for REST API, not for dotnet CLI)

### 2026-03-22 — Major Upgrade: .NET 10 + Aspire 13.1.3

- **Framework**: Upgraded all 6 .csproj files from `net9.0` → `net10.0`
- **Aspire SDK**: `Aspire.AppHost.Sdk` 9.5.2 → 13.1.3 (both AppHosts)
- **Aspire packages**: All Aspire.* packages upgraded 9.5.2 → 13.1.3
- **Breaking rename**: `Aspire.Hosting.NodeJs` → `Aspire.Hosting.JavaScript` (package renamed in Aspire 13.x)
- **Breaking API change**: `AddNpmApp()` → `AddJavaScriptApp()` — both AppHost Program.cs files updated
- **Microsoft.Extensions.Http.Resilience**: 9.10.0 → 10.4.0
- **Microsoft.Extensions.ServiceDiscovery**: 9.5.2 → 10.4.0
- **OpenTelemetry packages**: Already at 1.15.x (no change needed)
- **Microsoft.Azure.Cosmos**: Already at 3.58.0 (no change needed)
- **NoWarn cleanup**: Removed `ASPIREHOSTINGNODEJS001` from polyglot-event-stream AppHost (no longer applies)
- **README updates**: Fixed `AddNpmApp` → `AddJavaScriptApp` in code snippets, version refs updated
- Both samples build clean: 0 warnings, 0 errors on .NET 10
- This was a major framework jump (9→10) + major Aspire jump (9.x→13.x), not a patch bump
- Key learning: Aspire 13.x package versioning follows the CLI versioning (both at 13.1.3)

### 2026-03-22 — Package Upgrade Complete
- All Aspire packages upgraded: 9.2.1 → 9.5.2 (both samples)
- Supporting packages updated: Cosmos SDK, OpenTelemetry, HTTP Resilience
- dotnet restore verified, zero breaking changes
- Documentation fixed: API references, version mentions
- Both samples tested and synchronized

### 2026-03-23 — Aspire 13.2.0 Upgrade Attempted (BLOCKED)
- Attempted upgrade of all 11 Aspire package references from 13.1.3 → 13.2.0 across 4 .csproj files
- **Result: 13.2.0 does not exist on NuGet yet** — latest stable is 13.1.3 as of this date
- All 11 packages checked: Aspire.AppHost.Sdk, Aspire.Hosting.AppHost, Aspire.Hosting.Azure.CosmosDB, Aspire.Hosting.JavaScript, Aspire.Hosting.Kafka, Aspire.Hosting.Python, Aspire.Microsoft.Azure.Cosmos, Aspire.Confluent.Kafka
- Changes reverted to 13.1.3 — both samples restore clean
- Files audited: 6 .csproj files total; ServiceDefaults have no Aspire packages (only OpenTelemetry/MS Extensions)
- Full inventory ready for when 13.2.0 drops: 4 files, 11 references to bump
- Key learning: decisions.md references "Aspire 13.2" features (aspire.config.json) but NuGet packages haven't shipped yet

## Team Coordination

### 2026-03-22 — Aspire 13.2 Upgrade Sprint (Orchestration Session)

**Team Pattern Established:** All language specialists (Banner, Parker, Thor, Romanoff) migrated their respective samples from `.aspire/settings.json` → `aspire.config.json` following a unified pattern in a single coordinated session.

**Rogers' Role:** Attempted NuGet upgrade to 13.2.0 but packages not yet published. Full scope documented and ready for immediate execution once 13.2.0 lands on NuGet:
- dotnet-angular-cosmos: 5 package refs across 2 files
- polyglot-event-stream: 6 package refs across 2 files
- ServiceDefaults: No Aspire packages (no upgrade needed)

**Config Migration Pattern (Standardized across all 5 polyglot samples):**
- Old: File at `.aspire/settings.json`, path relative to `.aspire/` directory with `../` prefix to AppHost
- New: File at sample root as `aspire.config.json`, path relative to sample root, no `../` prefix
- Old format: `"appHostPath": "../apphost.{lang}"`, `"polyglotSupportEnabled": "true"` (string), language-specific flags
- New format: `"appHost": { "path": "apphost.{lang}", "language": "{lang}" }`, `"polyglotSupportEnabled": true` (boolean), no language-specific flags, `"sdk": { "version": "13.2.0" }` added
- All 5 samples validated with `aspire restore` — zero regressions

**Team Outcomes:**
- ✅ Banner (Python): flask-markdown-wiki, django-htmx-polls migrated
- ✅ Parker (JS): vite-react-api migrated
- ✅ Thor (Java): spring-boot-postgres migrated
- ✅ Romanoff (Go): svelte-go-bookmarks migrated
- ⏳ Rogers (C#/.NET): Aspire 13.2.0 NuGet packages not yet published; scope documented for immediate execution once 13.2.0 lands
- ✅ Strange (Content): Slides updated to comprehensively cover Aspire 13.2 features and config changes

**Decision #1 (Config Migration):** ✅ RESOLVED — All polyglot samples now use Aspire 13.2 format
**Decision #3 (13.2 Slides):** ✅ RESOLVED — Expanded from 2 slides to 4 slides covering all major 13.2 features
**Decision #7 (NuGet Upgrades):** ⏳ DEFERRED — Waiting for Aspire 13.2.0 on NuGet; scope ready

### 2026-07-02 — Aspire 13.4.6 Upgrade Complete

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
