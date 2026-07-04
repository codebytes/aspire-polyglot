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

### 2026-05-07 — Post-OTel Validation: Polyglot .gitignore Refinement + Artifact Cleanup

**Task:** Deep cleanup of `samples/` following agent OTel work. Prior cleanup (a7e37f2, ca4e46c, d2be533) added polyglot section but missed pattern anchoring. `.modules/.codegen-hash` pattern was unanchored (didn't match `samples/{sample}/.modules/.codegen-hash`). Also: Vite TS compile artifacts (`vite.config.d.ts`, `vite.config.js`) not yet covered.

**Execution Summary:**

1. **Identified pattern anchoring gap:** Prior .gitignore used `*.aspire.run.log`, `aspire.run.log`, `.modules/.codegen-hash` — unanchored patterns that failed to match files nested under `samples/` tree.

2. **Converted all Aspire Polyglot patterns to `**/` glob anchors:**
   - `.modules/.codegen-hash` → `**/.modules/.codegen-hash`
   - `*.aspire.run.log`, `aspire.run.log` → `**/aspire.run.log`
   - All Python, Java, frontend patterns similarly anchored (e.g., `.venv/` → `**/.venv/`)

3. **Added missing Vite compile artifacts:**
   - `**/vite.config.d.ts` (TypeScript declaration auto-generated by tsc)
   - `**/vite.config.js` (compiled output when `vite.config.ts` source exists)

4. **Deleted untracked build artifacts:**
   - 6 `.codegen-hash` files (Aspire codegen hashes — regenerate on each `aspire run`)
   - 2 `vite.config.js` files (compiled Vite configs)
   - 4 `.tsbuildinfo` files (TypeScript incremental build metadata)

5. **Verified OTel source work remains untracked and legitimate:**
   - 10 untracked files: all agent OTel additions (otel.ts, instrumentation.ts, logback-spring.xml, package-lock.json, tsconfig.apphost.json, scripts/, assets/)
   - 44 modified files: legitimate apphost, config, package, resource changes
   - 5 deleted (staged as D): build artifacts from deletion

**Commit Details:**
- SHA: 05446da
- Pattern scope: Aspire Polyglot section only (root .gitignore, no samples/ file changes)
- Files changed: 1 (.gitignore)
- Deletions: .codegen-hash, vite compile artifacts (from disk and staged)

**Learnings:**

1. **Glob anchoring is critical for polyglot repos:** Unanchored patterns in a monorepo with multi-language samples can silently fail to catch artifacts nested under sample subdirs. Testing `.modules/.codegen-hash` vs. `**/.modules/.codegen-hash` revealed the gap — always verify patterns match the deepest nesting level expected.

2. **Vite compile artifacts are often missed:** Vite's `.d.ts` and `.js` are generated from TypeScript sources (`vite.config.ts`). Unlike Webpack or tsc, Vite doesn't emit a fixed output directory name — output files sit alongside source. Two-part pattern needed: (1) verify source exists, (2) add pattern. Manual review beats blind glob (risk of deleting hand-written .js configs).

3. **Regenerating files must never be tracked:** `.codegen-hash`, `.tsbuildinfo`, Vite compile output all regenerate on tool runs. Tracking them causes constant "modified" noise and merge conflicts. Workflow: (1) add to .gitignore immediately, (2) do NOT commit once untracked, (3) delete from disk, (4) verify clean status.

4. **Build artifacts branch-scoped:**  When multiple agent branches generate artifacts independently, post-merge cleanup needs cross-branch scope check. This task verified 10 untracked source files (legitimate OTel work) vs. build cruft — only OTel work should remain after cleanup + ignore fix.

5. **Index (staged deletions) behavior:** Deleted files show as `D` in `git status -s` after deletion from disk. This is normal and expected. Stage them with `git add` after confirming they're build artifacts, NOT source code.

**Trap: `find -delete` without `git add -u`:** Using `find` to delete files from disk bypasses git's index. Files deleted this way remain *tracked* until staged with `git add -u -- <pattern>`. After physical deletion, use `git add -u` (not bare `git add`) to stage the removal — common pattern when batch-deleting build artifacts.

### 2026-07-04 — Aspire 13.4 Slide Validation + Visual QA

**Task:** Validate and update `slides/Slides.md` against the official Aspire 13.4 release notes (aspire.dev/whats-new/aspire-13-4) and current guidance. Scope: TypeScript AppHost, polyglot/multi-language support, cloud/hosting/deployment. All corrections folded into EXISTING slides (no "What's New in 13.4" slide added). Followed by an additive style/visual overflow QA pass. Delegated task from coordinator session "Aspire 13.4 slide review".

**Branch:** `codebytes-aspire-slide-refresh` (worktree folder: `codebytes-symmetrical-happiness`)

**Content corrections applied (commit 7b4839e):**
1. TypeScript AppHost import path → `import { createBuilder } from "./.aspire/modules/aspire.mjs";` (2 slides: The AppHost — TypeScript; Use From TypeScript — Zero Bindings)
2. Entry point `apphost.ts` → `apphost.mts` (Two AppHost Languages)
3. Generated SDK location `./.modules/` → `./.aspire/modules/`; `my-integration.js` → `my-integration.mjs`
4. TypeScript AppHost status: "preview feature" → "generally available in Aspire 13.4 — TypeScript AppHosts are first-class alongside C#"
5. Cheat Sheet — Runtimes: added Bun → `AddBunApp()`; Go → `AddGoApp()` (now core; dropped Community Toolkit tag); Java/Spring `AddSpringApp()` left as Community Toolkit (unchanged in 13.4)
6. `aspire.config.json`: sdk version `13.2.0` → `13.4.0`
7. "Same Model, Two Commands": removed `(Preview)` from `aspire deploy` and `aspire publish` (both GA in 13.4); KEPT `(Preview)` on `aspire do`
8. Deployment slide: enhanced AKS ingress guidance — cert-manager HTTPS, Gateway API + Azure Application Gateway for Containers (AGC), external Helm charts via `AddHelmChart`
9. Demo cleanup: marked Go/Java/Python "AppHost" labels *(preview)* to reconcile with the C#/TypeScript-only GA AppHost framing stated elsewhere (Go/Python/Java are hosted workloads; polyglot AppHost authoring is still preview). C#/TypeScript demo labels untouched.

**Verification:**
- Marp CLI build: exit 0. 50 source slides → 56 rendered `<section>` elements (6 extra from advanced-background `![bg]` layers — expected). Theme `custom-aspire-light` + all `_class`/columns/HTML-comment speaker-note structure preserved.
- Grep sweep confirmed no stale references remained (`AddGolangApp`, `.modules/aspire.js`, `apphost.ts`, `13.2.0`, "preview feature").

**Visual / overflow QA (additive pass, no content change):**
- Rendered all slides to PNG (`--images png --allow-local-files`) → exit 0, 50 images.
- Playwright per-`<section>` overflow measurement: neutralized `overflow:hidden` clipping per section (height:auto, overflow:visible), forced reflow, measured natural content height + widest child right-edge vs. 1280×720 bounds (4px tolerance) → **0 offenders** across all 56 sections.
- Visual spot-check of every high-density / edited slide (code blocks, 2×2 language grids, both cheat sheets, config field reference, Agent-Ready CLI, 8 Live Demos, demo diagrams, Key Takeaways, Resources/QR): no clipping, no truncated code, no column collisions.
- Result: **NO style fixes required** → no separate style commit was made.

**Decisions inbox:** empty (0 entries; no `.squad/decisions/inbox/` dir) — nothing to merge.

**Commits:**
- `7b4839e` — content corrections (`slides/Slides.md`), with Co-authored-by: Copilot App trailer
- (this) — `docs(.squad)`: session log entry

**Learnings:**
1. **Marp section count ≠ slide count:** advanced-background slides (`![bg ...]`) each emit an extra background `<section>`. 50 slides rendered as 56 sections — expected, not new slides. Always reconcile before flagging a count mismatch.
2. **`scrollHeight` lies under `overflow:hidden` + fixed-height sections:** Marp sections are fixed 1280×720 with `overflow:hidden`, so `scrollHeight === clientHeight` (720) always and hides real overflow. To detect true overflow, neutralize clipping per section and measure natural content extent + widest child right-edge.
3. **PNG export drops local images by default:** portrait/logo/architecture assets need `--allow-local-files` to render in exported PNGs; otherwise they silently drop with a security warning (making a "clipped" slide look like missing art).
4. **Branch vs. worktree-folder divergence:** this worktree folder is `codebytes-symmetrical-happiness` (original generated name shown in the session-context header) but the checked-out branch was renamed to `codebytes-aspire-slide-refresh`. Always confirm the real branch with `git branch --show-current` before committing/reporting — the header name can be stale.
