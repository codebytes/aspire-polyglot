# Strange — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Marp, Markdown, DrawIO, Mermaid
- **Primary files:** slides/Slides.md, slides/img/, slides/themes/, README.md

## Learnings

### Slides Review (2025-03-22)
1. **Feature Flags Gap (CRITICAL):** Slides never mention `polyglotSupportEnabled` or `experimentalPolyglot:*` flags. Chris specifically wants this covered. Samples require these flags in `.aspire/settings.json` but slides have no explanation.
   - **Action:** Insert new slide after line 193 explaining feature flag structure and why samples need them.

2. **13.2 Coverage (INCOMPLETE):** "What's New in 13.2" section (lines 814–837) covers only 3/15+ key features. Missing: `aspire.config.json`, detached mode, isolated mode, `aspire doctor`, `aspire restore`, `aspire agent`, resource commands, Bun support, Foundry, Data Lake integration, MongoDB EF.
   - **Action:** Expand section with 5–7 additional features, consider moving earlier in deck.

3. **Theme Inconsistency:** Slides use `theme: custom-aspire-light` but charter says `theme: custom-default`. Both exist; need clarification on intent.
   - **Status:** `custom-aspire-light.css` is well-designed and looks better; recommend keeping if Chris approves.

4. **DrawIO vs. Mermaid:** Slides use DrawIO SVG diagrams (not Mermaid). Charter mentions Mermaid but DrawIO is actually superior for complex architecture diagrams (better performance, no JS needed).
   - **Status:** No action needed; SVGs work well. Update charter if SVG is standard.

5. **All Images Present:** Verified all 9 referenced images exist and load correctly.

6. **Demo Slides Ready:** 7 complete, commented-out demo walkthroughs (lines 896–1,434) are ready to uncomment. Python AppHost code correctly uses snake_case (`add_python_app`, `with_http_endpoint`).

7. **CLI Commands Current:** API references are up-to-date. Missing mentions: `aspire agent` (renamed from `aspire mcp`), `aspire doctor`, `aspire restore`, `--detach`, `--isolated` flags.

8. **Narrative Flow Strong:** Well-structured progression (intro → concepts → architecture → observability → wrap-up). Pacing good with mix of titles, code, diagrams, bullets.

9. **Marp Formatting Clean:** YAML frontmatter valid, slide delimiters correct, layout classes proper, speaker notes well-formatted, no syntax errors.

10. **Report Location:** Detailed review written to `.squad/decisions/inbox/strange-slides-review.md` with priority-ranked recommendations.

### 2026-03-22 — Team Review Session Complete
- Full team prioritized slides as critical path for talk readiness.
- **Action items routed to Strange:**
  - **CRITICAL:** Insert feature flags slide after line 193 (Chris specifically wants this)
    - Explain `.aspire/settings.json` structure
    - Cover `polyglotSupportEnabled: true`
    - Cover language-specific flags (`experimentalPolyglot:python|java|go`)
    - Show sample config examples
  - **CRITICAL:** Expand "What's New in 13.2" section (lines 814–837)
    - Add 5–7 additional key 13.2 features
    - Prioritize: config migration, CLI enhancements, dashboard features
    - Consider expanding to 2–3 slides if needed
  - Clarify Marp theme intent with Chris (keep custom-aspire-light if approved)
  - Review demo slide timing (7 demos = 30–40 min; ensure fits talk duration)
  - Have pre-recorded demo backups ready for safety
- **Deliverable:** Slides complete and ready for 13.2 talk

### 2026-03-22 — Slides Expansion Complete
- Feature flags slide added after TypeScript AppHost section
- "What's New in 13.2" expanded from 1 slide to 2 slides (CLI + infrastructure)
- Comprehensive coverage of 8+ major 13.2 features for talk preparation

### 2025-07-25 — Full Aspire 13.2 Polyglot Alignment
- Updated slides for full Aspire 13.2 polyglot coverage across all slide sections
- **"What's New in 13.2" expanded from 2 slides to 4 slides:**
  1. CLI Revolution — aspire new (language-aware), restore, run --detach, ps, stop, describe, doctor, agent, docs, wait, export, update
  2. aspire.config.json — Full new format with code example, breaking change callout, boolean feature flags, config/secret/cert CLI commands
  3. Multi-Language & Integrations — TypeScript AppHost improvements, WithBun(), Java cert trust, Go/Java/Rust codegen, WithMcpServer, WithBuildSecret, Foundry, contextual endpoint resolution
  4. Dashboard & DX — Export/import, .env export, persistent UI state, resource graph, OTLP/JSON, security, breaking changes list
- **Config slide updated:** aspire.config.json example now shows full format (appHost.path+language, sdk.version+channel, boolean features)
- **CLI Quick Start updated:** Language-aware aspire new callout, aspire update mention
- **Cheat Sheet updated:** Added WithBun(), WithBuildSecret(), WithMcpServer()
- **Resources slide updated:** Added aspire docs search and aspire update references
- **Key Takeaways updated:** Added 13.2-specific line (doctor, agent, docs, WithBun, --isolated)
- All CLI references verified — no stale aspire resources/aspire mcp references found

## Team Coordination

### 2026-03-22 — Aspire 13.2 Upgrade Sprint (Orchestration Session)

**Team Pattern Established:** All language specialists (Banner, Parker, Thor, Romanoff) migrated their respective samples from `.aspire/settings.json` → `aspire.config.json` following a unified pattern in a single coordinated session.

**Strange's Role:** Expanded slides from 2 to 4 in "What's New in 13.2" section to comprehensively cover all polyglot-critical features. All config examples updated to new `aspire.config.json` format. Breaking changes documented for audience awareness.

**Config Migration Pattern (Standardized across all 5 polyglot samples):**
- Old: File at `.aspire/settings.json`, path relative to `.aspire/` directory with `../` prefix to AppHost
- New: File at sample root as `aspire.config.json`, path relative to sample root, no `../` prefix
- Old format: `"appHostPath": "../apphost.{lang}"`, `"polyglotSupportEnabled": "true"` (string), language-specific flags
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
**Decision #7 (NuGet Upgrades):** ⏳ DEFERRED — Waiting for Aspire 13.2.0 on NuGet; scope ready

### 2025-07-25 — Major Slide Deck Restructure (Polyglot-First Narrative)
- **Complete restructure** of slides/Slides.md from "Aspire intro + 13.2 changelog" to a tight polyglot story
- **Removed:** "What is Aspire?" generic slide, "Key Aspire Concepts" deep dive, 4 separate "What's New in 13.2" changelog slides, redundant "Observability & Service Discovery" deep dive section (6 slides), duplicate dashboard/health/service-discovery slides
- **Added:** "Five AppHost Languages" slide showing all 5 in one view, ts-starter demo slide (Demo 1), combined dashboard slide with cross-language trace example, `aspire new` language-aware scaffolding slide
- **Restructured flow:** Act 1 (Problem, 2 slides) → Act 2 (Polyglot Answer, 5 slides) → Act 3 (How It Works, 5 slides) → Act 4 (Demos, 9 slides for 8 samples) → Act 5 (Wrap-Up, 3 slides) → Appendix (reference + commented demo walkthroughs)
- **13.2 features woven into narrative:** aspire.config.json in Act 2, CLI commands (doctor/restore/detach/ps/stop) in Act 3, aspire new scaffolding in Act 3 — no longer a separate "What's New" section
- **Demo order changed:** simple → complex, now 8 demos (added ts-starter): ts-starter → flask-markdown-wiki → django-htmx-polls → vite-react-api → spring-boot-postgres → svelte-go-bookmarks → dotnet-angular-cosmos → polyglot-event-stream
- **vite-react-api demo updated:** Now shows TypeScript AppHost code (converted from Python)
- **Appendix preserved:** Cheat sheet, OTel setup, lifecycle, deployment, and all detailed commented-out demo walkthroughs kept intact
- **Net result:** Active deck went from ~33 main slides with lots of filler to 25 focused slides + 8 appendix slides. Tighter pacing for 30-45 min talk with demos
- **All 9 image references verified** — no broken links

### 2026-03-23 — Team Scribe Session: Orchestration Log Recorded
- Strange's slide restructure work documented in orchestration log
- All 25 main slides + 8 appendix slides approved by Stark
- Decision #6 (Slide Restructure) and Decision #2 (ts-starter demo) archived to decisions.md
- Slide deck confirmed ready for 30-minute conference delivery
- Session log created in .squad/log/ with cross-agent outcomes
