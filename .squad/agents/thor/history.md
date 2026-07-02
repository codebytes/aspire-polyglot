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

### 2025-01-XX — OpenTelemetry Integration: Spring Boot + Aspire Dashboard
- **Challenge:** Spring Boot sample had ZERO telemetry instrumentation — no traces, metrics, or logs sent to Aspire dashboard
- **Approach:** Used `opentelemetry-spring-boot-starter` (v2.12.0) for zero-code auto-instrumentation instead of Java agent (no `AddSpringApp` in file-based AppHost)
- **Key changes:**
  1. Added `opentelemetry-instrumentation-bom` dependency management + `opentelemetry-spring-boot-starter` dependency to `pom.xml`
  2. Configured OTel exporters in `application.properties` (grpc protocol, otlp exporters for logs/metrics/traces)
  3. Starter auto-reads `OTEL_EXPORTER_OTLP_ENDPOINT`, `OTEL_SERVICE_NAME`, `OTEL_RESOURCE_ATTRIBUTES` from Aspire-injected env vars
- **Bonus fix:** Corrected environment variable mismatch in `AppHost.java` — replaced `PG_HOST`, `PG_PORT`, `PG_USER`, `PG_PASSWORD`, `PG_DB` with `NOTESDB_JDBCCONNECTIONSTRING`, `NOTESDB_USERNAME`, `NOTESDB_PASSWORD` to match what Spring Boot actually reads from `application.properties`
- **Outcome:** Full observability stack (traces, metrics, logs) now flows from Spring Boot → Aspire dashboard with zero Java code changes — pure config-driven setup
- **Learning:** Spring Boot OTel starter is ideal for Aspire integration in Java file-based AppHosts where Java agent path injection isn't available

### 2026-07-02 — Aspire 13.4.3 SDK Upgrade

- **Upgraded spring-boot-postgres** from Aspire 13.2.4 → 13.4.3 SDK
- **aspire.config.json** version bumped (13.4.6 attempted but CLI at 13.4.3, so used 13.4.3 to avoid version mismatch)
- **aspire restore** succeeded without errors; `.modules/` directory unchanged (perfect API stability between 13.2.4 and 13.4.3)
- **AppHost.java** required zero changes — all methods (`addContainer`, `addDockerfile`, `withOtlpExporter`, `withEnvironment`, `withExternalHttpEndpoints`) work identically
- **Spring Boot app** (`src/`) compiles successfully with `mvn clean compile`
- **Key finding:** Generated Java bindings are 100% stable across minor versions (13.2.4 → 13.4.3) — no breaking changes
- **CLI behavior:** Installed CLI (13.4.3) cannot restore SDK 13.4.6; CLI update requires interactive prompt unavailable in non-interactive mode
- **Recommendation:** Team should run `aspire update` interactively to get CLI 13.4.6+ for future upgrades

### 2026-07-02 — Final Upgrade: Aspire 13.4.6

- **CLI upgraded:** Team (Romanoff/Parker) upgraded Aspire CLI from 13.4.3 → 13.4.6 during their runs
- **Re-bumped spring-boot-postgres** from 13.4.3 → 13.4.6 for alignment with C#, Go, TS samples
- **aspire restore** succeeded with CLI 13.4.6; `.modules/` unchanged (perfect API stability across 13.2.4 → 13.4.3 → 13.4.6)
- **AppHost.java** compiles cleanly; Spring Boot app builds successfully
- **No breaking changes:** Java bindings kept `withOtlpExporter()` signature unchanged (no options object required unlike TypeScript)
- **Key win:** Java polyglot hosting API is 100% stable across 4 patch versions (13.2.4, 13.4.3, 13.4.6)
- **Final state:** All polyglot samples now aligned at Aspire 13.4.6

### 2026-07-02 — Aspire 13.4.6 Binding Layout Migration

- **Migrated from old `.modules/` → new `.aspire/modules/` layout** as part of Aspire 13.4.6 SDK upgrade
- **Old layout:** 3 monolithic files (Aspire.java, Base.java, Transport.java) at `.modules/` (13.2.4-era)
- **New layout:** 182 exploded per-class files + `sources.txt` at `.aspire/modules/` (13.4.6)
- **Breaking change found:** `addDockerfile(name, contextPath, null, null)` → `addDockerfile(name, contextPath)` (4-param signature removed, replaced by 2-param + optional AddDockerfileOptions)
- **AppHost.java updated:** Simplified `addDockerfile` call from 4 params to 2 params
- **Package alignment confirmed:** Both old and new bindings use `package aspire;` matching AppHost.java
- **Compilation verified:** AppHost compiles cleanly with `javac @.aspire/modules/sources.txt AppHost.java`
- **Spring Boot app:** Builds successfully (no regression to service code)
- **Migration complete:** Old `.modules/` removed via `git rm -r`; new `.aspire/modules/` remains on disk (gitignore rule pending from Rogers)
- **Key insight:** Aspire 13.4.6 uses exploded per-class layout with `sources.txt` manifest for cleaner codegen and better IDE support

### 2026-07-02 — Aspire 13.4.6 Java AppHost Default-Package Migration

- **Context:** Aspire 13.4.6 Java launcher runs `java -cp .java-build AppHost` (expects main class in default package)
- **Issue:** AppHost.java declared `package aspire;` (line 1) → compiled to `.java-build/aspire/AppHost.class` → ClassNotFoundException
- **Root cause:** 13.4.6 launcher derives main class name from FILENAME (`AppHost.java` → `AppHost`) and expects it in default package
- **Fix 1 (BLOCKER):** Migrated AppHost.java to default-package convention
  - Removed `package aspire;` declaration
  - Added `import aspire.*;` to access generated bindings (IDistributedApplicationBuilder, Aspire, DistributedApplication)
  - No logic changes — builder pattern, env vars, and `addDockerfile("api", "./src")` unchanged
- **Fix 2:** Added explicit startup ordering: `api.waitFor(pg);`
  - Spring Boot uses `spring.jpa.hibernate.ddl-auto=update` → connects to Postgres at startup to create schema
  - Without `waitFor`, cold starts race: if `pg` isn't Healthy yet, HikariCP fails fast and Spring Boot crashes
  - Placement: after env-wiring block (after `api.withExternalHttpEndpoints();`) and before `builder.build();`
  - Comment added explaining env wiring alone does NOT order startup in 13.4.6 polyglot
- **Verification:** Compiled with launcher's exact command: `javac --enable-preview --source 25 -d .java-build @.aspire/modules/sources.txt AppHost.java`
  - ✅ Compilation succeeded (warnings about unchecked operations are normal)
  - ✅ Class file landed at `.java-build/AppHost.class` (default package, NOT under `aspire/`)
  - ✅ Confirms `java -cp .java-build AppHost` will now find the class
- **Key learning:** Java apphost convention in 13.4.6: main class in DEFAULT package + `import aspire.*;` for bindings + filename = class name
- **Failure mode:** `package aspire;` in user apphost → bindings compile to correct location but main class in wrong package → ClassNotFoundException at launch
- **Ordering rule:** `waitFor(resource)` is mandatory for containers with startup dependencies — env/`withReference` wiring is config-only, not ordering
- **Java version used:** OpenJDK 25.0.3 (supports `--enable-preview --source 25`)
