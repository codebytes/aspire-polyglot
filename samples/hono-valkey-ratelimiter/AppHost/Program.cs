var builder = DistributedApplication.CreateBuilder(args);

var valkey = builder.AddValkey("cache");

var api = builder.AddNpmApp("api", "../src", "start")
    .WithReference(valkey)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
