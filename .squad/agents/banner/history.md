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

### 2026-03-22 — Team Review Session Complete
- Full team review complete; all findings consolidated into decisions.md.
- **Action items routed to Banner:**
  - Add infrastructure (Redis/PostgreSQL) to flask-markdown-wiki and django-htmx-polls (if team approves)
  - Migrate `.aspire/settings.json` → `aspire.config.json` for 3 Python samples (timing TBD with team)
  - Add dependency version pins to flask and vite-react-api requirements.txt
  - Add threading.Lock() to polyglot-event-stream python-consumer `aggregates` dict
- **Team decision pending:** Config migration timing (now vs. after talk)
