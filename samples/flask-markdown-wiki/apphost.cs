#:package Aspire.Hosting.Python@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

builder.AddPythonApp("wiki", "./src", "main.py")
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
