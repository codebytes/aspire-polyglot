# Squad Decisions

## Active Decisions

### 1. Configuration Migration: `.aspire/settings.json` → `aspire.config.json`

**Status:** Pending team consensus  
**Affected Samples:** 5 non-.NET samples (flask-markdown-wiki, django-htmx-polls, vite-react-api, svelte-go-bookmarks, spring-boot-postgres)  
**Decision Needed:** Migrate now (before talk) or defer until after?

**Context:**
- Aspire 13.2 introduces `aspire.config.json` as the new config format
- Current format still works but is deprecated
- Affects string `"true"` → boolean `true` for feature flags
- All polyglot samples currently use `.aspire/settings.json`

**Recommendation:** Migrate all polyglot samples together for consistency. Test with `aspire restore` to regenerate SDK code.

**Owners:** Language specialists (Banner for Python, Parker for JS, Romanoff for Go, Thor for Java) + Scribe to coordinate

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

**Status:** ✅ RESOLVED — Strange expanded 1 slide to 2 slides  
**Issue:** Slides cover only 3 of 15+ key 13.2 features  
**Decision:** Expand to cover 8–10 major features across 2–3 slides

**Implementation:**
- Slide 13.2a: CLI enhancements (`aspire run --detach`, `aspire ps`, `aspire stop`, `aspire doctor`, `aspire restore`)
- Slide 13.2b: Configuration and infrastructure (`aspire.config.json`, dashboard persistence, resource graph)

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

**Status:** ✅ RESOLVED — Rogers upgraded all .NET packages  
**Issue:** Aspire packages 3+ versions behind (9.2.1 → 9.5.2)  
**Decision:** Upgrade all NuGet packages to latest stable within 9.x line

**Implementation:**
- Aspire packages: 9.2.1 → 9.5.2 (both AppHosts and all hosting packages)
- Supporting packages: Cosmos SDK 3.47.2 → 3.58.0, OpenTelemetry 1.10.x → 1.15.x, etc.
- Documentation fixes: `.AddDatabase()` → `.AddCosmosDatabase()`, version mentions updated
- Testing: `dotnet restore` succeeded, zero breaking changes in 9.2.1→9.5.2 range

**Owner:** Rogers (C#/.NET Dev) — ✅ COMPLETED

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

## Governance

- All meaningful changes require team consensus
- Document architectural decisions here
- Keep history focused on work, decisions focused on direction
