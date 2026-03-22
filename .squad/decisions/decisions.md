# Decision: Java Connection String Format & Properties

**Author:** Thor (Java Dev)  
**Date:** 2026-03-22  
**Status:** RESOLVED  

## Summary

Aspire does NOT need a connection string parser for Java. When `.withReference(db)` is used, Aspire automatically injects individual connection properties including a ready-to-use JDBC connection string as a separate environment variable.

## Problem Statement

The Aspire-provided `ConnectionStrings__notesdb` is in .NET semicolon-delimited format (`Host=...;Port=...;Username=...;Password=...;Database=notesdb`), not JDBC format. Spring Boot cannot use this directly.

## Solution

**No code changes required.** Use the Aspire-injected properties:

```properties
server.port=${PORT:8080}
spring.datasource.url=${NOTESDB_JDBCCONNECTIONSTRING:jdbc:postgresql://localhost:5432/notesdb}
spring.datasource.username=${NOTESDB_USERNAME:postgres}
spring.datasource.password=${NOTESDB_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Key Findings

| Environment Variable | Value | Format |
|---|---|---|
| `NOTESDB_JDBCCONNECTIONSTRING` | `jdbc:postgresql://<host>:<port>/notesdb` | JDBC format |
| `NOTESDB_USERNAME` | `<username>` | From `GetConnectionProperties()` |
| `NOTESDB_PASSWORD` | `<password>` | From `GetConnectionProperties()` |
| `NOTESDB_HOST` | `<hostname>` | Individual property |
| `NOTESDB_PORT` | `<port>` | Individual property |

The JDBC connection string does NOT include credentials; username and password must be provided separately.

## Verification

- All 5 research questions answered with source evidence
- Test file: `tests/Aspire.Hosting.PostgreSQL.Tests/ConnectionPropertiesTests.cs` verifies these properties
- Applied to: spring-boot-postgres sample (application.properties, demo slide, README)

---

# Decision: .NET 10 + Aspire 13.1.3 Upgrade

**Author:** Rogers (C#/.NET Dev)  
**Date:** 2026-03-22  
**Status:** Implemented  

## Summary

Upgraded both C# samples (dotnet-angular-cosmos, polyglot-event-stream) from .NET 9 / Aspire 9.5.2 to .NET 10 / Aspire 13.1.3. This is a major framework upgrade spanning two .NET major versions and four Aspire major versions.

## Changes Made

### All 6 .csproj files
- `<TargetFramework>` net9.0 → net10.0
- Aspire.AppHost.Sdk 9.5.2 → 13.1.3
- All `Aspire.*` packages → 13.1.3
- `Microsoft.Extensions.Http.Resilience` 9.10.0 → 10.4.0
- `Microsoft.Extensions.ServiceDiscovery` 9.5.2 → 10.4.0

### Breaking Changes Encountered and Fixed
1. **Package rename**: `Aspire.Hosting.NodeJs` → `Aspire.Hosting.JavaScript` — the old package stops at 9.5.2
2. **API rename**: `AddNpmApp()` → `AddJavaScriptApp()` — same signature, new method name
3. **NoWarn cleanup**: Removed `ASPIREHOSTINGNODEJS001` suppression (obsolete warning code)

### No Changes Required
- OpenTelemetry packages (already at 1.15.x — latest)
- Microsoft.Azure.Cosmos (already at 3.58.0 — latest)
- No other deprecated APIs found (WithSecretBuildArg, AIFoundry not used)

### README Updates
- Fixed code snippets: `AddNpmApp` → `AddJavaScriptApp` in both sample READMEs
- Updated version reference: "Aspire 9.5.2" → "Aspire 13.1.3" in dotnet-angular-cosmos README

## Verification
- `dotnet restore` ✅ both samples
- `dotnet build` ✅ both samples — 0 warnings, 0 errors

## Impact on Team
- **Slide deck**: References to Aspire versions and API names (AddNpmApp) may need updating (Strange/Scribe)
- **Polyglot samples**: Other AppHosts (Python, Go, Java, JS) are unaffected — they don't use .NET
- **NuGet.config decision (#7)**: Still pending — not affected by this upgrade
