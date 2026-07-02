---
name: "aspire-polyglot-go-apphost"
description: "Go AppHost patterns for Aspire 13.4.6+ polyglot orchestration"
domain: "aspire, go, orchestration"
confidence: "high"
source: "earned (13.4.6 migration, cold-start fix)"
---

## Context

Aspire 13.4.6 introduced significant breaking changes in the Go SDK. The API shifted from tuple-based returns `(resource, error)` to single-resource returns with internal error tracking, positional arguments to options-struct patterns, and helper function removals. Additionally, manual env var wiring (e.g., `PG_HOST`, `PG_USER`, `PG_PASSWORD`) does NOT establish startup ordering — `WaitFor` is required to prevent cold-start race conditions.

## Patterns

### 1. Builder Creation and Resource Addition (13.4.6 API)

**CreateBuilder** still returns `(builder, error)`, but resource addition methods now return only the resource:

```go
import (
    "log"
    "apphost/modules/aspire"
)

func main() {
    builder, err := aspire.CreateBuilder(nil)
    if err != nil {
        log.Fatalf("Failed to create builder: %v", err)
    }

    // ✅ Resource methods now return only the resource (no error tuple)
    pg := builder.AddContainer("pg", "postgres:16")
    api := builder.AddDockerfile("api", "./go-api", nil, nil)
    frontend := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})

    // Builder errors are tracked internally and checked at Build() time
    app, err := builder.Build()
    if err != nil {
        log.Fatalf("Failed to build: %v", err)
    }

    if err := app.Run(nil); err != nil {
        log.Fatalf("Failed to run: %v", err)
    }
}
```

### 2. WithHttpEndpoint Options-Struct Pattern

**OLD (13.2.4):** Positional args `(name, port, targetPort, scheme, env)`
**NEW (13.4.6):** Options-struct `&WithHttpEndpointOptions{...}`

```go
// Container endpoint (PostgreSQL on port 5432)
pg := builder.AddContainer("pg", "postgres:16")
pgPort := 5432.0
pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &pgPort,
    Name:       aspire.StringPtr("tcp"),
})

// API endpoint (HTTP on port 8080)
api := builder.AddDockerfile("api", "./go-api", nil, nil)
apiPort := 8080.0
api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &apiPort,
    Name:       aspire.StringPtr("http"),
})

// Frontend endpoint with env injection (Vite dev server on port 5173)
frontend := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})
frontendPort := 5173.0
frontend.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &frontendPort,
    Name:       aspire.StringPtr("http"),
    Env:        aspire.StringPtr("PORT"),
})
```

**Note:** Use `aspire.StringPtr("value")` helper for pointer fields. Custom `stringPtr` helpers from 13.2.4 are obsolete.

### 3. GetEndpoint and WithEnvironment (Simplified API)

**GetEndpoint** now returns `EndpointReference` directly (no error tuple):

```go
// ✅ NEW (13.4.6): Direct return, no error handling
pgEndpoint := pg.GetEndpoint("tcp")
api.WithEnvironment("PG_HOST", pgEndpoint)

apiEndpoint := api.GetEndpoint("http")
frontend.WithEnvironment("services__api__http__0", apiEndpoint)
```

**OLD (13.2.4):** `pgEndpoint, _ := pg.GetEndpoint("tcp")` — required tuple unpacking.

### 4. WaitFor for Startup Ordering (MANDATORY for Datastores)

**CRITICAL:** In Aspire 13.4.6 polyglot, manual env var wiring (`PG_HOST`, `PG_USER`, `PG_PASSWORD`) only injects connection strings — it does NOT order startup. Without `WaitFor`, apps can start before datastores are ready, causing connection failures or silent fallbacks.

```go
// PostgreSQL container
pg := builder.AddContainer("pg", "postgres:16")
pg.WithEnvironment("POSTGRES_USER", "postgres")
pg.WithEnvironment("POSTGRES_PASSWORD", "postgres")
pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")
pgPort := 5432.0
pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &pgPort,
    Name:       aspire.StringPtr("tcp"),
})

// API with manual PG connection string wiring
api := builder.AddDockerfile("api", "./go-api", nil, nil)
pgEndpoint := pg.GetEndpoint("tcp")
api.WithEnvironment("PG_HOST", pgEndpoint)
api.WithEnvironment("PG_USER", "postgres")
api.WithEnvironment("PG_PASSWORD", "postgres")
api.WithEnvironment("PG_DB", "bookmarksdb")
api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &apiPort,
    Name:       aspire.StringPtr("http"),
})
api.WithExternalHttpEndpoints()

// ✅ MANDATORY: WaitFor ensures API never starts until Postgres is Healthy
// Without this, on a cold start (uncached postgres:16 image), the API can
// start before Postgres is ready, fail its connection Ping, and silently
// fall back to an in-memory store (if fallback logic exists in the app).
api.WaitFor(pg)
```

**Pattern:**
1. Wire connection string via manual env vars (`PG_HOST`, etc.)
2. **IMMEDIATELY** add `api.WaitFor(pg)` after wiring
3. Then add OTLP, endpoints, and other config

**WaitFor signature:**
```go
func (s *containerResource) WaitFor(dependency Resource, options ...*WaitForOptions) ContainerResource
```

Returns the resource for chaining. The `Resource` interface is satisfied by `ContainerResource` (from `AddContainer`, `AddDockerfile`, `AddExecutable`), `DatabaseResource` (from `AddDatabase`), and other managed resources.

### 5. WithOtlpExporter (Void Return)

**WithOtlpExporter** now returns the resource for chaining (not `(resource, error)`):

```go
// ✅ NEW (13.4.6): Chainable, no error handling
api.WithOtlpExporter()
api.WithExternalHttpEndpoints()

// OR chain directly
api.WithOtlpExporter().WithExternalHttpEndpoints()
```

### 6. Complete AppHost Pattern (svelte-go-bookmarks)

```go
package main

import (
    "log"
    "apphost/modules/aspire"
)

func main() {
    builder, err := aspire.CreateBuilder(nil)
    if err != nil {
        log.Fatalf("Failed to create builder: %v", err)
    }

    // PostgreSQL container
    pg := builder.AddContainer("pg", "postgres:16")
    pg.WithEnvironment("POSTGRES_USER", "postgres")
    pg.WithEnvironment("POSTGRES_PASSWORD", "postgres")
    pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")
    pgPort := 5432.0
    pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
        TargetPort: &pgPort,
        Name:       aspire.StringPtr("tcp"),
    })

    // Go API via Dockerfile
    api := builder.AddDockerfile("api", "./go-api", nil, nil)
    api.WithOtlpExporter()
    // Manual connection string wiring (plain containers don't support WithReference)
    pgEndpoint := pg.GetEndpoint("tcp")
    api.WithEnvironment("PG_HOST", pgEndpoint)
    api.WithEnvironment("PG_USER", "postgres")
    api.WithEnvironment("PG_PASSWORD", "postgres")
    api.WithEnvironment("PG_DB", "bookmarksdb")
    apiPort := 8080.0
    api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
        TargetPort: &apiPort,
        Name:       aspire.StringPtr("http"),
    })
    api.WithExternalHttpEndpoints()
    // ✅ Ensure API waits for Postgres to be Healthy before starting
    api.WaitFor(pg)

    // Svelte frontend via npm
    frontend := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})
    apiEndpoint := api.GetEndpoint("http")
    frontend.WithEnvironment("services__api__http__0", apiEndpoint)
    frontendPort := 5173.0
    frontend.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
        TargetPort: &frontendPort,
        Name:       aspire.StringPtr("http"),
        Env:        aspire.StringPtr("PORT"),
    })
    frontend.WithExternalHttpEndpoints()
    // WaitFor establishes both startup ordering and a Reference relationship
    // visible on the dashboard. (Plain ContainerResources like api can't be
    // used with WithReference — they don't expose a connection string.)
    frontend.WaitFor(api)

    app, err := builder.Build()
    if err != nil {
        log.Fatalf("Failed to build: %v", err)
    }

    if err := app.Run(nil); err != nil {
        log.Fatalf("Failed to run: %v", err)
    }
}
```

## Examples

### PostgreSQL + Go API + Svelte Frontend (svelte-go-bookmarks)

**File:** `samples/svelte-go-bookmarks/apphost.go`

**Pattern:**
1. `AddContainer` for PostgreSQL with env vars
2. `AddDockerfile` for Go API with manual PG_* env wiring
3. **`api.WaitFor(pg)`** to enforce startup ordering
4. `AddExecutable` for Svelte frontend (npm)
5. `frontend.WaitFor(api)` to ensure API is ready before frontend

**Key insight:** Manual env wiring for plain containers (`AddContainer`) doesn't support `WithReference`, so you wire env vars manually (`PG_HOST`, etc.) AND add `WaitFor` for startup ordering.

## Anti-Patterns

### ❌ OLD 13.2.4 API (Tuple Returns + Positional Args)

```go
// ❌ BREAKS in 13.4.6: AddContainer returns resource only (no error tuple)
pg, err := builder.AddContainer("pg", "postgres:16")
if err != nil {
    log.Fatalf("Failed to add container: %v", err)
}

// ❌ BREAKS in 13.4.6: WithHttpEndpoint uses positional args
pg.WithHttpEndpoint("tcp", 5432.0, 5432.0, "", nil)

// ❌ BREAKS in 13.4.6: GetEndpoint returns tuple
pgEndpoint, err := pg.GetEndpoint("tcp")
if err != nil {
    log.Fatalf("Failed to get endpoint: %v", err)
}
```

**Why this fails:** The 13.4.6 API removed tuple returns for resource methods, moved to options-struct pattern for endpoints, and simplified GetEndpoint.

### ❌ Missing WaitFor (Silent Cold-Start Failure)

```go
// ❌ WRONG: Manual env wiring without WaitFor
pgEndpoint := pg.GetEndpoint("tcp")
api.WithEnvironment("PG_HOST", pgEndpoint)
api.WithEnvironment("PG_USER", "postgres")
api.WithEnvironment("PG_PASSWORD", "postgres")
api.WithEnvironment("PG_DB", "bookmarksdb")
// ❌ NO api.WaitFor(pg) — API can start before Postgres is ready!
```

**Why this fails:** On a cold start (uncached `postgres:16` image), Docker pull can take 10+ seconds. The API starts before Postgres is Healthy, connection Ping fails, and the app silently falls back to an in-memory store (if fallback logic exists). This creates a silent data-integrity trap.

**Correct:**
```go
api.WithEnvironment("PG_HOST", pgEndpoint)
api.WithEnvironment("PG_USER", "postgres")
api.WithEnvironment("PG_PASSWORD", "postgres")
api.WithEnvironment("PG_DB", "bookmarksdb")
api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &apiPort,
    Name:       aspire.StringPtr("http"),
})
api.WithExternalHttpEndpoints()
api.WaitFor(pg)  // ✅ MANDATORY: Ensures Postgres is Healthy first
```

### ❌ Custom Pointer Helpers (Obsolete in 13.4.6)

```go
// ❌ OLD: Custom pointer helpers from 13.2.4
func stringPtr(s string) *string { return &s }
func float64Ptr(f float64) *float64 { return &f }

pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    Name: stringPtr("tcp"),  // ❌ Obsolete helper
})
```

**Why this fails:** The SDK now provides `aspire.StringPtr`, `aspire.IntPtr`, etc. Custom helpers are redundant.

**Correct:**
```go
pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    Name: aspire.StringPtr("tcp"),  // ✅ Use SDK helpers
})
```

### ❌ Legacy Binding Layout (.modules/ → .aspire/modules/)

**go.mod** (OLD):
```go
replace apphost/modules/aspire => ./.modules  // ❌ Deprecated location
```

**go.mod** (NEW):
```go
replace apphost/modules/aspire => ./.aspire/modules  // ✅ 13.4.6 layout
```

**Why this fails:** The 13.4.6 binding layout moved from `.modules/` (373KB aspire.go) → `.aspire/modules/` (694KB aspire.go, nearly 2x size with richer API surface).

## Verification

**Build check:**
```bash
cd samples/svelte-go-bookmarks
go build ./...
```

**Lint check:**
```bash
cd samples/svelte-go-bookmarks
go vet ./...
```

**Format check:**
```bash
cd samples/svelte-go-bookmarks
gofmt -l apphost.go  # Should print nothing
```

**Runtime launch** (from sample root):
```bash
cd samples/svelte-go-bookmarks
aspire run
# Expect: dashboardUrl in output, resources start in correct order
```

## Cold-Start Race Condition: WaitFor for Datastore Dependencies

### Problem

App containers can start BEFORE their datastores are reachable on cold launches (when images must pull or containers must initialize). This causes connection failures, silent fallbacks to in-memory stores, or application crashes.

**Example failure** (svelte-go-bookmarks on cold launch):
```
WARN: Failed to connect to Postgres at pg.dev.internal:5432 — falling back to in-memory store
```

**Root cause:** Manual env var wiring (`PG_HOST`, `PG_USER`, `PG_PASSWORD`) only injects connection strings — it does NOT establish startup ordering.

### Solution

**ALWAYS add `.WaitFor()` after manual env var wiring when your app depends on a datastore:**

```go
// PostgreSQL container
pg := builder.AddContainer("pg", "postgres:16")
pg.WithEnvironment("POSTGRES_USER", "postgres")
pg.WithEnvironment("POSTGRES_PASSWORD", "postgres")
pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")
pgPort := 5432.0
pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &pgPort,
    Name:       aspire.StringPtr("tcp"),
})

// API with manual PG connection string wiring
api := builder.AddDockerfile("api", "./go-api", nil, nil)
pgEndpoint := pg.GetEndpoint("tcp")
api.WithEnvironment("PG_HOST", pgEndpoint)
api.WithEnvironment("PG_USER", "postgres")
api.WithEnvironment("PG_PASSWORD", "postgres")
api.WithEnvironment("PG_DB", "bookmarksdb")
apiPort := 8080.0
api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
    TargetPort: &apiPort,
    Name:       aspire.StringPtr("http"),
})
api.WithExternalHttpEndpoints()
// ✅ Ensure API waits for Postgres to be Healthy before starting — env var
// wiring alone does NOT order startup in Aspire 13.4.6 polyglot. Without
// this WaitFor, on a cold start (uncached postgres:16 image), the API can
// start first, fail its Postgres Ping, and silently fall back to the
// in-memory store.
api.WaitFor(pg)
```

### When to Use WaitFor

**Use `.WaitFor()` when:**
- Your app connects to the datastore immediately on launch (no retry logic)
- Your app has a silent fallback mechanism (in-memory store) that would mask the cold-start failure
- Your app runs migrations at startup (schema setup, seed data)
- Your app cannot gracefully handle "connection refused" or DNS failures

**Applies to:**
- PostgreSQL (`AddContainer("pg", "postgres:16")` with manual env wiring)
- Redis (`AddRedis` when using `AddContainer` instead)
- MySQL (`AddContainer("mysql", "mysql:8")` with manual env wiring)
- Any custom container that the app depends on

### Pattern

```go
db := builder.AddContainer("<db-name>", "<db-image>")  // OR AddRedis, AddPostgres, etc.
app := builder.AddDockerfile("app", "./src", nil, nil)
// Manual env wiring (for plain containers)
dbEndpoint := db.GetEndpoint("tcp")
app.WithEnvironment("DB_HOST", dbEndpoint)
app.WithEnvironment("DB_USER", "...")
app.WithEnvironment("DB_PASSWORD", "...")
app.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{...})
app.WithExternalHttpEndpoints()
app.WaitFor(db)  // ✅ Waits for datastore to be Healthy
```

**Chain order:**
1. Wire env vars (`PG_HOST`, etc.)
2. Configure HTTP endpoint
3. Enable external endpoints
4. **`WaitFor(db)`** — Establishes startup ordering
5. OTLP exporter (if needed)

## Related

- `.squad/decisions/inbox/romanoff-go-waitfor-coldstart.md` — cold-start fix decision
- `.squad/agents/romanoff/history.md` — historical context (13.4.6 migration, WaitFor fix)
- Aspire 13.4.6 Go SDK bindings: `.aspire/modules/aspire.go` (generated at runtime)
- `samples/svelte-go-bookmarks/apphost.go` — reference implementation
