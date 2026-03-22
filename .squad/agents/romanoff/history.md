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
