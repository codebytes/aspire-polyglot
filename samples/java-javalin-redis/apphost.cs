#:sdk Aspire.AppHost.Sdk@13.4.6

// Single-file C# AppHost orchestrating a Java service. The API is a lightweight
// Javalin app built from a multi-stage Dockerfile (Maven build + JRE runtime),
// so it runs and publishes as a container with no host JDK required. Polyglot:
// C# AppHost, Java (Javalin) API, Redis store.

var builder = DistributedApplication.CreateBuilder(args);

// Redis holds the leaderboard (a sorted set). Public image, no app code.
var cache = builder.AddContainer("cache", "redis", "7-alpine");

// Java Javalin API built from ./src/api. It reaches Redis by the container
// hostname "cache" on Aspire's network.
builder.AddDockerfile("api", "src/api")
    .WithEnvironment("REDIS_HOST", "cache")
    .WithEnvironment("REDIS_PORT", "6379")
    .WithHttpEndpoint(targetPort: 8080, env: "PORT")
    .WithExternalHttpEndpoints()
    .WaitFor(cache);

builder.Build().Run();
