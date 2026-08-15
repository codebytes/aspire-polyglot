# Standalone Dashboard: No AppHost Required

The Aspire dashboard ships as a **single container**. Point any app with an
OpenTelemetry SDK at its OTLP endpoint and you get **logs, traces, and metrics** —
no AppHost, no .NET, no `aspire` CLI, no code rewrite. This sample proves it with
**three workers in three languages** (Node.js, Python, Go) all reporting to one
dashboard.

> This is the "already on OTEL? get the dashboard with zero rewrites" story. It's
> a great migration on-ramp: start here for observability today, adopt the AppHost
> later when you want service discovery, integrations, and `aspire deploy`.

## Run it

```bash
cd samples/standalone-dashboard
docker compose up --build        # or:  ./run.sh   /   ./run.ps1
```

Then open **<http://localhost:18888>** and watch the **Structured Logs**,
**Traces**, and **Metrics** tabs fill with data from `node-worker`,
`python-worker`, and `go-worker`. Tear down with `docker compose down`.

> **No Docker Compose?** You still only need one container for the dashboard:
>
> ```bash
> docker run --rm -it -p 18888:18888 -p 4317:18889 -p 4318:18890 \
>   -e DOTNET_DASHBOARD_UNSECURED_ALLOW_ANONYMOUS=true \
>   -d --name aspire-dashboard \
>   mcr.microsoft.com/dotnet/aspire-dashboard:latest
> ```
>
> Then point any OTEL app at `http://localhost:4317` (gRPC) or `4318` (HTTP).

## What it demonstrates

- **The dashboard with no AppHost.** `mcr.microsoft.com/dotnet/aspire-dashboard`
  is a standalone OTLP receiver + UI. The `.NET`/AppHost side of Aspire is
  completely optional.
- **Truly polyglot telemetry.** Each worker uses only its language's OpenTelemetry
  SDK and the standard env vars — no Aspire-specific libraries:
  - `OTEL_EXPORTER_OTLP_ENDPOINT` — where to send telemetry
  - `OTEL_EXPORTER_OTLP_PROTOCOL` — `grpc` here
  - `OTEL_SERVICE_NAME` — the name shown in the dashboard
- **All three signals.** Every worker emits a parent/child **trace** (with ~12%
  error traces), a counter + histogram **metric**, and structured **logs**.

## How the wiring works

Inside the Compose network the workers reach the dashboard's OTLP/gRPC listener at
`http://dashboard:18889`. The dashboard maps that to your host as
`localhost:4317`, so an app running on your machine (outside Compose) would use
`http://localhost:4317` instead.

| Dashboard port | Purpose | Host mapping |
|----------------|---------|--------------|
| `18888` | Dashboard UI | `http://localhost:18888` |
| `18889` | OTLP/gRPC ingestion | `http://localhost:4317` |
| `18890` | OTLP/HTTP ingestion | `http://localhost:4318` |

`DOTNET_DASHBOARD_UNSECURED_ALLOW_ANONYMOUS=true` disables the browser login token
**and** the OTLP API key so the demo runs with zero auth setup. **Local demo only —
never do this in production.** (Without it, the dashboard prints a login URL with a
token in its container logs, and OTLP requires an API key.)

## Wire your own app in 30 seconds

Any OTEL SDK works. For example, Node.js:

```javascript
import { NodeSDK } from "@opentelemetry/sdk-node";
import { OTLPTraceExporter } from "@opentelemetry/exporter-trace-otlp-grpc";

const sdk = new NodeSDK({
  traceExporter: new OTLPTraceExporter({ url: "http://localhost:4317" }),
});
sdk.start();
```

Or skip the code and just set env vars before launching your app:

```bash
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
export OTEL_SERVICE_NAME=my-app
```

## Project layout

```
docker-compose.yml        dashboard + 3 polyglot OTEL workers
run.sh / run.ps1          one-command launcher
apps/
  node-worker/            Node.js OTEL emitter (traces + metrics + logs)
  python-worker/          Python OTEL emitter (traces + metrics + logs)
  go-worker/              Go OTEL emitter (traces + metrics + logs)
```
