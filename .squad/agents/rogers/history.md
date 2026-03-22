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
