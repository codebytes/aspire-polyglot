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
