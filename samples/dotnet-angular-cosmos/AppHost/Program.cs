#pragma warning disable ASPIRECOSMOSDB001, ASPIREJAVASCRIPT001

var builder = DistributedApplication.CreateBuilder(args);

builder.AddAzureContainerAppEnvironment("aca");

var cosmos = builder.AddAzureCosmosDB("cosmos")
    .RunAsPreviewEmulator(emulator =>
    {
        emulator.WithDataExplorer();
    });
var db = cosmos.AddCosmosDatabase("recipesdb");
db.AddContainer("recipes", "/id");

var api = builder.AddProject<Projects.Api>("api")
    .WithReference(db)
    .WaitFor(db)
    .WithHttpEndpoint();

if (builder.ExecutionContext.IsRunMode)
{
    api.WithExternalHttpEndpoints();
}

// Browser exporters need the HTTP collector and its authentication headers.
var frontend = builder.AddJavaScriptApp("frontend", "../frontend", "start")
    .WithReference(api)
    .WaitFor(api)
    .WithOtlpExporter(OtlpProtocol.HttpProtobuf)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints()
    .WithEnvironment("OTEL_SERVICE_NAME", "frontend")
    .WithEnvironment("OTEL_SERVICE_VERSION", "1.0.0")
    .PublishAsStaticWebsite("/api", api, options =>
    {
        options.OutputPath = "dist/recipe-manager/browser";
    });

builder.Build().Run();
