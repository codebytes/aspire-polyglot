# Bookmark Manager - Go + Svelte + Aspire

A full-stack bookmark manager demonstrating how **Aspire can orchestrate ANY executable** - not just .NET apps!

## Architecture

- **Backend**: Go HTTP server with PostgreSQL persistence (falls back to in-memory without Aspire)
- **Frontend**: Svelte 4 SPA with Vite
- **Database**: PostgreSQL managed by Aspire via `AddPostgres`
- **Orchestration**: Aspire using standalone `apphost.go` with `AddDockerfile`, `AddNpmApp`, and `AddPostgres`

This sample proves that Aspire is a **polyglot orchestration platform** - it can manage Go, Node.js, Python, Ruby, or any containerized application, **including real infrastructure like PostgreSQL**.

## Features

- ✅ Add bookmarks with URL, title, and tags
- 🔍 Real-time search by title, tags, or URL
- 🏷️ Tag-based organization with colored badges
- 🗑️ Delete bookmarks
- 🐘 PostgreSQL persistence via Aspire-managed infrastructure
- 💾 Graceful in-memory fallback when running without Aspire
- 🎨 Modern, responsive card-based UI

## Project Structure

```
svelte-go-bookmarks/
├── apphost.go            # Standalone Aspire orchestration (Go)
├── go-api/               # Go backend
│   ├── main.go           # HTTP server with CRUD endpoints + PostgreSQL
│   ├── go.mod
│   └── Dockerfile        # Multi-stage build
└── frontend/             # Svelte SPA
    ├── src/
    │   ├── App.svelte    # Main bookmark UI
    │   ├── main.js
    │   └── app.css
    ├── index.html
    ├── vite.config.js    # Proxies /api to backend
    └── package.json
```

## Key Aspire Patterns

### AddPostgres + WithReference

Aspire manages a PostgreSQL container and injects the connection string into the Go API automatically:

```go
pg, _ := builder.AddPostgres("pg")
db, _ := pg.AddDatabase("bookmarksdb")

api, _ := builder.AddDockerfile("api", "./go-api")
api.WithReference(db)
api.WithHttpEndpoint(8080, "http")
api.WithExternalHttpEndpoints()
```

The Go API reads `CONNECTIONSTRINGS__bookmarksdb` from the environment — no hardcoded connection strings needed.

### AddDockerfile

Since Aspire doesn't have `AddGoApp`, we use **`AddDockerfile`** to orchestrate the Go API. This pattern works for **any language** that can be containerized!

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bookmarks` | Get all bookmarks |
| POST | `/api/bookmarks` | Create bookmark |
| DELETE | `/api/bookmarks/{id}` | Delete bookmark |
| GET | `/api/bookmarks/search?q=` | Search bookmarks |
| GET | `/health` | Health check |

## Running

Prerequisites:
- .NET 9 SDK with Aspire workload
- Docker Desktop
- Node.js 18+

```bash
aspire run
```

Aspire will:
1. Build the Go API Docker image
2. Run the container on port 8080
3. Install npm dependencies for frontend
4. Start Vite dev server with proxy to Go API
5. Open the Aspire dashboard

## Why This Matters

This sample demonstrates:

- 🌍 **Polyglot orchestration** - Aspire isn't .NET-only!
- 🐳 **AddDockerfile pattern** - Universal integration for any language
- 🐘 **AddPostgres pattern** - Aspire manages real infrastructure for non-.NET apps
- 🔗 **Service references** - Frontend gets `services__api__http__0`, API gets `CONNECTIONSTRINGS__bookmarksdb` automatically
- 📊 **Unified observability** - Logs, metrics, traces in Aspire dashboard
- 🚀 **Local-to-cloud** - Same orchestration in dev and production

**The lesson**: Aspire isn't just for .NET — it orchestrates containers, databases, and frontends across any tech stack. If it runs in Docker, Aspire can orchestrate it and wire up infrastructure automatically.

## Technologies

- [Aspire](https://learn.microsoft.com/dotnet/aspire/)
- [Go 1.22](https://go.dev/)
- [PostgreSQL](https://www.postgresql.org/)
- [Svelte 4](https://svelte.dev/)
- [Vite 5](https://vitejs.dev/)
- Docker

---

**Part of the aspire-polyglot sample collection** - showcasing Aspire's ability to orchestrate diverse tech stacks.
