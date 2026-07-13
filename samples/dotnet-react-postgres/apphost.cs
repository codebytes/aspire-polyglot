#:sdk Aspire.AppHost.Sdk@13.4.6
#:package Aspire.Hosting.PostgreSQL@13.4.6
#:package Aspire.Hosting.JavaScript@13.4.6
#:project Api/Api.csproj

// Single-file C# AppHost (apphost.cs). No .csproj, no Program.cs — the whole
// dev-time orchestrator is this one file. `aspire run` compiles and runs it.
// The #: directives above pull in the AppHost SDK, the hosting integrations,
// and a reference to the Api project (surfaced as the generated Projects.Api type).

var builder = DistributedApplication.CreateBuilder(args);

// PostgreSQL container with a persistent data volume and one database.
var db = builder.AddPostgres("postgres")
    .WithDataVolume()
    .AddDatabase("quotesdb");

// ASP.NET Core Minimal API. WithReference injects the connection string and
// WaitFor holds startup until Postgres reports healthy.
var api = builder.AddProject<Projects.Api>("api")
    .WithReference(db)
    .WaitFor(db)
    .WithExternalHttpEndpoints();

// Vite + React dev server. AddViteApp runs `npm run dev` and injects the API
// endpoint via service discovery so vite.config.ts can proxy /api to it.
var web = builder.AddViteApp("web", "./frontend")
    .WithReference(api)
    .WaitFor(api);

// For `aspire publish`, bake the built SPA into the API container under wwwroot.
api.PublishWithContainerFiles(web, "wwwroot");

builder.Build().Run();
