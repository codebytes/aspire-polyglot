#:sdk Aspire.AppHost.Sdk@13.4.6
#:package Aspire.Hosting.Docker@13.4.6

// Single-file C# AppHost that targets a Docker Compose ENVIRONMENT. Running
// `aspire publish` turns this model into a docker-compose.yaml you can deploy
// with `docker compose up`; `aspire run` still runs everything locally with the
// dashboard. Polyglot: the AppHost is C#, the API is Go, the store is Redis.

var builder = DistributedApplication.CreateBuilder(args);

// Publish target: a Docker Compose environment. With this present, every
// resource below is automatically emitted as a service in the generated
// docker-compose.yaml — no per-resource opt-in required.
builder.AddDockerComposeEnvironment("compose");

// Redis (public image) — becomes an `image: redis` service in the compose output.
var cache = builder.AddContainer("cache", "redis", "7-alpine");

// Go hit-counter API built from ./src/api — becomes a `build:` service in the
// compose output. It reaches Redis by the container hostname "cache".
builder.AddDockerfile("api", "src/api")
    .WithEnvironment("REDIS_HOST", "cache")
    .WithEnvironment("REDIS_PORT", "6379")
    .WithHttpEndpoint(targetPort: 8080, env: "PORT")
    .WithExternalHttpEndpoints()
    .WaitFor(cache);

builder.Build().Run();
