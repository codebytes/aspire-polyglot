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
