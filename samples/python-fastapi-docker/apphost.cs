#:sdk Aspire.AppHost.Sdk@13.6.4
#:property AspireUseCliBundle=true
#:package Aspire.Hosting.Python@13.6.4
#:package Aspire.Hosting.Docker@13.6.4

// Single-file C# AppHost for a Python FastAPI service.
//
//   aspire run      -> uvicorn runs NATIVELY from a virtual environment Aspire
//                      creates from app/requirements.txt. No Dockerfile needed.
//   aspire publish  -> a generated Dockerfile for the app plus docker-compose.yaml.

var builder = DistributedApplication.CreateBuilder(args);

// Publish target: Docker Compose.
builder.AddDockerComposeEnvironment("compose");

// uvicorn main:app in ./app, with an HTTPS endpoint and OTLP settings injected.
builder.AddUvicornApp("api", "app", "main:app")
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints();

builder.Build().Run();
