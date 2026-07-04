var builder = DistributedApplication.CreateBuilder(args);

var kafka = builder.AddKafka("messaging")
    .WithKafkaUI();

var producer = builder.AddProject<Projects.EventProducer>("producer")
    .WithReference(kafka)
    .WaitFor(kafka)
    .WithHttpEndpoint()
    .WithExternalHttpEndpoints();

var consumer = builder.AddPythonApp("consumer", "../python-consumer", "main.py")
    .WithReference(kafka)
    .WaitFor(kafka);

var dashboard = builder.AddJavaScriptApp("dashboard", "../node-dashboard", "start")
    .WithReference(kafka)
    .WaitFor(kafka)
    .WithHttpEndpoint(env: "PORT")
    .WithExternalHttpEndpoints();

builder.Build().Run();
