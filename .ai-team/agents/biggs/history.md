# Project Context

- **Owner:** Chris Ayers (clayers@gmail.com)
- **Project:** Polyglot .NET Aspire samples — service orchestration across Python, JavaScript, Go, Java, C#, and mixed-language applications
- **Stack:** Python (Flask, Django), JavaScript (Vite, React, Svelte), Go, Java (Spring Boot), C# (ASP.NET Core, Angular), .NET Aspire, Docker, Redis, PostgreSQL, CosmosDB, Kafka
- **Created:** 2026-02-20

## Learnings

<!-- Append new learnings below. Each entry is something lasting about the project. -->

### 2026-02-20 — Full Frontend Review (all 6 samples)

- **Frontend tech map:** React 19 + Vite 6 (`vite-react-api`), Svelte 4 + Vite 5 (`svelte-go-bookmarks`), Angular 19 standalone (`dotnet-angular-cosmos`), Django templates + HTMX 2.0.4 (`django-htmx-polls`), Flask `render_template_string` inline HTML (`flask-markdown-wiki`), EJS + vanilla JS polling (`polyglot-event-stream`).
- **API integration pattern:** All frontends use relative `/api/` paths with dev-server proxy (Vite proxy for React/Svelte, Angular proxy.conf.json, HTMX direct Django URLs). Node dashboard polls its own Express endpoints.
- **No shared CSS framework** — every sample uses custom inline CSS. System font stacks are consistent across samples.
- **No routers in SPAs** — React, Svelte, and Angular samples are all single-view apps. Appropriate for demo scope.
- **Svelte sample uses Svelte 4 (not 5)** — `@sveltejs/vite-plugin-svelte@^3.0.1`, `svelte@^4.2.8`. Uses legacy `$:` reactive syntax, not Svelte 5 runes.
- **Angular uses standalone components** — modern pattern, no NgModules. Uses `provideHttpClient()` bootstrapping.
- **React uses `onKeyPress`** (deprecated) — should be `onKeyDown`.
- **Flask wiki has all HTML as Python string constants** — `render_template_string` with no Jinja file templates. Functional but hard to maintain.
- **Node dashboard has no static file serving** — pure EJS template with inline `<script>` and `<style>`. Polls `/api/readings` every 5 seconds.
- **Key file paths:**
  - `samples/vite-react-api/src/web/src/App.tsx` — React Todo SPA
  - `samples/svelte-go-bookmarks/frontend/src/App.svelte` — Svelte Bookmark Manager
  - `samples/dotnet-angular-cosmos/frontend/src/app/app.component.ts` — Angular Recipe Manager
  - `samples/django-htmx-polls/src/polls/templates/polls/` — HTMX poll templates
  - `samples/flask-markdown-wiki/src/main.py` — Flask wiki (all-in-one)
  - `samples/polyglot-event-stream/node-dashboard/views/index.ejs` — IoT dashboard

---

📌 **Team update (2026-02-20):**
- React sample: Replace deprecated onKeyPress with onKeyDown
- All SPA samples: Add error boundaries for better error handling and reliability
- Flask wiki: Move templates to templates/ folder for maintainability
- Svelte: Document choice to use Svelte 4 or consider migration to Svelte 5
- Consider shared design system if samples are presented as a visual suite
— decided by Biggs
