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
