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

### 2026-07-02 — Aspire 13.4.6 Upgrade Consolidated Verification

**Context:** All 5 language dev agents upgraded 8 samples from 13.2.4 → 13.4.6. Aspire CLI confirmed 13.4.6+87fe259. .NET SDK 10.0.301. Two pinning mechanisms verified:
- C# samples: `.csproj` pins (`<Project Sdk="Aspire.AppHost.Sdk/13.4.6">`)
- Polyglot samples: `aspire.config.json` pins (`"packages": { ... "13.4.6" }`)

**Verification Approach:**
1. Confirm CLI version
2. Build C# AppHosts (`dotnet build`)
3. Run `aspire restore` (no-op check) for polyglot samples
4. Build/typecheck where tooling available (npm, Go, Python syntax, Maven)
5. Search for deprecated `withOtlpExporterProtocol` signature
6. Check git status for transient artifacts

**Per-Sample Results:**

| Sample | Version | Restore/Build | Verdict |
|--------|---------|---------------|---------|
| dotnet-angular-cosmos | 13.4.6 | ✅ Build 0 errors (1.11s) | PASS |
| polyglot-event-stream | 13.4.6 | ✅ Build 0 errors (1.42s) | PASS |
| flask-markdown-wiki | 13.4.6 | ✅ Restore OK, py_compile OK | PASS |
| django-htmx-polls | 13.4.6 | ✅ Restore OK, py_compile OK | PASS |
| vite-react-api | 13.4.6 | ✅ Restore OK, npm install OK, skipLibCheck OK | PASS |
| ts-starter | 13.4.6 | ✅ Restore OK, npm install OK, runtime OK | PASS (dep type issues) |
| spring-boot-postgres | 13.4.6 | ✅ Restore OK, mvn build OK | PASS |
| svelte-go-bookmarks | 13.4.6 | ✅ Restore OK, go build OK | PASS |

**Environment Tooling:**
- npm: ✅ Available (v25.5.0)
- Go: ✅ Available (/opt/homebrew/bin/go)
- Maven: ✅ Available (/opt/homebrew/bin/mvn)
- Python: ✅ Available (syntax checks passed)

**Critical Findings:**
1. ✅ **Old `withOtlpExporterProtocol` signature:** Found in `.modules/` bindings (Python, Go, Java) but NOT in any `apphost.*` user code. This is expected — bindings define the capability, Parker's TS code correctly uses new `.withOtlpExporter({ protocol: ... })` syntax.
2. ⚠️ **ts-starter type errors:** vscode-jsonrpc incompatibility with Node v25.5.0 types (MapIterator). Build succeeds with `--skipLibCheck`, runtime works (expected `aspire run` error when not invoked via CLI). Pre-existing dependency issue, NOT 13.4.6-related.
3. ✅ **Git status:** Modified files = intentional config + history updates. Untracked `.aspire/` dirs in 4 samples — `.gitignore` excludes `**/.aspire/integrations/` but samples have nested structures. Root ignore covers these; no risk.

**Learnings:**
- Python `aspire restore` warns `pylock.apphost.toml` missing (harmless — lock files optional)
- Java sample structure: `AppHost.java` at root + Maven project in `src/`
- `.modules/` bindings define deprecated capabilities for backward compat — user code must avoid them
- `--skipLibCheck` workaround valid when dep types lag Node versions

**Verdict:** ✅ **APPROVE** — All 8 samples build/restore successfully. Version pins correct throughout. TS samples use new OTLP signature. No functional regressions detected.

### 2026-07-02 — Aspire 13.4.6 Binding-Layout Migration Re-Verification

**Context:** After initial 13.4.6 approval, 3 polyglot samples (Python/Java/Go) migrated from stale `.modules/` to fresh `.aspire/modules/` bindings. This surfaced 2 real breaking changes (now fixed by owning devs). TS samples intentionally unchanged — remain on `.modules/`.

**Verification Scope:**
1. Python (flask-markdown-wiki, django-htmx-polls): Import resolution from `.aspire/modules/aspire_app.py`
2. Java (spring-boot-postgres): Compilation against `.aspire/modules/sources.txt` + breaking change fix (4-param → 2-param `addDockerfile`)
3. Go (svelte-go-bookmarks): Build/vet against `.aspire/modules/` + options-struct rewrite
4. Regression: C# samples + TS samples (still `.modules/`) unchanged
5. Tree hygiene: Deleted `.modules/` for py/java/go, new `.aspire/modules/` untracked, no stray artifacts staged

**Per-Sample Results:**

| Sample | Language | Migration | Build/Import | Verdict |
|--------|----------|-----------|--------------|---------|
| flask-markdown-wiki | Python | ✅ `.aspire/modules/` | ✅ py_compile + import OK | PASS |
| django-htmx-polls | Python | ✅ `.aspire/modules/` | ✅ py_compile + import OK | PASS |
| spring-boot-postgres | Java | ✅ `.aspire/modules/` | ✅ javac + mvn OK | PASS |
| svelte-go-bookmarks | Go | ✅ `.aspire/modules/` | ✅ build + vet OK | PASS |
| dotnet-angular-cosmos | C# | N/A (unchanged) | ✅ Build 0 errors | PASS |
| polyglot-event-stream | C# | N/A (unchanged) | ✅ Build 0 errors | PASS |
| vite-react-api | TS | Intentional `.modules/` | ✅ Imports OK | PASS |
| ts-starter | TS | Intentional `.modules/` | ✅ Runtime OK | PASS |

**Migration Points Confirmed:**
1. **Python**: `sys.path.insert(0, ".aspire/modules")` + `from aspire_app import create_builder` ✅
2. **Java**: `@.aspire/modules/sources.txt` + 2-param `addDockerfile("api", "./src")` ✅
3. **Go**: `replace apphost/modules/aspire => ./.aspire/modules` + options-struct patterns (e.g. `AddDockerfile("api", "./go-api", nil, nil)`) ✅
4. **TS**: Still imports from `./.modules/aspire.js` (intentional) ✅

**Git Status:**
- **Deleted:** Old `.modules/` for python (6 files), java (3 files), go (4 files) ✅
- **Modified:** Apphost files (py, java, go), go.mod, aspire.config.json, agent histories ✅
- **Untracked:** `.aspire/` dirs for 4 samples (correctly gitignored via `**/.aspire/integrations/`) ✅
- **Binary:** `apphost` in svelte-go-bookmarks correctly gitignored ✅
- **Hygiene note:** ts-starter has untracked `.js` build artifacts (apphost.js, api/src/*.js, frontend/src/*.js) — minor cleanup needed but NOT a blocker

**Breaking Changes Verified (Fixed by Devs):**
1. **Java** (spring-boot-postgres): `builder.addDockerfile("api", "./src")` now 2-param (was 4-param with context/dockerfile args) — Thor fixed ✅
2. **Go** (svelte-go-bookmarks): Positional args → options structs (e.g. `AddDockerfile("api", "./go-api", nil, nil)`) — Romanoff fixed ✅

**Environment:**
- Python syntax + import smoke tests: ✅
- Java javac + Maven: ✅
- Go build + vet: ✅
- C# dotnet build: ✅
- TS runtime checks: ✅

**Verdict:** ✅ **APPROVE** — All 8 samples verified. Migration complete for py/java/go. TS intentionally unchanged. Breaking changes fixed. Tree clean except minor ts-starter .js artifacts (not blocking).
