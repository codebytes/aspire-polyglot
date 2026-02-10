# Bookmark Manager - Go + Svelte + .NET Aspire

A full-stack bookmark manager demonstrating how **.NET Aspire can orchestrate ANY executable** - not just .NET apps!

## Architecture

- **Backend**: Go HTTP server (stdlib `net/http`, no external dependencies)
- **Frontend**: Svelte 4 SPA with Vite
- **Orchestration**: .NET Aspire using standalone `apphost.cs` with `AddDockerfile` and `AddNpmApp`

This sample proves that Aspire is a **polyglot orchestration platform** - it can manage Go, Node.js, Python, Ruby, or any containerized application.

## Features

- ✅ Add bookmarks with URL, title, and tags
- 🔍 Real-time search by title, tags, or URL
- 🏷️ Tag-based organization with colored badges
- 🗑️ Delete bookmarks
- 💾 In-memory storage with seeded data
- 🎨 Modern, responsive card-based UI

## Project Structure

```
svelte-go-bookmarks/
├── apphost.cs            # Standalone .NET Aspire orchestration
├── go-api/               # Go backend
│   ├── main.go           # HTTP server with CRUD endpoints
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

## Key Aspire Pattern: AddDockerfile

Since Aspire doesn't have `AddGoApp`, we use **`AddDockerfile`** to orchestrate the Go API in our standalone `apphost.cs`:

```csharp
var api = builder.AddDockerfile("api", "./go-api")
    .WithHttpEndpoint(targetPort: 8080)
    .WithExternalHttpEndpoints();
```

This pattern works for **any language** that can be containerized!

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
- 🔗 **Service references** - Frontend gets `services__api__http__0` automatically
- 📊 **Unified observability** - Logs, metrics, traces in Aspire dashboard
- 🚀 **Local-to-cloud** - Same orchestration in dev and production

**The lesson**: If it runs in Docker, Aspire can orchestrate it. This makes Aspire a compelling choice for **heterogeneous microservices** where teams use different tech stacks.

## Technologies

- [.NET Aspire 9.2.1](https://learn.microsoft.com/dotnet/aspire/)
- [Go 1.22](https://go.dev/)
- [Svelte 4](https://svelte.dev/)
- [Vite 5](https://vitejs.dev/)
- Docker

---

**Part of the aspire-polyglot sample collection** - showcasing Aspire's ability to orchestrate diverse tech stacks.
