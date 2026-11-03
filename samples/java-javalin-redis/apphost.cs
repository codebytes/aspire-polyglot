#:sdk Aspire.AppHost.Sdk@13.6.4
#:property AspireUseCliBundle=true
#:package Aspire.Hosting.Redis@13.6.4
#:package Aspire.Hosting.Java@13.6.4-preview.1.26523.11

// Single-file C# AppHost: a Javalin (Java 21) leaderboard API and Redis.
// The API runs as a NATIVE JVM process: Aspire builds it with the project's
// Maven wrapper, then runs the JAR with the OpenTelemetry Java agent attached.

var builder = DistributedApplication.CreateBuilder(args);

// Redis with a generated password. Referencing it injects CACHE_URI.
var cache = builder.AddRedis("cache");

builder.AddJavaApp("api", "src/api", "target/leaderboard.jar")
    .WithMavenBuild("package", "-DskipTests") // a child build resource; tests run separately
    .WithOtelAgent()                           // target/agent/opentelemetry-javaagent.jar
    .WithReference(cache)
    .WaitFor(cache)
    .WithHttpEndpoint(env: "PORT")
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints();

builder.Build().Run();
