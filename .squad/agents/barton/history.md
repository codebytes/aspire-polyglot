# Barton — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** All languages — testing across Python, JS/TS, Go, Java, C#/.NET
- **Samples to verify:** flask-markdown-wiki, django-htmx-polls, vite-react-api, svelte-go-bookmarks, spring-boot-postgres, dotnet-angular-cosmos, polyglot-event-stream

## Learnings

### 2025-07-18 — Full structural review of all 7 samples

**Findings:**
- 3 of 7 samples (flask-markdown-wiki, django-htmx-polls, svelte-go-bookmarks) have NO Aspire-managed infrastructure — they only run apps, not databases/caches/messaging. Chris wants every sample to demonstrate infra orchestration.
- Root README has wrong AppHost types for 3 samples: vite-react-api, svelte-go-bookmarks, and spring-boot-postgres all say `apphost.cs` but actually use polyglot AppHosts (`.py`, `.go`, `.java`).
- Sample-level READMEs for svelte-go-bookmarks and spring-boot-postgres also reference `apphost.cs` incorrectly.
- Feature flags in `.aspire/settings.json` are correct for all 5 polyglot samples. The 2 .NET AppHost samples (dotnet-angular-cosmos, polyglot-event-stream) correctly don't need them.
- 4 samples missing per-sample `.gitignore` (mitigated by root `.gitignore`).
- All dependency files present and version-consistent (Aspire 9.2.1 throughout).
- Dockerfiles correct for Go and Java samples.
- Spring Boot correctly reads Aspire connection strings via `${ConnectionStrings__notesdb}`.
- No build artifacts tracked in git.
- Full report written to `.squad/decisions/inbox/barton-test-review.md`.

### 2026-03-22 — Team Review Session Complete
- Barton's findings (3 samples no infra, README typos) confirmed as critical by full team.
- **Action items from team:**
  - Fix root README AppHost column (trivial)
  - Fix sample-level READMEs for svelte-go-bookmarks and spring-boot-postgres
  - Consider adding infrastructure to flask-markdown-wiki, django-htmx-polls, svelte-go-bookmarks (team decision)
- **NuGet.config inconsistency decision raised:** All polyglot samples should have it or none — team to decide
