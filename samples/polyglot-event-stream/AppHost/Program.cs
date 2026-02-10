var builder = DistributedApplication.CreateBuilder(args);

var kafka = builder.AddKafka("messaging")
    .WithKafkaUI();

var producer = builder.AddProject<Projects.EventProducer>("producer")
    .WithReference(kafka)
    .WithExternalHttpEndpoints();

var consumer = builder.AddPythonApp("consumer", "../python-consumer", "main.py")
    .WithReference(kafka);

var dashboard = builder.AddNpmApp("dashboard", "../node-dashboard", "start")
    .WithReference(kafka)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
