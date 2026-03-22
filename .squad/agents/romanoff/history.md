# Romanoff — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Go, Svelte, Docker
- **Primary samples:** svelte-go-bookmarks

## Learnings

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
