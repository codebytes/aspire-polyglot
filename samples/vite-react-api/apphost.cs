#:package Aspire.Hosting.Python@9.2.1
#:package Aspire.Hosting.NodeJs@9.2.1
#:package Aspire.Hosting.Redis@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

var cache = builder.AddRedis("cache");

var api = builder.AddPythonApp("api", "./src/api", "main.py")
    .WithReference(cache)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.AddNpmApp("web", "./src/web", "dev")
    .WithReference(api)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
