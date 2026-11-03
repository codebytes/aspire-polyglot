#:sdk Aspire.AppHost.Sdk@13.6.4
#:property AspireUseCliBundle=true
#:package Aspire.Hosting.PostgreSQL@13.6.4
#:package Aspire.Hosting.Rust@13.6.4-preview.1.26523.11

// Single-file C# AppHost: a Rust (Axum) guestbook API and PostgreSQL.
// The API runs NATIVELY with `cargo run`; PostgreSQL runs in a container.

var builder = DistributedApplication.CreateBuilder(args);

// PostgreSQL plus a database. Referencing it injects GUESTBOOKDB_URI.
var db = builder.AddPostgres("pg").AddDatabase("guestbookdb");

// First-party Rust hosting: `cargo run` in ./api, rebuilt on every start.
builder.AddRustApp("api", "api")
    .WithReference(db)
    .WaitFor(db)
    .WithHttpEndpoint(env: "PORT")     // Rust reads PORT itself; it has no default
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints()
    // Aspire trusts its dev certificate through a multi-directory SSL_CERT_DIR.
    // On Linux, cargo replaces that value before starting the app, so also pass a
    // single CA bundle (system roots + dev certificate) for rustls to load.
    .WithCertificateTrustConfiguration(context =>
    {
        context.EnvironmentVariables["SSL_CERT_FILE"] = context.CertificateBundlePath;
        return Task.CompletedTask;
    });

builder.Build().Run();
