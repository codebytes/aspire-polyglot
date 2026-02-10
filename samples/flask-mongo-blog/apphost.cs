#:package Aspire.Hosting.Python@9.2.1
#:package Aspire.Hosting.MongoDB@9.2.1
#:sdk Aspire.AppHost.Sdk@9.2.1

var builder = DistributedApplication.CreateBuilder(args);

var mongo = builder.AddMongoDB("mongodb")
    .WithMongoExpress();

var db = mongo.AddDatabase("blogdb");

builder.AddPythonApp("blog", "./src", "main.py")
    .WithReference(db)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
