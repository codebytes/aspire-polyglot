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

// Browser exporters need the HTTP collector and its authentication headers.
var frontend = builder.AddJavaScriptApp("frontend", "../frontend", "start")
    .WithReference(api)
    .WaitFor(api)
    .WithOtlpExporter(OtlpProtocol.HttpProtobuf)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints()
    .WithEnvironment("OTEL_SERVICE_NAME", "frontend")
    .WithEnvironment("OTEL_SERVICE_VERSION", "1.0.0");

builder.Build().Run();
