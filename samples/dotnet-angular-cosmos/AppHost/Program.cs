var builder = DistributedApplication.CreateBuilder(args);

var cosmos = builder.AddAzureCosmosDB("cosmos")
    .RunAsEmulator();
var db = cosmos.AddCosmosDatabase("recipesdb");

var api = builder.AddProject<Projects.Api>("api")
    .WithReference(db)
    .WithExternalHttpEndpoints();

var frontend = builder.AddJavaScriptApp("frontend", "../frontend", "start")
    .WithReference(api)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
