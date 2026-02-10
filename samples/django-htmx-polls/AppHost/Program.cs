var builder = DistributedApplication.CreateBuilder(args);

var polls = builder.AddPythonApp("polls", "../src", "run.py")
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
