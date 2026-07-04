# Romanoff — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Go, Svelte, Docker
- **Primary samples:** svelte-go-bookmarks

## Learnings

### OpenTelemetry Integration for Go Bookmarks API (2025-01-XX)
- Successfully added full OpenTelemetry instrumentation to the Go bookmarks API
- Used gRPC OTLP exporters for traces, metrics, and logs compatible with Aspire dashboard
- Implemented conditional initialization checking for `OTEL_EXPORTER_OTLP_ENDPOINT` environment variable
- Wrapped all HTTP handlers with `otelhttp.NewHandler` middleware for automatic span creation
- Used standard OpenTelemetry SDK packages: `go.opentelemetry.io/otel`, `go.opentelemetry.io/otel/sdk/*`, and exporters
- Preserved existing functionality: CORS handling, PostgreSQL/in-memory storage logic remain unchanged
- The instrumentation is transparent - app runs normally when Aspire environment variables aren't present

### 2025-07-15: Deep Review of svelte-go-bookmarks
- **apphost.go** uses correct Aspire Go SDK patterns: `CreateBuilder` → `AddDockerfile` → `AddNpmApp` with proper service wiring
- **Go API** is stdlib-only (zero deps), thread-safe with `sync.RWMutex`, has full CRUD + search + health endpoint
- **Dockerfile** is proper multi-stage build: `golang:1.22-alpine` → `alpine:3.19`
- **Critical bug found:** README references `apphost.cs` (C#) in 3 places — actual file is `apphost.go`
- **Config migration needed:** `.aspire/settings.json` should migrate to `aspire.config.json` per Aspire 13.2
- **Route overlap:** `/api/bookmarks/search` registered twice (inside prefix handler + standalone)
- **Tags stored as comma-separated string** instead of `[]string` — works for demo but not idiomatic Go
- **Frontend integration** works via Vite proxy reading `services__api__http__0` env var — clean pattern
- **CSS conflict:** `app.css` dark theme overridden by `App.svelte` purple gradient
- **Versions:** Go 1.22 / Alpine 3.19 are reasonable for demo but slightly dated
- **README** lists Aspire 9.2.1 — very stale, needs update to current version
- Report written to `.squad/decisions/inbox/romanoff-go-review.md`

### 2026-03-22 — Team Review Session Complete
- Full team identified svelte-go-bookmarks README as blocking issue (says apphost.cs, is apphost.go).
- **Action items routed to Romanoff:**
  - Fix README: Replace all `apphost.cs` references with `apphost.go` (3 places: lines 9, 26, 43)
  - Fix code snippet: Show Go syntax instead of C# `builder.AddDockerfile(...)`
  - Migrate `.aspire/settings.json` → `aspire.config.json` (timing TBD with team)
  - Clean up CSS conflict between app.css and App.svelte body styles
  - Remove redundant `/api/bookmarks/search` route registration
  - Update README Aspire version reference (9.2.1 → current)
  - Optional: Consider adding infrastructure (Redis/PostgreSQL) if team approves
- **Priority:** Fix README before talk (immediate blocker)

### 2025-07-15: Added PostgreSQL Infrastructure to svelte-go-bookmarks
- **apphost.go** updated: Added `AddPostgres("pg")` → `AddDatabase("bookmarksdb")` → `WithReference(db)` — follows the exact same pattern as spring-boot-postgres (Java AppHost) but with Go PascalCase conventions
- **Go API rewritten** with `Storage` interface: `pgStore` (PostgreSQL via `lib/pq`) and `memStore` (in-memory fallback) — same REST API, swappable backend
- **Graceful fallback**: reads `CONNECTIONSTRINGS__bookmarksdb` env var; if missing or connection fails, falls back to in-memory with log message — sample works with or without Aspire
- **go.mod** updated: added `github.com/lib/pq v1.10.9` — chose `lib/pq` over `pgx` for simplicity (single import, no connection pool config)
- **Dockerfile** updated: added `go.sum` copy and `go mod download` step before source copy — proper layer caching for dependencies
- **README fully fixed**: all `apphost.cs` → `apphost.go`, C# code snippet → Go code snippet, added PostgreSQL to architecture/features/technologies, removed stale Aspire 9.2.1 version
- **DB schema**: single `bookmarks` table with `SERIAL PRIMARY KEY`, auto-seeds 3 bookmarks on empty table
- **Connection string pattern**: Aspire injects PostgreSQL connection strings as standard `lib/pq` format via `CONNECTIONSTRINGS__` prefix — works out of the box unlike Java's JDBC format issue (Decision #5)

### 2026-03-22 — PostgreSQL Integration Complete
- svelte-go-bookmarks: PostgreSQL database wiring complete
- Storage interface pattern implemented with in-memory fallback
- Graceful fallback working (sample runs standalone without Aspire)
- README corrected: apphost.cs → apphost.go references, Go language snippets
- Decision #6 (Infrastructure in Simple Samples) resolved for Go samples

### 2025-07-15 — Config Migration: .aspire/settings.json → aspire.config.json
- Migrated svelte-go-bookmarks from deprecated `.aspire/settings.json` to `aspire.config.json` at sample root
- Key changes: `appHostPath` → `appHost.path` (removed `../` prefix), added `appHost.language: "go"`, added `sdk.version: "13.2.0"`, feature flags now boolean `true` not string `"true"`, dropped `experimentalPolyglot:go` flag
- Deleted `.aspire/` directory (was only settings.json inside)
- Decision #1 (Config Migration) now executed for Go sample

## Team Coordination

### 2026-03-22 — Aspire 13.2 Upgrade Sprint (Orchestration Session)

**Team Pattern Established:** All language specialists (Banner, Parker, Thor, Romanoff) migrated their respective samples from `.aspire/settings.json` → `aspire.config.json` following a unified pattern in a single coordinated session.

**Config Migration Pattern (Standardized across all 5 samples):**
- Old: File at `.aspire/settings.json`, path relative to `.aspire/` directory with `../` prefix to AppHost
- New: File at sample root as `aspire.config.json`, path relative to sample root, no `../` prefix
- Old format: `"appHostPath": "../apphost.{lang}"`, `"polyglotSupportEnabled": "true"` (string), language-specific flags like `"experimentalPolyglot:go"`
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

### 2026-07-02 — Aspire 13.4.6 Upgrade for svelte-go-bookmarks
- Updated `aspire.config.json`: sdk.version from `13.2.4` → `13.4.6`
- **CLI upgrade required**: Aspire CLI mismatch (13.4.3 → 13.4.6) caused initial restore failure with "No code generator found for language: Go" error
- Ran `aspire update -y` to upgrade CLI from 13.4.3 to 13.4.6+87fe259e4fc244c599019a7b1304c85a1488f248
- **aspire restore successful**: CLI-generated Go bindings in `.modules/` regenerated for 13.4.6 SDK
- **No .modules changes**: `git diff --stat .modules/` showed zero changes — bindings API stable from 13.2.4 → 13.4.6
- **apphost.go unchanged**: All builder signatures remain compatible (CreateBuilder, AddContainer, AddDockerfile, WithEnvironment, WithHttpEndpoint, WithOtlpExporter, GetEndpoint, WaitFor) — no code changes needed
- **Build verification**: `go build ./...` (apphost module) ✅ clean, `go vet ./...` ✅ clean, `go build .` (go-api) ✅ clean
- **Key insight**: Aspire Go SDK maintains strong API stability across minor versions — zero breaking changes observed between 13.2.4 and 13.4.6

### 2026-07-02 — Aspire 13.4.6 Binding Layout Migration + Breaking API Changes
- **Binding relocation**: 13.4.6 moved generated Go bindings from `.modules/` (373KB aspire.go) → `.aspire/modules/` (694KB aspire.go) — nearly 2x size increase with richer API surface
- **go.mod replace update**: Changed `replace apphost/modules/aspire => ./.modules` → `replace apphost/modules/aspire => ./.aspire/modules` — module name unchanged, import statements in apphost.go remain identical
- **Stale bindings retired**: `git rm -r .modules/` removed old 13.2.4-era bindings; `.aspire/modules/` is now canonical location for polyglot code generation
- **BREAKING API CHANGES in 13.4.6**: Initial assumption of API stability was WRONG — significant breaking changes discovered after switching to new bindings:
  1. **Builder method returns simplified**: `AddContainer`, `AddDockerfile`, `AddExecutable` now return only the resource (not `(resource, error)` tuple) — error handling moved to internal deferred check pattern
  2. **GetEndpoint signature changed**: Returns `EndpointReference` directly (not `(EndpointReference, error)`) — errors tracked internally via resource builder state
  3. **WithHttpEndpoint now options-based**: Old positional args `(name, port, targetPort, scheme, env)` → new struct `&WithHttpEndpointOptions{Port, TargetPort, Name, Env, IsProxied}`
  4. **WithEnvironmentEndpoint removed**: Unified into `WithEnvironment` — now accepts `EndpointReference` directly as second arg (no separate method)
  5. **WaitFor signature simplified**: Takes `Resource` interface directly (not tuple), returns resource for chaining — `NewIResource` wrapper helper removed from API
  6. **WithOtlpExporter now void**: Returns resource for chaining (not `(resource, error)`)
- **apphost.go rewrite required**: Full refactor from 13.2.4 positional-arg pattern → 13.4.6 options-struct pattern — custom `float64Ptr`/`stringPtr` helpers replaced with `aspire.StringPtr` helper from SDK
- **Migration complexity**: MEDIUM — 67-line apphost.go required 12 API call updates (3 AddXxx, 3 WithHttpEndpoint, 2 GetEndpoint, 1 WithEnvironment, 1 WaitFor, 1 WithOtlpExporter) but logic unchanged
- **Benefits of new API**: Cleaner error handling (no tuple unpacking), better discoverability (options struct self-documents parameters), forward compatibility (new fields can be added without breaking changes)
- **Build verification post-migration**: `go build ./...` ✅, `go vet ./...` ✅, `aspire restore` ✅ (regenerates `.aspire/modules/` in place), `go-api` backend ✅ (unaffected by apphost changes)
- **Key insight**: Aspire 13.2.x → 13.4.6 was NOT a minor bump for Go — major API redesign hidden behind minor version number; always test bindings after SDK upgrade


### 2026-07-02 — WaitFor Startup Ordering Fix for Cold-Start Race Condition

**Problem:** On cold starts (when the `postgres:16` image isn't cached), the Go API in `samples/svelte-go-bookmarks/` can start before Postgres is healthy. When its initial Postgres connection Ping fails, `main.go` (lines 466-518) silently falls back to an in-memory store (logs a warning but continues serving). The API still reports `/health` as healthy, creating a silent data-integrity trap — writes appear to succeed but aren't persisted to Postgres.

**Root Cause:** In Aspire 13.4.6 polyglot, `WithReference` and manual env var wiring (`PG_HOST`, `PG_USER`, `PG_PASSWORD`, `PG_DB`) only inject connection strings — they do NOT order startup. Startup ordering requires an explicit `WaitFor` call. The sample had `frontend.WaitFor(api)` (line 59) but was missing `api.WaitFor(pg)`.

**Fix Applied:** Added `api.WaitFor(pg)` to `samples/svelte-go-bookmarks/apphost.go` immediately after `api.WithExternalHttpEndpoints()` (after line 40) and before the frontend block. This ensures the API never starts until Postgres is Healthy, preventing the silent in-memory fallback.

**Comment Added:** Explains the 13.4.6 WaitFor-vs-WithReference rule: env wiring alone does not order startup in polyglot; on a cold start the API can race Postgres, fail Ping, and silently fall back to in-memory.

**Verification:** `go vet .` ✅ clean, `gofmt -l apphost.go` ✅ reports nothing (file is gofmt-clean), `api.WaitFor(pg)` confirmed present in output.

**Key Insight:** This is the exact analogue of the fix already applied to Python samples (`polls.WaitFor(pollsdb)`, `wiki.WaitFor(cache)`). The WaitFor pattern is mandatory in 13.4.6 polyglot for any resource that depends on a datastore or cache — manual env wiring is insufficient for startup ordering.

**Related File:** `samples/svelte-go-bookmarks/go-api/main.go` (lines 466-518 contain the silent in-memory fallback logic that triggers when Postgres is unreachable).
