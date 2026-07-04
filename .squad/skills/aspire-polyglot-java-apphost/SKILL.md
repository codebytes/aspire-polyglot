---
name: "Aspire 13.4.6 Java AppHost Patterns"
description: "How to structure Java apphosts for Aspire 13.4.6 polyglot: default-package convention, binding imports, launcher requirements, and startup ordering"
domain: "aspire-polyglot, java, orchestration"
confidence: "high"
source: "earned (migrated spring-boot-postgres from 13.2.4 → 13.4.6, fixed ClassNotFoundException, verified with launcher's exact compile command)"
tools:
  - name: "bash"
    description: "Run javac/java commands with exact launcher flags"
    when: "Verifying apphost compilation or diagnosing ClassNotFoundException"
---

## Context

Aspire 13.4.6 polyglot changed the Java launcher convention for user apphosts. The launcher now expects:
- **Main class in the DEFAULT (unnamed) package**
- **Filename = class name** (e.g., `AppHost.java` → class `AppHost`)
- **Generated bindings imported** (not shared package with user code)

This aligns Java with Go/Python polyglot samples where the user apphost lives at the root level and imports the generated `aspire` module.

### When This Applies

- Writing a new Java apphost for Aspire 13.4.6+
- Migrating an existing Java apphost from 13.2.4 or earlier (which used `package aspire;`)
- Diagnosing `ClassNotFoundException: AppHost` at startup (indicates wrong package)
- Understanding why the launcher can't find your main class

### Launcher Mechanics

The Aspire 13.4.6 Java launcher runs this exact command from the sample directory:

```bash
sh -c 'mkdir -p .java-build && javac --enable-preview --source 25 -d .java-build @.aspire/modules/sources.txt AppHost.java && java --enable-preview -cp .java-build AppHost'
```

**Key observations:**
1. `javac` compiles `.aspire/modules/*.java` (from `sources.txt`) + `AppHost.java` into `.java-build/`
2. `java ... AppHost` (bare, no package prefix) invokes the main class
3. The class name is derived from the **filename** (`AppHost.java` → `AppHost`)
4. If `AppHost.java` declares a package, the class file goes into a subdirectory and `java ... AppHost` can't find it

## Patterns

### 1. Default-Package AppHost Structure

**Required structure for Aspire 13.4.6:**

```java
// AppHost.java (at sample root, e.g., samples/spring-boot-postgres/AppHost.java)

import aspire.*;  // Import generated bindings (IDistributedApplicationBuilder, Aspire, etc.)

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            
            // Add resources...
            var pg = builder.addContainer("pg", "postgres:16");
            // Configure resources...
            
            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

**Critical rules:**
- ❌ **NO `package aspire;`** declaration (or any package)
- ✅ **YES `import aspire.*;`** to access generated bindings
- ✅ Filename = class name (`AppHost.java` → `public class AppHost`)
- ✅ Main method signature: `public static void main(String[] args)`

### 2. Generated Bindings Location

**Aspire 13.4.6 binding layout:**

```
samples/spring-boot-postgres/
├── .aspire/
│   └── modules/
│       ├── sources.txt           ← Lists all .java files to compile
│       ├── Aspire.java            ← Main entry point (Aspire.createBuilder)
│       ├── IDistributedApplicationBuilder.java
│       ├── ContainerResource.java
│       ├── ... (182 files total in 13.4.6)
│       └── aspire/                ← (NOT used in 13.4.6 — old layout was flat)
└── AppHost.java                   ← USER APPHOST (default package)
```

**Package structure:**
- Generated bindings: `package aspire;` (in `.aspire/modules/*.java`)
- User apphost: **DEFAULT PACKAGE** (no `package` declaration)
- User apphost imports bindings: `import aspire.*;`

### 3. Startup Ordering with `waitFor`

**Problem:** In Aspire 13.4.6 polyglot, environment variable wiring (`withEnvironment`, `withReference`) is **config-only** — it does NOT order startup.

**Solution:** Use explicit `waitFor` to enforce dependencies.

**Example (Spring Boot app that connects to Postgres at startup):**

```java
var pg = builder.addContainer("pg", "postgres:16");
pg.withEnvironment("POSTGRES_USER", "postgres");
pg.withEnvironment("POSTGRES_PASSWORD", "postgres");
pg.withEnvironment("POSTGRES_DB", "notesdb");

var api = builder.addDockerfile("api", "./src");
api.withOtlpExporter();
api.withEnvironment("NOTESDB_JDBCCONNECTIONSTRING", "jdbc:postgresql://pg:5432/notesdb");
api.withEnvironment("NOTESDB_USERNAME", "postgres");
api.withEnvironment("NOTESDB_PASSWORD", "postgres");
api.withExternalHttpEndpoints();

// CRITICAL: Enforce startup ordering so Spring Boot never starts before Postgres is healthy.
// Without this, cold starts race: HikariCP fails fast if pg isn't ready → app crashes.
api.waitFor(pg);

DistributedApplication app = builder.build();
```

**Method signature:**
- `ContainerResource.waitFor(IResource dependency)`
- `ContainerResource.waitFor(ResourceBuilderBase dependency)`
- Both `addContainer` and `addDockerfile` return `ContainerResource`
- Pass the dependency resource directly: `api.waitFor(pg)`

**When to use `waitFor`:**
- App connects to a datastore at startup (e.g., Hibernate `ddl-auto=update`, schema migrations)
- App depends on another service's HTTP endpoint being available
- App reads config from a service at startup (e.g., feature flags, secrets)
- Basically: whenever the app **fails fast** if the dependency isn't ready

### 4. Compilation Verification

**To test if your apphost will work with the launcher:**

```bash
cd samples/spring-boot-postgres  # or your sample directory
rm -rf .java-build && mkdir -p .java-build
javac --enable-preview --source 25 -d .java-build @.aspire/modules/sources.txt AppHost.java
```

**Success indicators:**
- ✅ Compilation succeeds (warnings about `unchecked` or `unsafe operations` are normal)
- ✅ Class file exists at `.java-build/AppHost.class` (default package, NOT under `aspire/`)
- ✅ No class file at `.java-build/aspire/AppHost.class` (would indicate wrong package)

**Verification command:**

```bash
ls .java-build/AppHost.class                # MUST exist
ls .java-build/aspire/AppHost.class 2>/dev/null && echo "❌ WRONG PACKAGE" || echo "✅ CORRECT"
```

**Cleanup:**

```bash
rm -rf .java-build  # Transient build directory; not committed
```

## Examples

### Example 1: Simple Apphost with Container

```java
import aspire.*;

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            
            var redis = builder.addContainer("redis", "redis:7-alpine");
            
            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

### Example 2: App + Database with Ordering

```java
import aspire.*;

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            
            // Add PostgreSQL container
            var pg = builder.addContainer("pg", "postgres:16");
            pg.withEnvironment("POSTGRES_USER", "postgres");
            pg.withEnvironment("POSTGRES_PASSWORD", "postgres");
            pg.withEnvironment("POSTGRES_DB", "mydb");
            
            // Add Spring Boot app
            var api = builder.addDockerfile("api", "./src");
            api.withOtlpExporter();
            api.withEnvironment("SPRING_DATASOURCE_URL", "jdbc:postgresql://pg:5432/mydb");
            api.withEnvironment("SPRING_DATASOURCE_USERNAME", "postgres");
            api.withEnvironment("SPRING_DATASOURCE_PASSWORD", "postgres");
            api.withExternalHttpEndpoints();
            
            // Enforce startup ordering: app waits for database
            api.waitFor(pg);
            
            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

### Example 3: Migration from 13.2.4 → 13.4.6

**Before (13.2.4):**

```java
package aspire;  // ❌ User and generated code shared package

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            // ... resources ...
            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

**After (13.4.6):**

```java
import aspire.*;  // ✅ Import generated bindings

public class AppHost {  // ✅ Default package
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            // ... resources ...
            DistributedApplication app = builder.build();
            app.run(null);
        } catch (Exception e) {
            System.err.println("Failed to run: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
```

**Changes:**
- Removed `package aspire;` (line 1)
- Added `import aspire.*;` (line 1, new)
- No logic changes

## Anti-Patterns

### ❌ Anti-Pattern 1: Declaring a Package

```java
package aspire;  // ❌ WRONG — breaks launcher

public class AppHost {
    public static void main(String[] args) {
        // ...
    }
}
```

**Why it fails:**
- `javac` compiles the class to `.java-build/aspire/AppHost.class`
- `java ... AppHost` (bare) looks for `.java-build/AppHost.class`
- Result: `ClassNotFoundException: AppHost`

### ❌ Anti-Pattern 2: Forgetting to Import Bindings

```java
// ❌ NO import statement

public class AppHost {
    public static void main(String[] args) {
        IDistributedApplicationBuilder builder = Aspire.createBuilder(null);  // ❌ undefined
        // ...
    }
}
```

**Why it fails:**
- `IDistributedApplicationBuilder`, `Aspire`, etc. are in `package aspire;`
- Without `import aspire.*;`, the compiler can't find them
- Result: compilation errors for all binding classes

### ❌ Anti-Pattern 3: Missing `waitFor` for Startup Dependencies

```java
var pg = builder.addContainer("pg", "postgres:16");
// ... configure pg ...

var api = builder.addDockerfile("api", "./src");
api.withEnvironment("SPRING_DATASOURCE_URL", "jdbc:postgresql://pg:5432/mydb");
// ❌ NO api.waitFor(pg)

DistributedApplication app = builder.build();
```

**Why it fails:**
- On cold starts, `pg` may not be Healthy when `api` starts
- Spring Boot / Hibernate tries to connect → HikariCP fails fast → app crashes
- Aspire shows `api` in CrashLoopBackOff until `pg` is ready (wastes time, confusing for demos)

**Fix:**

```java
api.withEnvironment("SPRING_DATASOURCE_URL", "jdbc:postgresql://pg:5432/mydb");
api.waitFor(pg);  // ✅ Enforce ordering
```

### ❌ Anti-Pattern 4: Wrong Filename / Class Name Mismatch

```java
// File: MyApp.java
public class AppHost {  // ❌ Class name doesn't match filename
    public static void main(String[] args) {
        // ...
    }
}
```

**Why it fails:**
- Launcher runs `java ... AppHost` (derived from filename `AppHost.java`)
- But the class is named `MyApp`
- Result: `ClassNotFoundException: AppHost`

**Fix:** Filename must match class name: `AppHost.java` → `public class AppHost`

### ❌ Anti-Pattern 5: Using Old 13.2.4 Binding Layout

```java
import aspire.*;  // ✅ Correct import

public class AppHost {
    public static void main(String[] args) {
        try {
            IDistributedApplicationBuilder builder = Aspire.createBuilder(null);
            // ...
        } catch (Exception e) { /* ... */ }
    }
}
```

But the sample still has old `.modules/` directory instead of `.aspire/modules/`.

**Why it fails:**
- 13.4.6 launcher expects `.aspire/modules/sources.txt`
- Without running `aspire restore` to regenerate bindings, you get 13.2.4 bindings
- May compile but can have runtime API mismatches (e.g., old `addDockerfile` signature)

**Fix:** Run `aspire restore` to regenerate bindings in `.aspire/modules/` with 13.4.6 layout.

## Related Skills

- **aspire-polyglot-python-apphost:** Python equivalent (default-level `apphost.py` + `from aspire_app import create_builder`)
- **aspire-polyglot-go-apphost:** Go equivalent (`package main` + `import "apphost/modules/aspire"`)
- **project-conventions:** Cross-language Aspire conventions (binding layouts, gitignore rules)

## Troubleshooting

### Symptom: `ClassNotFoundException: AppHost`

**Root cause:** Apphost class not in the default package.

**Diagnosis:**

```bash
javac --enable-preview --source 25 -d .java-build @.aspire/modules/sources.txt AppHost.java
ls .java-build/AppHost.class               # Should exist
ls .java-build/aspire/AppHost.class        # Should NOT exist
```

**Fix:** Remove `package aspire;` from `AppHost.java`, add `import aspire.*;`.

### Symptom: `error: package aspire does not exist`

**Root cause:** Missing `import aspire.*;` or bindings not generated.

**Diagnosis:**

```bash
ls .aspire/modules/sources.txt    # Should exist
ls .aspire/modules/Aspire.java    # Should exist
```

**Fix:**
1. Add `import aspire.*;` to `AppHost.java`
2. If bindings missing, run `aspire restore` to regenerate them

### Symptom: App crashes on startup with `Connection refused` or `HikariCP connection error`

**Root cause:** App starts before database is healthy (no `waitFor`).

**Fix:** Add `api.waitFor(pg);` after env-wiring, before `builder.build()`.

## References

- **Sample:** `samples/spring-boot-postgres/AppHost.java` (reference implementation)
- **Decision:** `.squad/decisions/inbox/thor-java-apphost-13.4.6.md` (migration rationale)
- **History:** `.squad/agents/thor/history.md` (2026-07-02 entry)
- **Aspire Docs:** [Polyglot support](https://learn.microsoft.com/en-us/dotnet/aspire/polyglot/) (13.4.6 conventions)
