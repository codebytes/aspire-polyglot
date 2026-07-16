#:sdk Aspire.AppHost.Sdk@13.4.6

// Single-file C# AppHost that adds two PUBLIC Docker images directly — no
// hosting-integration packages, no Dockerfiles, and no app projects. The whole
// orchestrator is `AddContainer` calls for PostgreSQL and Adminer, wired
// together on Aspire's container network. `aspire run` pulls the images and
// starts everything, with logs, metrics, and traces in the dashboard.

var builder = DistributedApplication.CreateBuilder(args);

// PostgreSQL, straight from the public image, with a persistent named volume.
var db = builder.AddContainer("db", "postgres", "17-alpine")
    .WithEnvironment("POSTGRES_USER", "postgres")
    .WithEnvironment("POSTGRES_PASSWORD", "postgres")
    .WithEnvironment("POSTGRES_DB", "appdb")
    .WithVolume("postgres-adminer-data", "/var/lib/postgresql/data")
    .WithEndpoint(targetPort: 5432, name: "sql");

// Adminer — a single published image that serves a web UI for the database.
// ADMINER_DEFAULT_SERVER points at the "db" container by its resource name,
// which Aspire resolves to the right host on the shared container network.
builder.AddContainer("adminer", "adminer", "5")
    .WithEnvironment("ADMINER_DEFAULT_SERVER", "db")
    .WithHttpEndpoint(targetPort: 8080, name: "http")
    .WithExternalHttpEndpoints()
    .WaitFor(db);

builder.Build().Run();
