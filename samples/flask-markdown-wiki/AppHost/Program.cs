var builder = DistributedApplication.CreateBuilder(args);

var wiki = builder.AddPythonApp("wiki", "../src", "main.py")
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
