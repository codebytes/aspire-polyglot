# Stark — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck for conference talk
- **User:** Chris Ayers
- **Stack:** Python, JS/TS, Go, Java, C#/.NET, Kafka, Marp
- **Samples:** flask-markdown-wiki, django-htmx-polls, vite-react-api, svelte-go-bookmarks, spring-boot-postgres, dotnet-angular-cosmos, polyglot-event-stream

## Learnings

### 2025-07-17 — Full Talk Repo Quality Review
- Audited all 7 samples, root README, slides, and config files.
- **Root README error found:** vite-react-api listed as `apphost.cs` but is actually `apphost.py`.
- **Config migration needed:** 5 polyglot samples use deprecated `.aspire/settings.json` with string `"true"`. Must migrate to `aspire.config.json` with boolean `true` for 13.2.
- **Slides "What's New in 13.2" is thin:** Only covers TS AppHost, CLI, and integrations. Missing: `aspire.config.json`, new CLI commands (`--detach`, `ps`, `stop`, `doctor`, `agent`), Bun support, Java cert trust, dashboard improvements, breaking changes.
- **3 of 7 samples don't demo Aspire-managed infrastructure** (flask-markdown-wiki, django-htmx-polls, svelte-go-bookmarks use SQLite/in-memory). Not a blocker — they show app orchestration — but slides should frame this clearly.
- **NuGet.config inconsistency:** Only 2 of 5 polyglot samples have it. Needs standardization.
- **Slide theme uses `custom-aspire-light`**, not `custom-default` from project convention. Likely intentional — needs Chris's confirmation.
- **Sample arc is strong:** Simple Python → frameworks → frontend+infra → Java+DB → Go+Dockerfile → .NET+cloud → polyglot Kafka. Good narrative progression.
- Report written to `.squad/decisions/inbox/stark-talk-review.md`.

### 2026-03-22 — Full Team Review Session (Decision Summary)
- All 8 agents completed parallel reviews (6 language specialists + Scribe).
- **Critical issues consolidated into decisions.md:** Feature flags slide needed, 13.2 coverage incomplete, 3 samples lack infrastructure, Java connection string format risk.
- **Config migration decision raised:** `.aspire/settings.json` → `aspire.config.json` — all polyglot samples, timing TBD.
- **Theme decision needed:** custom-aspire-light (current, looks good) vs. custom-default (charter).
- **Infrastructure additions recommended:** Flask, Django, and Go samples could add Redis/PostgreSQL for stronger demo impact.
- **Orchestration logs and team session log created** in `.squad/orchestration-log/` and `.squad/log/`.

### 2026-03-22 — Root README Corrections Complete
- Fixed 3 critical AppHost type errors in root README table
- vite-react-api: apphost.cs → apphost.py (Python)
- svelte-go-bookmarks: apphost.cs → apphost.go (Go)
- spring-boot-postgres: apphost.cs → AppHost.java (Java)
- Root documentation now accurate for talk preparation

### 2025-03-23 — Final Talk Flow Review
- **Narrative arc:** ✅ Excellent — "No .NET required" lands early (line 175), reinforced at line 200 and 843
- **Slide order:** ✅ Optimal — "Enabling Polyglot Support" perfectly placed after CLI Quick Start; "What's New 13.2" lands before takeaways
- **Key issues found:**
  - **🔴 CRITICAL:** Demo code is STALE — Flask Wiki missing Redis wiring, Django Polls missing PostgreSQL, Go Bookmarks missing PostgreSQL (updated in samples but not slides)
  - **🔴 CRITICAL:** Java connection string format risk (Demo 4) — Aspire injects C#-format, Spring Boot expects JDBC format; no error handling shown
  - **⚠️ HIGH:** Missing Demos intro context (line 470), DX/hot reload not covered, CI/CD polyglot story underdeveloped
  - **⚠️ MEDIUM:** Minor redundancy between CLI Quick Start (173–192) and Enabling Polyglot (198–216); dashboard login token context missing
- **Strengths:** Pacing excellent, Key Takeaways powerful (opens with "orchestrates ANY language"), cheat sheet useful
- **Verdict:** Ready for talk with fixes to demo code #1–2 above
- Full report written to `.squad/decisions/inbox/stark-flow-review.md`

### 2025-07-17 — Full Slide Deck Polish Pass
- **Scope:** Complete review and edit of slides/Slides.md against actual sample code and 30-minute timing.
- **Critical fixes applied:**
  - **Demo code accuracy:** Updated all 8 demo slides to match actual AppHost source files. Major discrepancies found: slides showed idealized typed APIs (`add_redis`, `add_python_app`, `addPythonApp`) but actual polyglot samples use low-level primitives (`add_container`, `add_dockerfile`, `AddExecutable`). C# samples were mostly correct except `AddNpmApp` → `AddJavaScriptApp`.
  - **ts-starter demo:** Updated to match actual `apphost.ts` (uses `addNodeApp("app")` + `addViteApp("frontend")` with `waitFor`, not the old `addNpmApp` pattern). Added TS debugging speaker note (Aspire 13.2 VS Code extension improvements).
  - **aspire.config.json example:** Fixed version from "9.5.2" to "13.2.0", removed non-existent "channel" field.
  - **Connection Strings slide:** Updated Java example from `NOTESDB_JDBCCONNECTIONSTRING` to actual explicit env var pattern matching the Spring Boot sample.
  - **Five Languages slide:** Updated Python example from `add_python_app` (doesn't exist in Python SDK) to `add_dockerfile` (actual API). Added qualifier about C# having richest typed integrations.
  - **Cheat Sheet:** Removed `AddGolangApp()` and `AddSpringApp()` (don't exist), added `AddJavaScriptApp()` and `AddExecutable()`.
  - **Bio slide:** Removed strikethrough Twitter line.
- **Narrative polish:** Tightened "Polyglot Problem" wording, improved transitions, enhanced speaker notes on key slides.
- **Verdict:** ✅ Ready for delivery. Theme decision (#4) still pending Chris's approval.
- Report written to `.squad/decisions/inbox/stark-slide-review.md`.

### 2026-03-23 — Team Scribe Session: Orchestration Log Recorded
- Stark's slide accuracy review work documented in orchestration log
- All 8 demo slides verified against actual AppHost source code
- Verdict: ✅ Slides ready for 30-minute conference delivery (timing: ~26 slides, ~30 min)
- Decision #6 (Slide Polish Pass) archived to decisions.md
- Outstanding items documented: theme decision #4 pending, commented-out demo walkthroughs flagged for future accuracy pass if uncommented
