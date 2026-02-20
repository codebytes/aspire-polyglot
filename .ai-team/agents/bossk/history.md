# Project Context

- **Owner:** Chris Ayers (clayers@gmail.com)
- **Project:** Polyglot .NET Aspire samples — service orchestration across Python, JavaScript, Go, Java, C#, and mixed-language applications
- **Stack:** Python (Flask, Django), JavaScript (Vite, React, Svelte), Go, Java (Spring Boot), C# (ASP.NET Core, Angular), .NET Aspire, Docker, Redis, PostgreSQL, CosmosDB, Kafka
- **Created:** 2026-02-20

## Learnings

<!-- Append new learnings below. Each entry is something lasting about the project. -->

### 2026-02-20 — Full QA Review of All 7 Samples

- Reviewed all 7 polyglot samples for code quality, security, error handling, dependencies, and runnability.
- **Key patterns found across samples:**
  - All samples use `CORS *` / `AllowAnyOrigin` — acceptable for demos but worth documenting.
  - Python deps in `flask-markdown-wiki` and `vite-react-api` are unpinned (no versions). `django-htmx-polls` and `polyglot-event-stream` pin correctly.
  - No lock files anywhere (no `package-lock.json`, no `requirements.lock`). Gitignored in `polyglot-event-stream`.
  - Django sample has hardcoded `SECRET_KEY` and `DEBUG = True` in settings.py.
  - Flask wiki has XSS risk via `{{ content|safe }}` rendering user Markdown as raw HTML, plus `debug=True` fallback.
  - Go bookmarks API hardcodes port 8080 — not configurable via environment.
  - Spring Boot has default postgres credentials in `application.properties` (fallback values).
  - Vote endpoint in Django polls has race condition on `choice.votes += 1` (no F-expression or select_for_update).
  - No input validation on Note content length in Spring Boot despite `@Column(length = 2000)`.
  - `add_choice_input` view in Django polls does not sanitize `num` parameter — potential for crafted HTML injection.
- **File paths of interest:**
  - `samples/flask-markdown-wiki/src/main.py` — XSS via `|safe`, `debug=True` fallback
  - `samples/django-htmx-polls/src/pollsite/settings.py` — hardcoded SECRET_KEY, DEBUG=True
  - `samples/django-htmx-polls/src/polls/views.py` — race condition on vote, unvalidated `num` param
  - `samples/svelte-go-bookmarks/go-api/main.go` — hardcoded port, CORS *, in-memory store lost on restart
  - `samples/spring-boot-postgres/src/src/main/resources/application.properties` — default credentials
  - `samples/vite-react-api/src/api/main.py` — unpinned deps, no Redis error handling
  - `samples/dotnet-angular-cosmos/Api/Program.cs` — best error handling of all samples, good CosmosException catching
  - `samples/polyglot-event-stream/` — best-structured sample, good error handling, pinned deps
- **Documentation:** All 7 samples have READMEs. `vite-react-api` and `spring-boot-postgres` READMEs are thin compared to the others.

---

📌 **Team update (2026-02-20):**
- Pin unpinned Python dependencies in flask-markdown-wiki and vite-react-api
- Fix XSS vulnerability in flask-markdown-wiki (add bleach sanitization)
- Document demo security patterns (hardcoded keys, CORS allow-all) in README
- Verify all samples are runnnable end-to-end with proper error handling
— decided by Bossk
