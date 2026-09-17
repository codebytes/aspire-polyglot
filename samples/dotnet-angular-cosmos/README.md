# Recipe Manager - ASP.NET Core + Angular + Cosmos DB

A .NET 10 Minimal API and Angular 22 recipe manager backed by the **local Cosmos DB
preview emulator**. No Azure subscription or separately installed emulator is needed.

## Prerequisites

- [Aspire CLI](https://aspire.dev/get-started/install-cli/) 13.5.3 and the .NET 10 SDK.
- [Docker](https://docs.docker.com/get-docker/) running Linux containers.
- Node.js supported by Angular 22: `^22.22.3`, `^24.15.0`, or `>=26.0.0`.

## Run

From this sample directory:

```bash
aspire run --apphost AppHost/AppHost.csproj
```

Aspire starts the preview emulator, creates the database, seeds three recipes, and
starts the API and Angular dev server. The first emulator startup can take a few
minutes. Do not start a separate emulator on port 8081.

Open **frontend** in the Aspire dashboard. API and frontend ports are assigned by
Aspire; the Angular `/api` proxy reads the API's service-discovery endpoint.
The **Data Explorer** link on `cosmos` opens the emulator's database UI.

For an isolated background run in a worktree:

```bash
aspire start --apphost AppHost/AppHost.csproj --isolated
aspire wait frontend --apphost AppHost/AppHost.csproj --status up
aspire describe --apphost AppHost/AppHost.csproj
```

Stop only this sample with:

```bash
aspire stop --apphost AppHost/AppHost.csproj
```

## Demo

- View the seeded Carbonara, cookies, and green curry recipes.
- Search titles or ingredient substrings, case-insensitively; try `milk`.
- Filter by category, expand ingredients and instructions, or create a recipe.
- Edit the new recipe, reload to confirm storage in Cosmos DB, then delete it.

The Angular UI explicitly uses zone-based change detection for its observable
subscriptions. The browser's OpenTelemetry exporters use Aspire's HTTP collector
and authentication headers, not the gRPC endpoint. Runtime configuration is
generated into the ignored `frontend/.generated/` directory; do not publish this
local-demo configuration or its authentication token. Standalone builds without
an HTTP collector do not attempt telemetry exports.

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/recipes` | List recipes |
| GET | `/api/recipes/{id}` | Read a recipe |
| POST | `/api/recipes` | Create a recipe |
| PUT | `/api/recipes/{id}` | Replace a recipe |
| DELETE | `/api/recipes/{id}` | Delete a recipe |
| GET | `/api/recipes/search?q=milk` | Search titles and ingredients |
| GET | `/health` | Service-default health checks, including Cosmos connectivity |
| GET | `/alive` | Process liveness |

## Build

```bash
dotnet build dotnet-angular-cosmos.sln
cd frontend
npm ci
npm run build
npm run test:config
```

`AppHost/` owns orchestration, `Api/` owns the Cosmos CRUD endpoints and seed data,
`ServiceDefaults/` supplies health checks and OpenTelemetry, and `frontend/`
contains the Angular application.

The API creates `recipesdb` and its `recipes` container with `/id` as the
partition key. This is a local development demo; stopping and recreating the
emulator can reset its data.
