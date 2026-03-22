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

**Status:** URGENT — Chris requests  
**Issue:** Slides do not cover `polyglotSupportEnabled` or language-specific feature flags  
**Decision:** Add new slide after TypeScript AppHost intro (after line 193)

**Content to Cover:**
- What `.aspire/settings.json` / `aspire.config.json` is
- `polyglotSupportEnabled: true` — master flag
- Language-specific flags: `experimentalPolyglot:python`, `:java`, `:go`
- How samples use these flags
- Why flags exist (experimental feature maturity)

**Owner:** Strange (Content Dev) — implement immediately

---

### 3. "What's New in 13.2" Slide Expansion

**Status:** URGENT — Critical gap identified  
**Issue:** Slides cover only 3 of 15+ key 13.2 features  
**Decision:** Expand to cover 8–10 major features across 2–3 slides

**Missing Features to Add:**
- `aspire.config.json` (new config format)
- `aspire run --detach`, `aspire ps`, `aspire stop` (detached mode)
- `aspire doctor`, `aspire restore`, `aspire agent`, `aspire docs`, `aspire resource` (new CLI commands)
- Dashboard enhancements (export, persistent state, resource graph)
- Bun support, Java cert trust, Foundry, MongoDB EF, Azure VNet

**Owner:** Strange (Content Dev) — implement in parallel with feature flags slide

---

### 4. Marp Theme Standardization

**Status:** Pending Chris approval  
**Issue:** Slides use `theme: custom-aspire-light` but charter specifies `theme: custom-default`  
**Decision Needed:** Is custom-aspire-light intentional (keep) or should align with charter (switch)?

**Notes:** custom-aspire-light has nice Aspire branding and looks professional. Keeping it is reasonable if Chris approves.

**Owner:** Chris Ayers (decision), Strange (implementation if change needed)

---

### 5. Java Connection String Format Handling

**Status:** URGENT — Functional issue  
**Issue:** Aspire injects PostgreSQL connection strings in .NET format (`Host=...;Port=...`), but Spring Boot expects JDBC URL format (`jdbc:postgresql://host:port/db`)  
**Decision Needed:** How to handle?

**Options:**
1. Add Spring `@Configuration` class to parse Aspire format and build JDBC DataSource
2. Ensure AppHost explicitly sets `POSTGRES_USER` and `POSTGRES_PASSWORD` env vars
3. Document expected behavior and fallback pattern

**Owner:** Thor (Java Dev) — investigate and implement fix before talk (functional blocker)

---

### 6. Infrastructure in Simple Samples

**Status:** Pending team consensus  
**Issue:** 3 samples lack Aspire-managed infrastructure (weakens orchestration narrative)  
**Affected Samples:**
- flask-markdown-wiki — uses SQLite only
- django-htmx-polls — uses SQLite only
- svelte-go-bookmarks — uses in-memory store

**Decision Needed:** Add Redis/PostgreSQL to these samples?

**Recommendation:** Add infrastructure for stronger demo impact. Flask and Django could add Redis for session cache. Go sample could add PostgreSQL for bookmark persistence.

**Owners:** Banner (Python), Romanoff (Go) — implement if approved

---

### 7. NuGet.config Standardization

**Status:** Pending decision  
**Issue:** Only Python polyglot samples have NuGet.config; Go and Java samples don't  
**Decision Needed:** Should all polyglot samples have it (if targeting preview feeds) or none?

**Owner:** Rogers (C#/.NET) to clarify with team

---

### 8. Root README AppHost Types

**Status:** CRITICAL — Documentation accuracy  
**Issue:** Root README lists wrong AppHost types for 3 samples  
**Decision:** Fix table entries

**Changes Needed:**
- vite-react-api: `apphost.cs` → `apphost.py`
- svelte-go-bookmarks: `apphost.cs` → `apphost.go`
- spring-boot-postgres: `apphost.cs` → `AppHost.java`

**Owner:** Any specialist (trivial fix) — implement immediately

---

## Governance

- All meaningful changes require team consensus
- Document architectural decisions here
- Keep history focused on work, decisions focused on direction
