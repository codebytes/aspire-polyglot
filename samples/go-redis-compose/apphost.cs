#:sdk Aspire.AppHost.Sdk@13.6.4
#:property AspireUseCliBundle=true
#:package Aspire.Hosting.Docker@13.6.4
#:package Aspire.Hosting.Redis@13.6.4
#:package Aspire.Hosting.Go@13.6.4-preview.1.26523.11

// Single-file C# AppHost: a Go API and Redis.
//
//   aspire run      -> the Go API runs NATIVELY with `go run .`; Redis runs in a container.
//   aspire publish  -> a docker-compose.yaml plus a generated Dockerfile for the Go API.

var builder = DistributedApplication.CreateBuilder(args);

// Publish target. Every compute resource below becomes a Compose service.
builder.AddDockerComposeEnvironment("compose");

// Redis with a generated password. Referencing it injects CACHE_URI.
var cache = builder.AddRedis("cache");

// First-party Go hosting: `go run .` in src/api, with the Go toolchain on PATH.
builder.AddGoApp("api", "src/api")
    .WithReference(cache)
    .WaitFor(cache)
    .WithHttpEndpoint(env: "PORT")
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints();

builder.Build().Run();
