#pragma warning disable ASPIRECOSMOSDB001

var builder = DistributedApplication.CreateBuilder(args);

var cosmos = builder.AddAzureCosmosDB("cosmos")
    .RunAsPreviewEmulator(emulator =>
    {
        emulator.WithDataExplorer();
    });
var db = cosmos.AddCosmosDatabase("recipesdb");

var api = builder.AddProject<Projects.Api>("api")
    .WithReference(db)
    .WaitFor(db)
    .WithHttpEndpoint()
    .WithExternalHttpEndpoints();

// Angular dev server. The browser SDK cannot speak gRPC, so we hand it the
// dashboard's HTTP/protobuf OTLP endpoint via OTEL_EXPORTER_OTLP_ENDPOINT_HTTP.
// scripts/write-otel-config.cjs picks that up and writes /assets/otel-config.js
// before `ng serve` starts.
var frontend = builder.AddJavaScriptApp("frontend", "../frontend", "start")
    .WithReference(api)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints()
    .WithEnvironment("OTEL_SERVICE_NAME", "frontend")
    .WithEnvironment("OTEL_SERVICE_VERSION", "1.0.0")
    .WithEnvironment(ctx =>
    {
        // Aspire sets OTEL_EXPORTER_OTLP_ENDPOINT to the dashboard's gRPC port.
        // The dashboard also exposes HTTP/protobuf at DASHBOARD_OTLP_HTTP_ENDPOINT_URL.
        // Prefer the HTTP one for the browser; fall back to rewriting the gRPC URL.
        var http = builder.Configuration["DOTNET_DASHBOARD_OTLP_HTTP_ENDPOINT_URL"]
                   ?? builder.Configuration["ASPIRE_DASHBOARD_OTLP_HTTP_ENDPOINT_URL"]
                   ?? Environment.GetEnvironmentVariable("DOTNET_DASHBOARD_OTLP_HTTP_ENDPOINT_URL")
                   ?? Environment.GetEnvironmentVariable("ASPIRE_DASHBOARD_OTLP_HTTP_ENDPOINT_URL");
        if (!string.IsNullOrEmpty(http))
        {
            ctx.EnvironmentVariables["OTEL_EXPORTER_OTLP_ENDPOINT_HTTP"] = http;
        }
    });

builder.Build().Run();
