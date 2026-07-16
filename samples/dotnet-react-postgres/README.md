# .NET + React + PostgreSQL (single-file C# AppHost)

A full-stack quotes board orchestrated by a **single-file C# AppHost** — the entire
dev-time orchestrator is one `apphost.cs`, with no `.csproj` and no `Program.cs`.

## Tech Stack
- **Backend**: ASP.NET Core Minimal API (`Api/`) using the `Aspire.Npgsql` client integration
- **Database**: PostgreSQL (container, with a persistent data volume)
- **Frontend**: Vite + React (TypeScript), run via `AddViteApp`
- **AppHost**: single-file `apphost.cs` (`#:sdk` / `#:package` / `#:project` directives)

## Running

```bash
cd samples/dotnet-react-postgres
aspire run
```

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/), the .NET 10 SDK, and Node 20+.

Aspire starts PostgreSQL, the API, and the Vite dev server, and opens the dashboard.
The React app is available from the `web` resource; it proxies `/api` to the API using
the endpoint Aspire injects via service discovery.

## What It Demonstrates
- A **single-file `apphost.cs`** — `#:sdk Aspire.AppHost.Sdk`, `#:package` for the
  PostgreSQL and JavaScript hosting integrations, and `#:project Api/Api.csproj` to
  reference a real .NET project (surfaced as the generated `Projects.Api` type).
- `AddPostgres(...).WithDataVolume().AddDatabase(...)` for a persisted database.
- `WithReference` + `WaitFor` to inject the connection string and order startup.
- `AddViteApp` for a host-run Vite dev server, plus `PublishWithContainerFiles`
  to bake the built SPA into the API container for `aspire publish`.
- The `Aspire.Npgsql` client integration wiring up `NpgsqlDataSource`, OpenTelemetry,
  and health checks from the injected `ConnectionStrings__quotesdb`.

## Project Layout

```
apphost.cs            single-file C# AppHost (the orchestrator)
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
Api/                  ASP.NET Core Minimal API (quotes CRUD over Postgres)
ServiceDefaults/      shared OpenTelemetry / health / service-discovery wiring
frontend/             Vite + React SPA
```
