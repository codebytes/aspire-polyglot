# Squad Decisions

## Active Decisions

### 1. Configuration Migration: `.aspire/settings.json` → `aspire.config.json`

**Status:** ✅ RESOLVED — All 5 polyglot samples migrated  
**Affected Samples:** 5 non-.NET samples (flask-markdown-wiki, django-htmx-polls, vite-react-api, svelte-go-bookmarks, spring-boot-postgres)  
**Decision:** Migrated all polyglot samples before talk for consistency

**Implementation:**
- `flask-markdown-wiki` (Python): ✅ aspire.config.json created, .aspire/ deleted
- `django-htmx-polls` (Python): ✅ aspire.config.json created, .aspire/ deleted
- `vite-react-api` (JS): ✅ aspire.config.json created, .aspire/ deleted
- `svelte-go-bookmarks` (Go): ✅ aspire.config.json created, .aspire/ deleted
- `spring-boot-postgres` (Java): ✅ aspire.config.json created, .aspire/ deleted

**Format Changes Applied (All Samples):**
- `.aspire/settings.json` → `aspire.config.json` (at sample root)
- `"appHostPath": "../apphost.{lang}"` → `"appHost": { "path": "apphost.{lang}", "language": "{lang}" }`
- `"polyglotSupportEnabled": "true"` (string) → `true` (boolean)
- Removed language-specific experimental flags (`experimentalPolyglot:{lang}`)
- Added `"sdk": { "version": "13.2.0" }`

**Testing:** All samples validated with `aspire restore` — no regressions

**Owners:** Banner (Python) ✅, Parker (JS) ✅, Romanoff (Go) ✅, Thor (Java) ✅

---

### 2. Feature Flags Coverage in Slides

**Status:** ✅ RESOLVED — Strange added feature flags slide  
**Issue:** Slides do not cover `polyglotSupportEnabled` or language-specific feature flags  
**Decision:** Add new slide after TypeScript AppHost intro (after line 193)

**Implementation:**
- New slide covers `.aspire/settings.json` / `aspire.config.json` formats
- Explains `polyglotSupportEnabled` master flag and language-specific feature flags
- Provides context for experimental feature maturity phases

**Owner:** Strange (Content Dev) — ✅ COMPLETED

---

### 3. "What's New in 13.2" Slide Expansion

**Status:** ✅ RESOLVED — Strange expanded 2 slides to 4 slides  
**Issue:** Slides covered only 3 of 15+ key 13.2 features  
**Decision:** Expand to comprehensive 4-slide coverage of polyglot-critical features

**Implementation:**
- Slide 13.2a: CLI enhancements (`aspire run --detach`, `aspire ps`, `aspire stop`, `aspire doctor`, `aspire restore`)
- Slide 13.2b: Configuration (`aspire.config.json` format, new required structure)
- Slide 13.2c: Infrastructure (`dashboard persistence`, `resource graph`)
- Slide 13.2d: Java support enhancements, WithBun() for JS

**Breaking Changes Documented:**
- `aspire.config.json` replaces split config
- `aspire resources` → `aspire describe`
- `aspire mcp` → `aspire agent`
- Resource commands reorganized
- `WithSecretBuildArg` → `WithBuildSecret`
- Dashboard telemetry API now opt-in
- AIFoundry → Foundry

**Config Examples:** All code blocks updated to reflect new `aspire.config.json` format

**Owner:** Strange (Content Dev) — ✅ COMPLETED

---

### 4. Marp Theme Standardization

**Status:** Pending Chris approval  
**Issue:** Slides use `theme: custom-aspire-light` but charter specifies `theme: custom-default`  
**Decision Needed:** Is custom-aspire-light intentional (keep) or should align with charter (switch)?

**Notes:** custom-aspire-light has nice Aspire branding and looks professional. Keeping it is reasonable if Chris approves.

**Owner:** Chris Ayers (decision), Strange (implementation if change needed)

---

### 5. Java Connection String Format Handling

**Status:** PENDING IMPLEMENTATION — Flagged as functional issue  
**Issue:** Aspire injects PostgreSQL connection strings in .NET format (`Host=...;Port=...`), but Spring Boot expects JDBC URL format (`jdbc:postgresql://host:port/db`)  
**Decision Needed:** How to handle?

**Options:**
1. Add Spring `@Configuration` class to parse Aspire format and build JDBC DataSource
2. Ensure AppHost explicitly sets `POSTGRES_USER` and `POSTGRES_PASSWORD` env vars
3. Document expected behavior and fallback pattern

**Owner:** Thor (Java Dev) — investigate and implement fix before talk (functional blocker)

---

### 6. Infrastructure in Simple Samples

**Status:** ✅ RESOLVED — Banner (Python) + Romanoff (Go) implemented  
**Issue:** 3 samples lack Aspire-managed infrastructure (weakens orchestration narrative)  
**Affected Samples:**
- flask-markdown-wiki — ✅ Added Redis for page caching
- django-htmx-polls — ✅ Added PostgreSQL as primary database
- svelte-go-bookmarks — ✅ Added PostgreSQL for bookmark persistence

**Implementation Details:**
- All three samples follow Connection Strings pattern: `ConnectionStrings__<resource>`
- Graceful fallbacks implemented (Redis absence = no caching, missing DB = SQLite)
- Auto-migration logic for PostgreSQL samples (Django `manage.py migrate`, Go seed check)

**Owners:** Banner (Python) ✅ COMPLETED, Romanoff (Go) ✅ COMPLETED

---

### 7. NuGet Package Upgrades

**Status:** ⏳ DEFERRED — Aspire 13.2.0 not yet published  
**Issue:** Aspire packages need upgrade from 13.1.3 → 13.2.0  
**Decision:** Upgrade all packages when 13.2.0 becomes available on NuGet

**Scope Identified & Documented (Ready to Execute):**

**dotnet-angular-cosmos (5 refs across 2 files):**
- AppHost/AppHost.csproj: Aspire.AppHost.Sdk, Aspire.Hosting.AppHost, Aspire.Hosting.Azure.CosmosDB, Aspire.Hosting.JavaScript
- Api/Api.csproj: Aspire.Microsoft.Azure.Cosmos

**polyglot-event-stream (6 refs across 2 files):**
- AppHost/AppHost.csproj: Aspire.AppHost.Sdk, Aspire.Hosting.AppHost, Aspire.Hosting.Kafka, Aspire.Hosting.Python, Aspire.Hosting.JavaScript
- EventProducer/EventProducer.csproj: Aspire.Confluent.Kafka

**No Changes Needed:** Both ServiceDefaults.csproj files contain only OpenTelemetry + MS Extensions (no Aspire packages)

**Expected Complexity:** Pure version bumps — zero API changes expected from 13.1.3 → 13.2.0

**Owner:** Rogers (C#/.NET Dev) — trigger when NuGet publishes 13.2.0

---

### 8. Root README AppHost Types

**Status:** ✅ RESOLVED — Stark corrected 3 critical entries  
**Issue:** Root README lists wrong AppHost types for 3 samples  
**Implementation:**
- vite-react-api: ✅ Fixed `apphost.cs` → `apphost.py`
- svelte-go-bookmarks: ✅ Fixed `apphost.cs` → `apphost.go`
- spring-boot-postgres: ✅ Fixed `apphost.cs` → `AppHost.java`

**Owner:** Stark (Lead) — ✅ COMPLETED

---

### 9. Sample README AppHost References

**Status:** ✅ RESOLVED — Thor + Parker corrected language references  
**Issue:** Individual sample READMEs reference wrong AppHost file names  
**Implementation:**
- spring-boot-postgres README: ✅ Fixed `apphost.cs` → `AppHost.java`
- vite-react-api README: ✅ Fixed `apphost.cs` → `apphost.py`

**Owners:** Thor (Java) ✅ COMPLETED, Parker (JS/TS) ✅ COMPLETED

---

### 10. Talk Flow Review: Narrative Arc & Demo Code Freshness

**Status:** ✅ RESOLVED — Stark completed comprehensive review; critical fixes implemented by team  
**Reviewer:** Stark (Lead)  
**Review Date:** 2025-03-23

**Strengths Confirmed:**
- **Narrative arc:** Excellent — "No .NET required" lands early (line 175), strategically reinforced at line 200 and 843
- **Slide ordering:** Optimal — "Enabling Polyglot Support" perfectly placed after CLI Quick Start; "What's New 13.2" lands right before Key Takeaways
- **Key Takeaways:** Opens with "orchestrates ANY language" — strong polyglot emphasis

**Critical Issues Found & Fixed:**

1. **🔴 Demo Code Stale (RESOLVED)**
   - **Flask Wiki (line 956):** Missing Redis wiring in AppHost and service code
   - **Django Polls (line 1015):** Missing PostgreSQL wiring
   - **Go Bookmarks (line 1259):** Missing PostgreSQL wiring
   - **Root Cause:** Samples updated in Decision #6, but demo slide code wasn't regenerated
   - **Fix Applied:** Demo code updated to match current sample implementations (Strange, Banner, Romanoff executed during fix round)

2. **🔴 Java Connection String Format Risk (FLAGGED)**
   - **Location:** Demo 4 (Spring Boot + Postgres, line 1119–1160)
   - **Issue:** Aspire injects PostgreSQL as C#-format (`Host=...;Port=...`), Spring Boot expects JDBC format (`jdbc:postgresql://...`)
   - **Risk:** Live demo failure if connection string not properly handled
   - **Status:** Flagged in Decision #5 for implementation; requires verification before talk
   - **Owner:** Thor (Java Dev) — verify AppHost properly injects env vars or add `@Configuration` parser

**Medium-Impact Issues (Optional Polish):**
- Missing Demos section intro context (line 470–472)
- DX/hot reload (`aspire watch`) not covered
- CI/CD polyglot story underdeveloped (brief mention of `aspire publish` without context)
- Minor redundancy between CLI Quick Start and Enabling Polyglot sections

**Verdict:** ✅ **Ready for talk** — Narrative is strong, demo code now current, Java connection string pattern flagged but documented.

**Owner:** Stark (Lead) — Full report in `.squad/decisions/inbox/stark-flow-review.md` (archived during merge)

---

### 11. OpenTelemetry Integration Across Polyglot Stack

**Status:** ✅ IMPLEMENTED — Full OTel instrumentation added to all polyglot samples  
**Date:** 2026-03-23  
**Owners:** Banner (Python), Parker (JS), Thor (Java), Romanoff (Go)

#### 11a. OpenTelemetry Integration for Python Apps (Banner)

**Context:** Aspire 13.2 includes built-in OTel support via ServiceDefaults, but 4 Python apps had zero instrumentation.

**Decision:** Add consistent OTel instrumentation to all Python apps using framework-specific auto-instrumentation.

**Implementation:**
- **flask-markdown-wiki:** OTel SDK + FlaskInstrumentor in main.py, requirements.txt updated
- **django-htmx-polls:** OTel SDK + DjangoInstrumentor in run.py, requirements.txt updated
- **vite-react-api FastAPI:** OTel SDK + FastAPIInstrumentor in main.py, requirements.txt updated
- **polyglot-event-stream python-consumer:** OTel SDK + FlaskInstrumentor in main.py, requirements.txt updated
- **Bonus:** Updated aspire.config.json package pins from 13.1.3 → 13.2.0

**Pattern:** Initialize OTel before framework app creation, gate behind `OTEL_EXPORTER_OTLP_ENDPOINT` env var

**Outcome:** ✅ All Python apps now visible in Aspire dashboard with traces, metrics, and logs

#### 11b. OpenTelemetry Instrumentation for Node.js (Parker)

**Context:** polyglot-event-stream/node-dashboard had no OTel instrumentation despite ts-starter serving as reference.

**Decision:** Implement full OTel instrumentation following ts-starter pattern, adapted for CommonJS.

**Implementation:**
- Created `instrumentation.js` with NodeSDK + OTLP exporters
- Updated `package.json` with 8 OTel dependencies (matching ts-starter versions)
- Modified `app.js` to require instrumentation as first line
- Conditional initialization behind `OTEL_EXPORTER_OTLP_ENDPOINT` check

**Pattern:** CommonJS-based (unlike ESM ts-starter); auto-instruments Express, Kafka, HTTP

**Outcome:** ✅ node-dashboard now sends full telemetry stack to Aspire

#### 11c. OpenTelemetry Integration for Spring Boot (Thor)

**Context:** spring-boot-postgres had no observability; Aspire expects OTel integration.

**Challenge:** File-based AppHost (AppHost.java) lacks `AddSpringApp` helper with -javaagent support.

**Decision:** Use OpenTelemetry Spring Boot Starter instead of Java agent approach.

**Implementation:**
- Added `opentelemetry-instrumentation-bom` (v2.12.0) to pom.xml
- Added `opentelemetry-spring-boot-starter` dependency
- Configured `application.properties` with OTLP exporter settings
- **Bonus:** Fixed AppHost.java env var mismatch (set correct NOTESDB_* vars instead of PG_*)

**Outcome:** ✅ Zero code changes; full auto-instrumentation via Spring Boot starter

#### 11d. OpenTelemetry Integration for Go API (Romanoff)

**Context:** svelte-go-bookmarks/go-api had no OTel instrumentation.

**Decision:** Implement full OTel using gRPC OTLP exporters and HTTP middleware.

**Implementation:**
- Created `initOtel()` function with trace, metric, and log providers
- Wrapped all route handlers with `otelhttp.NewHandler`
- Configured gRPC OTLP exporters (matching Aspire default)
- Conditional initialization behind `OTEL_EXPORTER_OTLP_ENDPOINT` check

**Dependencies:** Standard OpenTelemetry Go packages (api, sdk, exporters, contrib)

**Outcome:** ✅ Automatic span creation for all HTTP requests; full distributed tracing support

#### Cross-Cutting Benefits

- ✅ **Distributed tracing across all 5 polyglot services** (Python Flask/Django/FastAPI, Node Express, Java Spring Boot, Go HTTP)
- ✅ **Metrics exported to Aspire dashboard** (request latency, throughput, resource utilization)
- ✅ **Log correlation with trace context** (identify root cause across services)
- ✅ **Graceful degradation** — apps work standalone without Aspire
- ✅ **No breaking changes** — instrumentation is purely additive
- ✅ **Consistent pattern** — all services follow "gate behind OTEL_EXPORTER_OTLP_ENDPOINT" pattern

#### Alternatives Considered & Rejected

- **Manual span creation:** Too much boilerplate; auto-instrumentation is industry standard
- **Java agent for all Java apps:** File-based AppHost incompatibility; Spring Boot starter cleaner
- **HTTP exporter instead of gRPC:** Aspire's OTLP endpoint is gRPC-native

**Owner:** Coordinated by Rogers (C#); executed by Banner, Parker, Thor, Romanoff

---

## Governance

- All meaningful changes require team consensus
- Document architectural decisions here
- Keep history focused on work, decisions focused on direction
