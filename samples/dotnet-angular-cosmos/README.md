# Recipe Manager - ASP.NET Core + Angular + Azure CosmosDB

A modern recipe management application demonstrating .NET Aspire orchestration with:
- **ASP.NET Core** Minimal API backend
- **Angular 19** standalone component frontend  
- **Azure CosmosDB Emulator** (no Azure subscription needed)

## Architecture

This sample showcases a different technology stack compared to David Fowler's samples:
- **Angular** instead of React (modern standalone components)
- **CosmosDB** instead of PostgreSQL (NoSQL document database)
- **Aspire orchestration** for local development

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│   Angular   │─────▶│  ASP.NET    │─────▶│  CosmosDB   │
│   Frontend  │      │  Core API   │      │  Emulator   │
└─────────────┘      └─────────────┘      └─────────────┘
       │                     │                     │
       └─────────────────────┴─────────────────────┘
                      Aspire AppHost
```

## Features

- 📝 Create, read, update, delete recipes
- 🔍 Search recipes by title or ingredient
- 🏷️ Filter by category (Italian, Thai, Dessert, etc.)
- 📊 CosmosDB container auto-creation with sample data
- 🎨 Clean, modern UI with material-inspired design
- 🚀 Full observability with Aspire dashboard

## Prerequisites

- [.NET 9 SDK](https://dotnet.microsoft.com/download/dotnet/9.0)
- [Node.js 20+](https://nodejs.org/)
- [Azure CosmosDB Emulator](https://learn.microsoft.com/azure/cosmos-db/local-emulator) (Windows/macOS)
  - **Windows**: Download installer
  - **macOS/Linux**: Use Docker image or Linux emulator

## Getting Started

### 1. Install CosmosDB Emulator

**macOS/Linux (Docker):**
```bash
docker run -d --name cosmos-emulator \
  -p 8081:8081 -p 10250-10255:10250-10255 \
  mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest
```

**Windows:**
Download and install from [Microsoft Docs](https://learn.microsoft.com/azure/cosmos-db/local-emulator).

### 2. Run the Application

```bash
cd AppHost
dotnet run
```

This will:
1. Start the CosmosDB emulator connection
2. Launch the ASP.NET Core API
3. Start the Angular development server
4. Open the Aspire dashboard

### 3. Access the Application

- **Frontend**: http://localhost:4200
- **API**: http://localhost:5000/api/recipes
- **Aspire Dashboard**: Shown in console output
- **CosmosDB Emulator UI**: https://localhost:8081/_explorer/index.html

## Project Structure

```
dotnet-angular-cosmos/
├── AppHost/               # .NET Aspire orchestration
│   ├── Program.cs        # Defines cosmos, api, frontend resources
│   └── AppHost.csproj
├── Api/                   # ASP.NET Core Web API
│   ├── Program.cs        # Minimal API endpoints
│   ├── Models/
│   │   └── Recipe.cs     # Recipe data model
│   └── Api.csproj
├── ServiceDefaults/       # Shared Aspire service defaults
│   ├── Extensions.cs     # OpenTelemetry, health checks
│   └── ServiceDefaults.csproj
└── frontend/             # Angular application
    ├── src/
    │   ├── app/
    │   │   ├── app.component.ts      # Main UI component
    │   │   └── recipe.service.ts     # HTTP service
    │   ├── main.ts                   # Bootstrap
    │   ├── index.html
    │   └── styles.css
    ├── angular.json
    ├── package.json
    └── proxy.conf.json               # API proxy config
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/recipes` | List all recipes |
| GET    | `/api/recipes/{id}` | Get recipe by ID |
| POST   | `/api/recipes` | Create new recipe |
| PUT    | `/api/recipes/{id}` | Update recipe |
| DELETE | `/api/recipes/{id}` | Delete recipe |
| GET    | `/api/recipes/search?q={query}` | Search recipes |
| GET    | `/health` | Health check |

## Key Aspire Features

### CosmosDB Integration
```csharp
var cosmos = builder.AddAzureCosmosDB("cosmos")
    .RunAsEmulator();  // No Azure subscription needed!
var db = cosmos.AddDatabase("recipesdb");
```

### Service References
```csharp
var api = builder.AddProject<Projects.Api>("api")
    .WithReference(db)  // Inject connection string
    .WithExternalHttpEndpoints();
```

### NPM App Support
```csharp
var frontend = builder.AddNpmApp("frontend", "../frontend", "start")
    .WithReference(api)  // Discover API endpoint
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();
```

## Development

### Run API Only
```bash
cd Api
dotnet run
```

### Run Frontend Only
```bash
cd frontend
npm install
npm start
```

### View Cosmos Data
The API automatically:
- Creates the `recipesdb` database
- Creates the `recipes` container
- Seeds 3 sample recipes on first run

Use the CosmosDB Emulator Data Explorer to inspect data:
https://localhost:8081/_explorer/index.html

## Recipe Data Model

```csharp
public record Recipe
{
    public string Id { get; init; }                  // Unique ID (GUID)
    public string Title { get; init; }               // Recipe name
    public string Description { get; init; }         // Short description
    public List<string> Ingredients { get; init; }   // List of ingredients
    public string Instructions { get; init; }        // Step-by-step instructions
    public int CookTimeMinutes { get; init; }        // Cook time
    public string Category { get; init; }            // Italian, Thai, Dessert, etc.
}
```

## Technologies

- **.NET 9** - Latest .NET runtime
- **ASP.NET Core Minimal APIs** - Lightweight HTTP API
- **Angular 19** - Modern standalone components
- **Azure Cosmos DB SDK** - NoSQL document database client
- **.NET Aspire 9.2.1** - Cloud-native orchestration
- **OpenTelemetry** - Distributed tracing & metrics

## Differences from David Fowler's Samples

| Feature | David's Sample | This Sample |
|---------|---------------|-------------|
| Frontend | React | **Angular 19** (standalone) |
| Database | PostgreSQL | **Azure CosmosDB** (NoSQL) |
| DB Client | Npgsql | Microsoft.Azure.Cosmos |
| Data Access | Entity Framework | Cosmos SDK + LINQ |
| Port Config | Static | Dynamic via Aspire |

## Learn More

- [.NET Aspire Documentation](https://learn.microsoft.com/dotnet/aspire/)
- [Azure Cosmos DB Documentation](https://learn.microsoft.com/azure/cosmos-db/)
- [Angular Documentation](https://angular.dev)
- [ASP.NET Core Minimal APIs](https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis)

## License

MIT - Feel free to use this sample for learning and experimentation!
