#:package Aspire.Hosting.PostgreSQL@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

var db = builder.AddPostgres("pg")
    .WithPgAdmin()
    .AddDatabase("notesdb");

builder.AddDockerfile("api", "./src")
    .WithReference(db)
    .WithHttpEndpoint(targetPort: 8080, env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
