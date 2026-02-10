#:package Aspire.Hosting.NodeJs@9.2.1
#:package Aspire.Hosting.Valkey@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

var valkey = builder.AddValkey("cache");

builder.AddNpmApp("api", "./src", "start")
    .WithReference(valkey)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
