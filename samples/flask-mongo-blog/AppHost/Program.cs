var builder = DistributedApplication.CreateBuilder(args);

var mongo = builder.AddMongoDB("mongodb")
    .WithMongoExpress();

var db = mongo.AddDatabase("blogdb");

var blog = builder.AddPythonApp("blog", "../src", "main.py")
    .WithReference(db)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
