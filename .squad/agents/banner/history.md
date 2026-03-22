# Banner — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Python, Flask, Django, HTMX, SQLite, FastAPI
- **Primary samples:** flask-markdown-wiki, django-htmx-polls, Python parts of polyglot-event-stream

## Learnings

### Python Sample Review (completed)
- All four Python samples reviewed: flask-markdown-wiki, django-htmx-polls, vite-react-api (FastAPI), polyglot-event-stream (consumer)
- All apphost.py files use correct Aspire Python SDK patterns (create_builder → add_python_app → build → run)
- vite-react-api is the best orchestration demo (Redis + FastAPI + Vite wired together); Flask wiki and Django polls only orchestrate the app itself with embedded SQLite
- All three guest AppHost samples use `.aspire/settings.json` with string `"true"` — needs migration to `aspire.config.json` with boolean `true` per Aspire 13.2
- Fixed: removed unused imports in vite-react-api/main.py and polyglot-event-stream/python-consumer/main.py
- Fixed: deprecated Pydantic `.dict()` → `.model_dump()` in vite-react-api FastAPI code
- Django polls sample has best dependency management (version ranges); Flask wiki and Vite-React have no version pins
- polyglot-event-stream python-consumer has a race condition on shared `aggregates` dict (no threading lock)
- All samples use production WSGI servers (waitress or uvicorn), not dev servers — good practice
- Findings written to `.squad/decisions/inbox/banner-python-review.md`

### Infrastructure Additions (completed)
- Added Aspire-managed Redis to flask-markdown-wiki: `apphost.py` wires `add_redis("cache")` → wiki app via `with_reference(cache)`
- Flask app reads `ConnectionStrings__cache`, creates Redis client, caches rendered markdown HTML with 1-hour TTL, invalidates on page edit
- Graceful fallback: app works without Redis (prints warning, skips caching)
- Added Aspire-managed PostgreSQL to django-htmx-polls: `apphost.py` wires `add_postgres("pg")` → `add_database("pollsdb")` → polls app
- Django settings parse `ConnectionStrings__pollsdb` (supports both URI and .NET-style connection strings), falls back to SQLite
- Added auto-migrate in `run.py` so PostgreSQL tables are created on first startup
- Both samples follow the vite-react-api connection pattern: `ConnectionStrings__<resource-name>` env var
- Added `redis` and `psycopg2-binary` to respective `requirements.txt` files
- Updated both READMEs with new architecture diagrams and dependency info
- Decision #6 (Infrastructure in Simple Samples) now addressed for both Python samples

### 2026-03-22 — Team Review Session Complete
- Full team review complete; all findings consolidated into decisions.md.
- **Action items routed to Banner:**
  - Add infrastructure (Redis/PostgreSQL) to flask-markdown-wiki and django-htmx-polls (if team approves)
  - Migrate `.aspire/settings.json` → `aspire.config.json` for 3 Python samples (timing TBD with team)
  - Add dependency version pins to flask and vite-react-api requirements.txt
  - Add threading.Lock() to polyglot-event-stream python-consumer `aggregates` dict
- **Team decision pending:** Config migration timing (now vs. after talk)

### 2026-03-22 — Infrastructure Additions Completed
- Flask-markdown-wiki: Redis cache integration complete, graceful fallback working
- Django-htmx-polls: PostgreSQL database integration complete, auto-migration verified
- Both samples follow ConnectionStrings pattern, consistent with vite-react-api
- Decision #6 (Infrastructure in Simple Samples) fully resolved for Python samples

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
