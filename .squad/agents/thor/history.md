# Thor — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** Java, Spring Boot, PostgreSQL, pgAdmin, Docker
- **Primary samples:** spring-boot-postgres

## Learnings

### 2025-07-18 — Deep Review of spring-boot-postgres sample
- **AppHost.java** follows correct Aspire Java SDK patterns: `addPostgres` → `withPgAdmin` → `addDatabase` → `addDockerfile` with `withReference`
- **Dockerfile** is well-built: multi-stage (maven build → JRE-only runtime), alpine variants, dependency caching via `dependency:go-offline`
- **Connection string injection** uses `ConnectionStrings__notesdb` in `application.properties` — name matches `addDatabase("notesdb")` in AppHost
- **Risk identified**: Aspire may inject .NET-format connection strings that don't match JDBC URL format. Needs investigation or a parsing `@Configuration` class
- **Risk identified**: `POSTGRES_USER`/`POSTGRES_PASSWORD` env vars fall back to `postgres`/`postgres` but Aspire generates random passwords
- **README bug**: References `apphost.cs` instead of `AppHost.java`
- **Config migration**: `.aspire/settings.json` should eventually move to `aspire.config.json` per Aspire 13.2 — no samples have migrated yet, needs team decision
- Spring Boot 3.4.1 is current enough but could be bumped for patches
- Controller uses field injection (`@Autowired`) — works but constructor injection is modern best practice
- Filed review findings to `.squad/decisions/inbox/thor-java-review.md`

### 2026-03-22 — Team Review Session Complete
- Team elevated Thor's connection string format risk to URGENT/BLOCKING (functional issue).
- **Action items routed to Thor:**
  - **URGENT:** Investigate and fix Java connection string format handling
    - Verify Aspire injects .NET-format (`Host=...;Port=...`) vs JDBC format (`jdbc:postgresql://...`)
    - Option 1: Create Spring `@Configuration` class to parse Aspire format → JDBC URL
    - Option 2: Ensure AppHost explicitly sets `POSTGRES_USER` and `POSTGRES_PASSWORD` env vars
    - Test end-to-end integration before talk
  - Fix README: Replace `apphost.cs` with `AppHost.java` (immediate)
  - Migrate `.aspire/settings.json` → `aspire.config.json` (timing TBD with team)
  - Update README version references (from "9.2.1")
  - Optional: Spring Boot version bump, constructor injection refactor
- **Blocker status:** Connection string format MUST be resolved before demo day (sample won't connect to PostgreSQL otherwise)

### 2026-03-22 — README Correction Complete
- spring-boot-postgres README: Fixed apphost.cs → AppHost.java reference
- Documentation now consistent with Java codebase
- Sample architecture documented correctly

### 2026-03-22 — Java Connection String Research COMPLETE
- **Key finding:** Aspire injects connection properties as INDIVIDUAL env vars by default (flags = `All`), including `NOTESDB_JDBCCONNECTIONSTRING` = `jdbc:postgresql://host:port/notesdb`
- **`ConnectionStrings__notesdb`** is in .NET format (`Host=...;Port=...`) — confirmed NOT usable as JDBC URL
- **`AddSpringApp` does NOT exist** — no Java-specific hosting API; all polyglot apps use `addDockerfile`
- **No parser needed!** Fix is a 3-line `application.properties` change:
  - `spring.datasource.url=${NOTESDB_JDBCCONNECTIONSTRING:...}` (was `ConnectionStrings__notesdb`)
  - `spring.datasource.username=${NOTESDB_USERNAME:postgres}` (was `POSTGRES_USER`)
  - `spring.datasource.password=${NOTESDB_PASSWORD:postgres}` (was `POSTGRES_PASSWORD`)
- JDBC connection string from Aspire does NOT include credentials (by design) — username/password are separate properties
- `WithCertificateTrustConfiguration` is for TLS trust stores only, not connection strings
- Full report filed to `.squad/decisions/inbox/thor-java-connstring-research.md`
- **Decision #5 can be resolved with a simple config change — no Java code additions required**

### 2026-03-22 — Decision #5 IMPLEMENTED: JDBC Connection String Fix
- **application.properties** updated: `ConnectionStrings__notesdb` → `NOTESDB_JDBCCONNECTIONSTRING`, `POSTGRES_USER` → `NOTESDB_USERNAME`, `POSTGRES_PASSWORD` → `NOTESDB_PASSWORD`
- **Demo 4 slide** updated: Removed "challenge/mismatch" framing, now shows Aspire auto-generates JDBC URLs natively — no parsing needed
- **README** updated: Connection string line now references `NOTESDB_JDBCCONNECTIONSTRING` env var
- Password default changed from `postgres` to empty string (Aspire always injects credentials; empty default prevents accidental hardcoded password usage)
- **Decision #5 is now RESOLVED** — functional blocker eliminated

### 2026-03-22 — Config Migration: .aspire/settings.json → aspire.config.json
- Migrated spring-boot-postgres from deprecated `.aspire/settings.json` to new `aspire.config.json` (Aspire 13.2 format)
- Key changes: `appHostPath` → `appHost.path` (relative to sample root), added `appHost.language: "java"`, added `sdk.version: "13.2.0"`, feature flags now boolean `true` not string `"true"`, removed `experimentalPolyglot:java` (master flag only)
- Deleted `.aspire/settings.json` and empty `.aspire/` directory
- New config lives at sample root (`samples/spring-boot-postgres/aspire.config.json`)
- **Decision #1 partially resolved** — spring-boot-postgres done, other samples still pending their language owners

## Team Coordination

### 2026-03-22 — Aspire 13.2 Upgrade Sprint (Orchestration Session)

**Team Pattern Established:** All language specialists (Banner, Parker, Thor, Romanoff) migrated their respective samples from `.aspire/settings.json` → `aspire.config.json` following a unified pattern in a single coordinated session.

**Config Migration Pattern (Standardized across all 5 samples):**
- Old: File at `.aspire/settings.json`, path relative to `.aspire/` directory with `../` prefix to AppHost
- New: File at sample root as `aspire.config.json`, path relative to sample root, no `../` prefix
- Old format: `"appHostPath": "../apphost.{lang}"`, `"polyglotSupportEnabled": "true"` (string), language-specific flags like `"experimentalPolyglot:java"`
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
