# Bookmark Manager - Go AppHost + Go API + Svelte

A preview **Go-authored Aspire AppHost** orchestrating a Go HTTP API, a Svelte 5
frontend, and PostgreSQL. It demonstrates `AddContainer`, `AddDockerfile`, and
`AddExecutable` without a C# AppHost.

## Prerequisites

- Aspire CLI 13.5.3 and the .NET 10 SDK.
- Go 1.26.1 or later for `apphost.go`.
- Node.js 22.12+ and npm for the Vite 8/Svelte 5 frontend.
- Docker with Linux containers.

The Go AppHost is experimental. Its SDK and feature flags are configured in
`aspire.config.json`; use the CLI to regenerate `.aspire/modules/`, not manual edits.

## Run

From this sample directory:

```bash
aspire run --apphost apphost.go
```

Aspire starts PostgreSQL, builds the API image, runs `npm ci` as
`frontend-install`, then starts Vite after installation and API startup.
Open **frontend** in the Aspire dashboard. The frontend port and API proxy target
are assigned by Aspire, rather than reserving port 5173.

For a worktree background run:

```bash
aspire start --apphost apphost.go --isolated
aspire wait frontend --apphost apphost.go --status up
aspire describe --apphost apphost.go
```

Stop only this sample with `aspire stop --apphost apphost.go`.

## Demo

- View the three seeded bookmarks.
- Create a bookmark with a URL, title, and comma-separated tags.
- Search by title, tag, or URL.
- Reload to confirm database storage, then delete the test bookmark.
- Inspect correlated browser, Go HTTP, and PostgreSQL spans in Aspire.

## Wiring

`pg` is a raw `postgres:16` container, not the `AddPostgres` integration. Its
endpoint is TCP. The AppHost supplies `PG_HOST`, `PG_USER`, `PG_PASSWORD`, and
`PG_DB` to the API explicitly; `buildPgConnString` converts the endpoint into a
PostgreSQL DSN. `WaitFor(pg)` orders container startup.

The API's standalone mode can use in-memory storage; a failed PostgreSQL
connection is logged before falling back. For the database demo, confirm
**Connected to PostgreSQL** in the API's logs. This sample has no named database
volume: recreating PostgreSQL resets its data.

The frontend references the API endpoint through `services__api__http__0`.
Its browser tracing uses the dashboard's HTTP collector; the Go API uses gRPC.
Application telemetry labels inherit the SDK's schema instead of pinning a
conflicting schema version.

## API and checks

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/bookmarks` | List bookmarks |
| POST | `/api/bookmarks` | Create a bookmark |
| DELETE | `/api/bookmarks/{id}` | Delete a bookmark |
| GET | `/api/bookmarks/search?q=go` | Search titles, tags, and URLs |
| GET | `/health` | Process health |

```bash
go build .
cd go-api
go test ./...
go vet ./...
cd ../frontend
npm ci
npm run build
```

`go-api/Dockerfile` builds the API on Go 1.25 and runs it in Alpine.
`frontend/` contains the Svelte application and Vite configuration.
