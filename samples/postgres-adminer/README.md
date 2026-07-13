# PostgreSQL + Adminer (add public Docker images directly)

The whole AppHost is a single-file `apphost.cs` that adds **two public Docker
images directly** — PostgreSQL and [Adminer](https://www.adminer.org/) — with
`AddContainer`. No hosting-integration packages, no Dockerfiles, and no
application projects. It's the smallest possible "orchestrate some containers"
demo.

## Tech Stack
- **Database**: `postgres:17-alpine` (public image) with a persistent named volume
- **Admin UI**: `adminer:5` (public image) — a web UI for the database
- **AppHost**: single-file `apphost.cs`, base Aspire SDK only

## Running

```bash
cd samples/postgres-adminer
aspire run
```

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/).

Aspire pulls both images, starts them on a shared network, and opens the
dashboard. Open the **adminer** endpoint and log in:

| Field | Value |
|-------|-------|
| System | PostgreSQL |
| Server | `db` (pre-filled) |
| Username | `postgres` |
| Password | `postgres` |
| Database | `appdb` |

## What It Demonstrates
- `AddContainer("db", "postgres", "17-alpine")` — pulling a public image directly,
  configuring it with `WithEnvironment`, and persisting data with `WithVolume`.
- `WithEndpoint` / `WithHttpEndpoint` + `WithExternalHttpEndpoints()` to expose ports.
- **Container-to-container networking by resource name**: Adminer reaches Postgres
  at the hostname `db` (its resource name) — `ADMINER_DEFAULT_SERVER=db` — with no
  service-discovery code.
- `WaitFor` to order startup so Adminer starts after the database.
- Logs and container metrics for both images surfaced in the Aspire dashboard.

## Project Layout

```
apphost.cs            single-file C# AppHost (two AddContainer calls)
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
```
