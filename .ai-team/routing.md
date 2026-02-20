# Work Routing

How to decide who handles what.

## Routing Table

| Work Type | Route To | Examples |
|-----------|----------|----------|
| Architecture & Aspire patterns | Lando | AppHost design, service orchestration, cross-sample patterns |
| Python, Go, Java, C# services | Wedge | Flask, Django, Spring Boot, ASP.NET Core, Go APIs |
| Frontend & UI | Biggs | React, Svelte, Angular, HTMX, Vite configuration |
| Code review | Lando | Review PRs, check quality, suggest improvements |
| Testing & QA | Bossk | Write tests, find edge cases, verify fixes, code quality |
| Scope & priorities | Lando | What to build next, trade-offs, decisions |
| Session logging | Scribe | Automatic — never needs routing |

## Rules

1. **Eager by default** — spawn all agents who could usefully start work, including anticipatory downstream work.
2. **Scribe always runs** after substantial work, always as `mode: "background"`. Never blocks.
3. **Quick facts → coordinator answers directly.** Don't spawn an agent for "what port does the server run on?"
4. **When two agents could handle it**, pick the one whose domain is the primary concern.
5. **"Team, ..." → fan-out.** Spawn all relevant agents in parallel as `mode: "background"`.
6. **Anticipate downstream work.** If a feature is being built, spawn the tester to write test cases from requirements simultaneously.
