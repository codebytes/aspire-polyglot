var builder = DistributedApplication.CreateBuilder(args);

var redis = builder.AddRedis("broker");

var api = builder.AddPythonApp("api", "../src", "main.py")
    .WithReference(redis)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

var worker = builder.AddPythonApp("worker", "../src", "worker.py")
    .WithReference(redis);

builder.Build().Run();
