#:sdk Aspire.AppHost.Sdk@13.4.6
#:package Aspire.Hosting.Python@13.4.6
#:package Aspire.Hosting.Docker@13.4.6

// Single-file C# AppHost whose only service is a Python FastAPI app. In local
// development the app runs NATIVELY: Aspire creates a virtual environment from
// requirements.txt and launches uvicorn on the host — no Dockerfile needed to
// develop. On `aspire publish`, Aspire AUTO-GENERATES a Dockerfile for the
// Python service and emits a docker-compose.yaml, so the same app ships as a
// container image. Polyglot: C# AppHost orchestrating a Python service.

var builder = DistributedApplication.CreateBuilder(args);

// Docker Compose is the publish target. `aspire publish` writes a Dockerfile for
// the Python app plus a docker-compose.yaml that references the built image.
builder.AddDockerComposeEnvironment("compose");

// FastAPI via uvicorn — runs on the host in dev, containerized on publish.
builder.AddUvicornApp("api", "app", "main:app")
    .WithHttpEndpoint(env: "PORT")
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints();

builder.Build().Run();
