# Rust (Axum) + PostgreSQL guestbook

A single-file C# AppHost runs a **Rust** [Axum](https://github.com/tokio-rs/axum)
API **natively** with `cargo run`, next to PostgreSQL. Telemetry comes from the
`tracing` ecosystem: spans become OTLP traces, events become OTLP logs.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `api` | native `cargo run` | `AddRustApp` (`Aspire.Hosting.Rust`, preview) |
| `pg` / `guestbookdb` | PostgreSQL container + database | `AddPostgres().AddDatabase()` |

```csharp
var db = builder.AddPostgres("pg").AddDatabase("guestbookdb");

builder.AddRustApp("api", "api")
    .WithReference(db)
    .WaitFor(db)
    .WithHttpEndpoint(env: "PORT");
```

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, Rust 1.85+ (edition 2024), Docker.

```bash
cd samples/rust-axum-postgres
aspire run
```

The first start compiles the crate (about a minute); later starts are incremental.
Open the `api` URL and sign the guestbook. In the dashboard:

- **Traces**: `POST /api/entries` with a child `sign` span (from `#[instrument]`).
- **Structured logs**: `guestbook signed id=…`, linked to the request's trace.
- **Metrics**: the custom `guestbook.entries.signed` counter.

## How it is wired

- `WithReference(db)` injects `GUESTBOOKDB_URI`, which `tokio_postgres::connect` accepts as-is.
- `WithHttpEndpoint(env: "PORT")`: Rust has no port convention, so the app binds `PORT`.
- [`telemetry.rs`](api/src/telemetry.rs) builds OTLP/gRPC exporters (tonic + rustls).
  Endpoint, headers, and service name come from Aspire's standard `OTEL_*` variables.
- **TLS trust:** the exporters load native roots. Aspire adds its development
  certificate to a multi-directory `SSL_CERT_DIR`, but on Linux `cargo` rewrites that
  value before it starts the app. The AppHost therefore also sets `SSL_CERT_FILE` to
  Aspire's CA bundle via `WithCertificateTrustConfiguration`.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/` | Guestbook page |
| GET | `/api/entries` | Latest 20 entries |
| POST | `/api/entries` | Sign: `{"name": "...", "message": "..."}` |
| GET | `/health` | Health, including a database round trip |

## Build checks

```bash
cd api
cargo clippy
cargo fmt --check
```
