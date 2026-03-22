# Rogers — History

## Project Context
- **Project:** aspire-polyglot — Polyglot Aspire samples + Marp slide deck
- **User:** Chris Ayers
- **Stack:** C#, .NET, ASP.NET Core, Angular 19, CosmosDB, Aspire
- **Primary samples:** dotnet-angular-cosmos, C# parts of polyglot-event-stream, C# AppHosts

## Learnings

### 2025-07-18 — Full C#/.NET Code Review
- Both samples (dotnet-angular-cosmos, polyglot-event-stream) are on Aspire 9.2.1; latest 9.x is 9.5.2, latest overall is 13.1.3
- No deprecated APIs found (WithSecretBuildArg, AIFoundry not used) — clean upgrade path
- **Key rename in Aspire 13.x**: `Aspire.Hosting.NodeJs` → `Aspire.Hosting.JavaScript`
- ServiceDefaults are identical across both samples — keep in sync during upgrades
- OpenTelemetry packages behind (1.10.x → 1.15.0 available)
- Microsoft.Azure.Cosmos behind (3.47.2 → 3.58.0)
- dotnet-angular-cosmos README has a code snippet bug: shows `.AddDatabase()` but code uses `.AddCosmosDatabase()`
- polyglot-event-stream uses modern .slnx format, dotnet-angular-cosmos uses legacy .sln
- Both AppHosts could benefit from `WaitFor()` on infrastructure resources (CosmosDB emulator, Kafka)
- No C# AppHost files exist in the other 5 samples (django, flask, spring-boot, svelte-go, vite-react)

### 2026-03-22 — Team Review Session Complete
- Team confirmed Aspire version lag as high-priority fix (packages 3+ versions behind).
- **Action items routed to Rogers:**
  - Upgrade all Aspire packages: 9.2.1 → 9.5.2 (both samples, all 6+ packages per sample)
  - Upgrade OpenTelemetry: 1.10.x → 1.15.0
  - Upgrade Microsoft.Azure.Cosmos: 3.47.2 → 3.58.0
  - Fix dotnet-angular-cosmos README code snippet (`.AddDatabase()` → `.AddCosmosDatabase()`)
  - Update README version references from "9.2.1" to current
  - Test both samples after version bump
- **Deliverable:** All .NET packages on latest 9.x before talk
