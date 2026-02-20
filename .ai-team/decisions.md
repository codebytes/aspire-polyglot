# Decisions

> Team decisions that all agents must respect. Append-only — never edit or remove entries.

## 2026-02-20: Polyglot Samples Review Decisions

### 2026-02-20: Health Endpoint Standardization
**By:** Lando

**What:** All samples must expose a `/health` endpoint (not `/api/health`). The `django-htmx-polls` sample is missing this entirely. The `vite-react-api` FastAPI service uses `/api/health` instead of the standard `/health`.

**Why:** 
- Aspire requires `/health` endpoints for health monitoring and dashboard integration
- Consistency across the 7-sample portfolio is essential for teaching
- Simple to implement (one view/endpoint per sample)
- Teaches correct Aspire integration pattern

**Action:** Add Django health view to `django-htmx-polls`. Standardize `vite-react-api` health endpoint to `/health`. Verify all other samples expose `/health`.

---

### 2026-02-20: Django Vote Race Condition Fix
**By:** Wedge

**What:** The `django-htmx-polls` vote endpoint increments votes with `choice.votes += 1; choice.save()`, which loses votes under concurrent requests. Use Django `F()` expression for atomic increment.

**Why:**
- Textbook race condition causing data loss
- Essential teaching pattern for a demo app
- One-line fix using idiomatic Django
- Ensures vote counts are accurate under load

**Action:** Replace `choice.votes += 1; choice.save()` with `choice.votes = F('votes') + 1; choice.save()` in `polls/views.py`.

---

### 2026-02-20: Go API Port Environment Variable
**By:** Wedge

**What:** The Go bookmarks API hardcodes port `:8080` instead of reading the `PORT` environment variable. All other backend services read `PORT` from Aspire. Bring Go into line with the pattern.

**Why:**
- Consistency with every other sample (Python FastAPI, Node, .NET, Django all read `PORT`)
- Current hardcoding is fragile — only works because declared port matches
- Aligns with Aspire's environment wiring model
- Low-risk change

**Action:** Update Go API main.go to read `PORT` env var with fallback to `:8080`.

---

### 2026-02-20: Python Dependency Pinning & Lock Files
**By:** Bossk

**What:** Two samples have unpinned Python dependencies (`flask-markdown-wiki`, `vite-react-api`). No samples have lock files (`package-lock.json` or Python lock files). The `polyglot-event-stream` sample actively gitignores lock files.

**Why:**
- Unpinned dependencies make builds non-reproducible
- Other Python samples in the portfolio pin correctly
- Lock files are a deliberate trade-off for sample repos — should be documented as a choice
- Consistency and reproducibility matter for teaching samples

**Action:** 
1. Pin all unpinned Python deps in `flask-markdown-wiki` and `vite-react-api` to match pinned samples
2. Document in root README the deliberate choice not to commit lock files for these samples

---

### 2026-02-20: XSS Vulnerability in Flask Wiki
**By:** Bossk

**What:** The Flask wiki renders user-supplied Markdown to HTML and outputs with `{{ content|safe }}`. The `markdown` library does not sanitize HTML — users can inject `<script>` tags.

**Why:**
- Security vulnerability allowing arbitrary JavaScript injection
- Affects the teaching value of the sample
- Bleach library or markdown sanitization extensions are standard solutions
- Important to fix for a demo that may be deployed or studied

**Action:** Add `bleach` sanitization to `flask-markdown-wiki/src/main.py` OR use markdown's built-in sanitization extension to escape HTML in user content.

---

### 2026-02-20: Security Patterns Documentation (Demo Awareness)
**By:** Bossk

**What:** Multiple samples have demo-appropriate security trade-offs that should be explicitly documented:
1. Django hardcoded `SECRET_KEY = 'django-insecure-sample-key-change-in-production'`
2. All samples use `Access-Control-Allow-Origin: *` or equivalent (CORS allow-all)
3. No production-grade input validation or rate limiting

**Why:**
- Acceptable and intentional for demo/sample code
- But must be documented to prevent naive deployments
- Teaches the right mindset: "samples are not production-ready"
- Protects the team's credibility

**Action:** Add "Security Notes" section to root README documenting these as deliberate demo patterns. Link to hardening guides for production use.

---

### 2026-02-20: Frontend Code Quality — Deprecation & Maintenance
**By:** Biggs

**What:** Frontend issues across samples:
1. React sample uses deprecated `onKeyPress` (should be `onKeyDown`)
2. Svelte sample pinned to Svelte 4 with legacy `$:` reactive blocks (not broken, but outdated)
3. Flask wiki uses inline HTML templates as Python string constants (fragile for maintenance)
4. No error boundaries in any SPA — API failures silently console.error
5. No shared design system across samples

**Why:**
- Deprecated `onKeyPress` may stop working in future browser versions
- Svelte 4 → 5 teaches migration pattern (optional but good to show)
- Inline templates are hard to maintain and test
- Error boundaries improve demo reliability and teach error handling
- Visual coherence across the sample suite improves presentation

**Action (Priority Order):**
1. **High:** Replace `onKeyPress` with `onKeyDown` in React sample
2. **High:** Add basic error boundaries to React/Svelte/Angular samples
3. **Medium:** Move Flask templates to `templates/` folder for maintainability
4. **Low:** Consider Svelte 5 migration or document why Svelte 4 is intentional
5. **Low:** Create simple shared CSS/design system if samples are shown as a suite

---

### 2026-02-20: Aspire Integration Patterns
**By:** Lando

**What:** Several Aspire integration inconsistencies:
1. `svelte-go-bookmarks` uses manual environment wiring instead of `.WithReference`
2. `dotnet-angular-cosmos` has hardcoded proxy config in `proxy.conf.json`
3. `polyglot-event-stream` Python consumer has Flask endpoints but missing `WithHttpEndpoint`

**Why:**
- Inconsistency makes it harder to understand Aspire's integration model
- `.WithReference` is the idiomatic Aspire pattern
- Missing `WithHttpEndpoint` hides available endpoints from the Aspire dashboard
- Teaching clarity depends on pattern consistency

**Action:**
1. Document or refactor `svelte-go-bookmarks` — if manual wiring is intentional (Dockerfile limitation demo), explain in README. Otherwise, refactor to `.WithReference`
2. Add note to `dotnet-angular-cosmos` README explaining proxy limitation for Angular
3. Add `WithHttpEndpoint` to `polyglot-event-stream` Python consumer in AppHost

---

### 2026-02-20: Aspire SDK Version Lock
**By:** Lando

**What:** Aspire SDK version `9.2.1` is locked across all samples. Any future version bump must update all samples simultaneously to maintain consistency.

**Why:**
- Samples are teaching material — inconsistent versions confuse learners
- Coordinated upgrades prevent version drift
- Portfolio coherence is important for Aspire adoption examples

**Action:** Document this constraint in team decision log. Flag any future Aspire version bumps as "all or nothing" updates across the entire sample portfolio.
