# Project Context

- **Project:** aspire-polyglot
- **Created:** 2026-03-22

## Core Context

Agent Scribe initialized and ready for work.

## Recent Updates

📌 Team initialized on 2026-03-22
📌 Full team review completed 2026-03-22T15:45Z — 8 agents, comprehensive findings

## Learnings

Initial setup complete.

### 2026-03-22 — Full Team Review Session Complete

**Scribe Tasks Completed:**
1. ✅ Created 8 orchestration logs (one per agent) in `.squad/orchestration-log/2026-03-22T15-45-review-{agent}.md`
2. ✅ Created team session log in `.squad/log/2026-03-22T15-45-full-team-review.md`
3. ✅ Merged 8 decision inbox files → `decisions.md` with 8 active decisions (deduped)
4. ✅ Deleted inbox files from `.squad/decisions/inbox/`
5. ✅ Appended team updates to all 8 agents' history.md files
6. ⏳ Pending: Git commit (step 5 of Scribe charter)

**Key Findings Summary:**
- **8 critical/high-priority issues** identified across code, slides, config
- **3 of 7 samples** lack Aspire-managed infrastructure (narrative impact)
- **Slides** missing feature flags coverage + incomplete 13.2 features (Chris requests both)
- **Packages** 3+ versions behind (Aspire 9.2.1 → 9.5.2, OpenTelemetry, etc.)
- **Connection string format risk** in Java sample (functional blocker)
- **Error handling gaps** in React/Angular samples (demo reliability risk)

**Decisions Documented:**
1. Config migration: `.aspire/settings.json` → `aspire.config.json` (timing TBD)
2. Feature flags slide (URGENT — Chris specifically wants this)
3. What's New in 13.2 expansion (URGENT — coverage incomplete)
4. Marp theme clarification (custom-aspire-light vs. custom-default)
5. Java connection string handling (functional fix needed)
6. Infrastructure additions to simple samples (narrative strengthening)
7. NuGet.config standardization (team decision)
8. Root README AppHost types (documentation accuracy)

**Next Steps:**
- Proceed with git commit of all .squad changes
- Route action items to specialist agents for implementation
- Team decisions needed on config migration timing, theme preference, infrastructure additions

### 2026-05-07 — Repository Cleanup: Build Artifacts + Polyglot .gitignore

**Task:** Post-OTel validation cleanup. Repository accumulated ~213 untracked files at root, tracked build artifacts across samples, and lacked comprehensive polyglot ignore patterns.

**Execution Summary:**

1. **Untracked 92 Java .class files** from `samples/spring-boot-postgres/aspire/` via `git rm --cached`
2. **Untracked Angular build cache** (25+ files) from `samples/dotnet-angular-cosmos/frontend/.angular/` via `git rm --cached`
3. **Untracked .codegen-hash** scaffold artifact from `samples/django-htmx-polls/.modules/` via `git rm --cached`
4. **Curated .gitignore** — appended new "Aspire Polyglot" section with 40+ patterns covering:
   - Python venvs + caches (.venv/, .mypy_cache/, .pytest_cache/, .ruff_cache/)
   - Java build outputs (*.class, target/, .gradle/, build/)
   - Frontend caches (.angular/, .svelte-kit/, .vite/, .next/, dist/, .tsbuildinfo)
   - Aspire runtime (.modules/.codegen-hash, *.aspire.run.log)
   - macOS/IDE junk (.DS_Store, .idea/, *.swp)
   - Local nuget cache (**/.nugetpackages/)

5. **Deleted untracked temp files from disk:**
   - .apphost.pid (root)
   - samples/svelte-go-bookmarks/aspire.run.log
   - .mypy_cache/ directories (samples root + django-htmx-polls)
   - __pycache__/ directories (all Python samples, excluding node_modules)
   - .pytest_cache/, .ruff_cache/ directories
   - samples/dotnet-angular-cosmos/.nugetpackages/ (local SDK cache)
   - .DS_Store files

**Commit Details:**
- SHA: a7e37f2
- Files changed: 117
- Insertions: 53 (.gitignore patterns)
- Deletions: 47,810 (build artifacts + cache files)
- Preserved: samples/vite-react-api/, samples/ts-starter/ (Parker's active work areas)

**OTel Work Preserved:** 
- .squad/agents/{banner,rogers,romanoff,thor}/history.md (unchanged in commit)
- OTel instrumentation changes to samples left staged for agent-driven commits

**Learnings:**
1. **Build/cache cruft accumulation pattern:** Frontend builds (.angular/cache, dist/), Java compilation (*.class), Python virtualenvs + __pycache__/ all accumulate rapidly across polyglot samples. Single-source .gitignore patterns prevent re-tracking.
2. **Staged file lifecycle:** `git rm --cached` immediately stages deletions (no extra `git add` needed). Crucial for scoping cleanup commits separate from feature work.
3. **Polyglot ignore patterns are language-stack specific:** Standard VisualStudio.gitignore template covers .NET/C# but misses Python venvs, Java compiler output, Node-based frontend caches. Appending curated "polyglot" section maintains team template + polyglot coverage.
4. **Node_modules danger:** Many untracked __pycache__ dirs live inside node_modules/ (transitive deps). Careful find predicates (-not -path "*/node_modules/*") prevent accidental traversal.
5. **Local cache directories should rarely be tracked:** .nugetpackages/, node_modules/.venv-like paths are sample-local caches, not shared dependencies. They should be on-disk only, regenerated per clone.

**Patterns to apply in future runs:**
- After infrastructure work (build/test runs): Run cleanup scan before committing
- Post-agent runs with file generation: Verify .squad history updates don't include transient agent logs
- Parker's active areas (vite-react-api, ts-starter) remain untouched per coordination — always check coordination notes before repo-wide changes
