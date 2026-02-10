var builder = DistributedApplication.CreateBuilder(args);

var api = builder.AddDockerfile("api", "../go-api")
    .WithHttpEndpoint(targetPort: 8080, name: "http")
    .WithExternalHttpEndpoints();

var frontend = builder.AddNpmApp("frontend", "../frontend", "dev")
    .WithEnvironment("services__api__http__0", api.GetEndpoint("http"))
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
