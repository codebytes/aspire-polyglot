# Parker — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Vite, React, Node.js, TypeScript, Svelte, Angular
- **Primary samples:** vite-react-api, JS parts of polyglot-event-stream

## Learnings

### 2025-07-18 — Full JS/TS Code Review
- **All 4 samples have current deps**: React 19, Vite 5-6, Svelte 4, Angular 19, TypeScript 5.6-5.7, KafkaJS 2.2.4 — nothing outdated
- **Aspire service discovery is consistent**: Every sample correctly reads `services__*` or `ConnectionStrings__*` env vars with localhost fallbacks
- **Error handling is the #1 gap**: React (vite-react-api) and Angular (dotnet-angular-cosmos) both silently swallow API failures; Svelte sample handles errors best
- **vite-react-api uses deprecated `onKeyPress`** — needs `onKeyDown` replacement before browsers drop support
- **Angular subscriptions lack cleanup** — no `takeUntilDestroyed()` or `OnDestroy` in dotnet-angular-cosmos frontend
- **polyglot-event-stream node-dashboard** has excellent Kafka patterns (dual consumers, graceful shutdown, bounded state) but Express routes lack try-catch
- **No sample has ESLint, Prettier, or tests** — acceptable for demos but worth noting
- **svelte-go-bookmarks is the most demo-polished** — clean UI, error handling, debounced search, minimal deps
- **node_modules correctly not checked in** across all samples

### 2026-03-22 — Team Review Session Complete
- Full team confirmed error handling as critical blocker for demo reliability.
- **Action items routed to Parker:**
  - Fix deprecated `onKeyPress` → `onKeyDown` in vite-react-api (browsers dropping support)
  - Add `response.ok` validation to fetch calls in vite-react-api
  - Add error state/UI to React sample (currently silent failures)
  - Add error callbacks to all Angular `.subscribe()` calls in dotnet-angular-cosmos
  - Add unsubscribe cleanup (`takeUntilDestroyed()`) to Angular subscriptions
  - Add try-catch to Express routes in polyglot-event-stream node-dashboard
  - Add `process.on('unhandledRejection')` handler to node-dashboard
- **Deliverable:** All samples should handle API errors gracefully before live demo

### 2026-03-22 — README Correction Complete
- vite-react-api README: Fixed apphost.cs → apphost.py reference
- Updated AppHost language identification from C# to Python
- Documentation now consistent with TypeScript/React + Python architecture

### 2026-03-22 — Config Migration: vite-react-api
- Migrated from `.aspire/settings.json` → `aspire.config.json` (Aspire 13.2 format)
- Key changes: `appHostPath` nested under `appHost.path`, added `appHost.language: "python"`, added `sdk.version: "13.2.0"`, feature flags now boolean `true` (not string `"true"`), dropped language-specific experimental flags
- Removed deprecated `.aspire/` directory entirely
- Config file now lives at sample root, path is relative to sample root (no `../` prefix)

## Team Coordination

### 2026-03-22 — Aspire 13.2 Upgrade Sprint (Orchestration Session)

**Team Pattern Established:** All language specialists (Banner, Parker, Thor, Romanoff) migrated their respective samples from `.aspire/settings.json` → `aspire.config.json` following a unified pattern in a single coordinated session.

**Config Migration Pattern (Standardized across all 5 samples):**
- Old: File at `.aspire/settings.json`, path relative to `.aspire/` directory with `../` prefix to AppHost
- New: File at sample root as `aspire.config.json`, path relative to sample root, no `../` prefix
- Old format: `"appHostPath": "../apphost.{lang}"`, `"polyglotSupportEnabled": "true"` (string), language-specific flags like `"experimentalPolyglot:python"`
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
