# Spring Boot + PostgreSQL (Java AppHost preview)

A Java-authored Aspire AppHost orchestrates a **Java 21 / Spring Boot 3.5** notes
API and PostgreSQL. Both workloads run in containers; Maven builds the Spring
application in a separate Docker build stage.

## Prerequisites

- Aspire CLI 13.5.3 and .NET 10 SDK.
- A JDK on `PATH` to compile the preview AppHost (validated with Temurin 25).
- Docker with Linux containers. No host Maven installation is needed.

Java AppHost support is experimental and enabled in `aspire.config.json`.
The Aspire CLI restores `.aspire/modules/`; do not edit those generated bindings.

## Run

```bash
cd samples/preview/spring-boot-postgres
aspire run --apphost AppHost.java
```

For worktrees, use `aspire start --apphost AppHost.java --isolated`.
Open the `api` resource's assigned endpoint in the dashboard. The application is
an **API demo**, not a custom web frontend: append `/api/notes` to see its JSON
response in a browser, or use the requests below.

Stop it with `aspire stop --apphost AppHost.java`.

## Demo requests

Copy the API URL from `aspire describe --apphost AppHost.java`; do not assume
host port 8080. That is the container's internal port.

```bash
API_URL="http://localhost:PORT" # Replace PORT with Aspire's assigned host port.

curl -X POST "$API_URL/api/notes" \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","content":"My first note"}'

curl "$API_URL/api/notes"
```

Use the returned numeric `id` with `GET /api/notes/{id}` or
`DELETE /api/notes/{id}`. Creating a note returns HTTP 200 with the saved entity;
missing notes return 404. Data survives restarting the API while PostgreSQL
remains running.

## What the AppHost wires

- `addContainer("pg", "postgres:16")` creates PostgreSQL with the local demo database.
- `addDockerfile("api", "./src")` builds Spring Boot with Maven and runs the jar on a JRE.
- `NOTESDB_JDBCCONNECTIONSTRING`, `NOTESDB_USERNAME`, and `NOTESDB_PASSWORD` explicitly
  configure the datasource. This sample uses a raw container, not `AddPostgres`.
- `withOtlpExporter()` sends Java telemetry to Aspire; the container entrypoint
  imports Aspire's development certificate into its JRE truststore.

This sample **does not start pgAdmin**. Inspect data using the API or PostgreSQL
tools, and use Aspire for resource status and telemetry. Database credentials
are local-demo defaults, and no named volume is configured: recreating PostgreSQL
can reset the notes.
