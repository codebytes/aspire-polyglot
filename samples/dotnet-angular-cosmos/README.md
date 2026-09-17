# Recipe Manager - ASP.NET Core + Angular + Cosmos DB

A .NET 10 Minimal API and Angular 22 recipe manager backed by the **local Cosmos DB
preview emulator**. No Azure subscription or separately installed emulator is needed.
The same AppHost can also **publish Azure Container Apps and Cosmos DB Bicep
templates without deploying anything**.

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
an HTTP collector do not attempt telemetry exports. Production builds deliberately
omit browser collector URLs and authentication headers, even if the build shell
has local Aspire telemetry variables.

## Publish Bicep — artifact-only live demo

**Boundary: these commands write and compile local files only. They do not log in,
build or push container images, deploy, or create/update/delete Azure resources.**
You do not need a running AppHost, Docker, a Cosmos emulator, an Azure subscription,
or Azure credentials to publish the templates. Leave other demos running.

Publishing needs Aspire CLI **13.5.3** and the .NET 10 SDK; the matching
`Aspire.Hosting.Azure.AppContainers` package is already included. ARM compilation
also needs Azure CLI with Bicep installed (`az bicep version`; verified with Bicep
0.42.1). The optional assertions use Node.js, with no additional npm dependencies.
Use your normal system-configured package feeds.

From this sample directory, run:

```bash
# Override OUTPUT to put the artifacts elsewhere; this default is gitignored.
OUTPUT="${OUTPUT:-$PWD/aspire-output/bicep}"

# Inspect before executing: publish-azure-environment writes Bicep, not Azure resources.
aspire publish --apphost AppHost/AppHost.csproj --list-steps \
  --output-path "$OUTPUT" --non-interactive --nologo

aspire publish --apphost AppHost/AppHost.csproj \
  --output-path "$OUTPUT" --non-interactive --nologo

# Compile locally to ARM JSON; this is not an Azure deployment validation.
az bicep build --file "$OUTPUT/main.bicep"
for module in "$OUTPUT"/*/*.bicep; do
  az bicep build --file "$module" || exit 1
done

ASPIRE_PUBLISH_OUTPUT="$OUTPUT" node --test scripts/verify-publish.test.cjs
```

The inspected publish pipeline contains parameter processing, build-only resource
validation, JavaScript publish validation, and `publish-azure-environment`.
It contains no Azure provisioning, image push, or deployment step. No parameter
values or fake credentials are required for generation or compilation.

### What to show

Open `AppHost/Program.cs`, then the generated files:

| File | Generated Azure model |
|------|----------------------|
| `main.bicep` | Subscription-scoped entry point: resource group and five infrastructure modules |
| `aca-acr/aca-acr.bicep` | Basic Azure Container Registry |
| `aca/aca.bicep` | Consumption Container Apps environment, Log Analytics workspace, registry-pull managed identity and `AcrPull` role, Aspire Dashboard component |
| `cosmos/cosmos.bicep` | Serverless Cosmos account, `recipesdb`, and `recipes` container partitioned by `/id`; key authentication disabled |
| `api-identity/api-identity.bicep` | API user-assigned managed identity |
| `api-roles-cosmos/api-roles-cosmos.bicep` | Cosmos DB Built-in Data Contributor assignment for that API identity |
| `api/api.bicep` | Internal API Container App, Cosmos endpoint/database settings and managed identity |
| `frontend/frontend.bicep` | Public frontend Container App, static serving on port 5000 and `/api` reverse proxy |
| `frontend.Dockerfile` | Angular production build and YARP static-site runtime |

Compilation creates `main.json` and a matching `.json` beside each of the seven
module files. Resource names with uniqueness suffixes are generated from the
resource-group ID; `api`, `frontend`, `recipesdb`, and `recipes` retain their names.

**The output is staged, not a one-file application deployment.** `main.bicep`
references the infrastructure modules, but does not call the two Container App
modules. Those are separate because they require image information later:

- Entry-point inputs: `resourceGroupName`, `location`, and `principalId`.
- Compute inputs: `api_containerimage`, `frontend_containerimage`, and
  `api_containerport` (matching the built API image's HTTP listener, normally 8080).
- The other compute inputs are infrastructure outputs: environment ID/domain,
  registry endpoint/pull identity, API identity ID/client ID, and Cosmos endpoint.
  `cosmos_connectionString` is an endpoint URI, **not an account key**.

This publisher emits parameters in the Bicep files, not a populated parameters
file. It does not build images or fill in Azure-specific values. A separately
authorized deployment would build/push images and resolve these inputs through
the Aspire deployment pipeline. Applying templates or running `aspire deploy`
would create billable Azure resources and is **outside this demo**.

### Local versus published behavior

- Local `aspire run`/`start` still uses the preview emulator, Angular `ng serve`,
  the existing development `/api` proxy, and browser telemetry.
- At publish time, `PublishAsStaticWebsite("/api", api, ...)` replaces the dev server
  with YARP serving `dist/recipe-manager/browser`. It preserves the `/api` prefix,
  so the existing Angular relative URLs and API routes need no rewrites or CORS
  changes. Only the frontend is public in the generated Azure model.
- The AppHost now models the Cosmos container. Development startup still creates
  missing schema and seeds recipes; production uses the Bicep-created schema and
  seeds via data-plane calls, without trying to create databases/containers using
  the managed identity.
- `npm run build` generates credential-free browser telemetry configuration.
  `.dockerignore` also excludes local `.generated/` files from the image context.
  Server-side API/YARP telemetry remains separate from browser exporters.

The JavaScript publish API is experimental in Aspire 13.5.3 and its generated
YARP image is a preview image. Generated Azure APIs also include preview versions.
Bicep compilation succeeds with nonblocking unused-parameter warnings; it does
not prove Azure availability, permissions, image startup, or deployment success.

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

During local development, the API creates `recipesdb` and its `recipes` container
with `/id` as the partition key. The published Bicep models that same schema for
Azure. Stopping and recreating the local emulator can reset its data.
