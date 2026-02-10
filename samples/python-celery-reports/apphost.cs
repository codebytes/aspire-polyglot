#:package Aspire.Hosting.Python@9.2.1
#:package Aspire.Hosting.Redis@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

var redis = builder.AddRedis("broker");

builder.AddPythonApp("api", "./src", "main.py")
    .WithReference(redis)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.AddPythonApp("worker", "./src", "worker.py")
    .WithReference(redis);

builder.Build().Run();
