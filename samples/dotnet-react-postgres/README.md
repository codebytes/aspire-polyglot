# .NET + React + PostgreSQL (single-file C# AppHost)

A full-stack quotes board orchestrated by a **single-file C# AppHost** — the entire
dev-time orchestrator is one `apphost.cs`, with no `.csproj` and no `Program.cs`.
The API resource also exposes a **Seed quotes** command with a named count argument
and an **Import quotes** command with a dashboard file picker.

## Tech Stack
- **Backend**: ASP.NET Core Minimal API (`Api/`) using the `Aspire.Npgsql` client integration
- **Database**: PostgreSQL (container, with a persistent data volume)
- **Frontend**: Vite + React (TypeScript), run via `AddViteApp`
- **AppHost**: single-file `apphost.cs` (`#:sdk` / `#:package` / `#:project` directives)

## Running

```bash
cd samples/dotnet-react-postgres
aspire run
```

**Prerequisites:** [Aspire CLI](https://aspire.dev/get-started/install-cli/), [Docker](https://docs.docker.com/get-docker/), the .NET 10 SDK, and Node 20+.

Aspire starts PostgreSQL, the API, and the Vite dev server, and opens the dashboard.
The React app is available from the `web` resource; it proxies `/api` to the API using
the endpoint Aspire injects via service discovery.

## Demo: From README Steps to Resource Commands

To keep the terminal available for commands, use this instead of `aspire run`:

```bash
aspire start --apphost apphost.cs --isolated
aspire wait api --apphost apphost.cs --status healthy
```

Open the dashboard URL printed by Aspire, then open the `web` resource URL in
another tab. Keep the Quotes Board visible alongside the dashboard or terminal.

### 1. Seed from the CLI or dashboard

From this sample directory:

```bash
aspire resource api seed --help --apphost apphost.cs
aspire resource api seed --count 5 --apphost apphost.cs
```

The command returns JSON containing `added` and the inserted `ids`. In the Quotes
Board, click **Refresh quotes**: five new quotes and the updated count appear.
Omitting `--count` uses **5**; accepted values are whole numbers from **1 to 100**.

For the same workflow without a terminal, open the `api` resource's **Actions**
menu, choose **Seed quotes**, enter a count, and select **Seed quotes** in the
dialog. The dashboard opens the structured JSON result.

Try `--count 0` to demonstrate argument validation: the command fails without
adding rows. The dashboard also rejects invalid counts in its input dialog.

### 2. Import through the dashboard file picker

1. On the `api` resource, choose **Actions > Import quotes**.
2. Choose [`demo/quotes.json`](demo/quotes.json) and select **OK**.
3. Inspect the result (`added: 3` and three IDs), then click **Refresh quotes** in
   the board to see the imported entries.

The file contains a JSON array:

```json
[
  { "text": "Keep the workflow next to the resources it changes.", "author": "Resource Commands Demo" }
]
```

Only `text` and `author` string fields are accepted. Both are required and trimmed.
Imports accept **1-100 quotes**, at most **128 KiB** of UTF-8 JSON, **1,000 characters**
per quote text, and **200 characters** per author. A UTF-8 byte-order mark is accepted.
The file picker filters `.json` files; the command and API also validate the actual
content and byte count rather than trusting the filename.

Choose [`demo/invalid-quotes.json`](demo/invalid-quotes.json) to show a failed import:
its second entry has no author, so **neither entry is inserted**. Canceling the file
dialog before confirming also leaves the board unchanged.

### Behavior and implementation

- Both commands **append** data; they never clear existing quotes. Repeating an
  import creates duplicates. If a request times out or loses its response, refresh
  the board before retrying.
- Commands are enabled only while the API is healthy. **Import quotes** is a
  dashboard-only workflow; non-interactive CLI attempts fail with instructions to
  open the dashboard instead of waiting for input.
- `CommandOptions.Arguments` and `ValidateArguments` implement the shared seed
  input; `IInteractionService.PromptInputAsync` with `InputType.File` implements
  the upload. Results use `CommandResultFormat.Json`.
- `Api/QuoteImports.cs` shares count, JSON, and size validation with the AppHost.
  `TreatProjectReferencesAsResources=false` makes `#:project Api/Api.csproj` a
  normal assembly reference; `AddProject("api", "Api/Api.csproj")` separately
  registers the runnable resource.
- The AppHost calls the API's discovered HTTP endpoint, not a hardcoded port or
  the database directly. The API validates the whole batch before inserting all
  rows in one PostgreSQL transaction. Validation failures return HTTP 400 and are
  logged; oversized request bodies are read only up to the limit plus one byte.
- `POST /api/quotes/import` is registered only in **Development**. These are local
  demo operations, not production administration endpoints. The existing list and
  single-quote creation endpoints are unchanged.

Stop the demo with `aspire stop --apphost apphost.cs`. The PostgreSQL data volume
is preserved; do not use `--force` to end the walkthrough.

## Checks

From this sample directory:

```bash
dotnet test Api.Tests/Api.Tests.csproj
npm --prefix frontend run build
npm --prefix frontend run lint
```

The regression tests cover count bounds, JSON shape and required fields, whole-batch
validation, trimming, Unicode, field lengths, UTF-8 BOMs, exact file-size boundaries,
and cancellation.

## What It Demonstrates
- A **single-file `apphost.cs`** — `#:sdk Aspire.AppHost.Sdk`, `#:package` for the
  PostgreSQL and JavaScript hosting integrations, and `#:project Api/Api.csproj` to
  reuse API validation while registering the API resource explicitly by path.
- `AddPostgres(...).WithDataVolume().AddDatabase(...)` for a persisted database.
- `WithReference` + `WaitFor` to inject the connection string and order startup.
- `AddViteApp` for a host-run Vite dev server, plus `PublishWithContainerFiles`
  to bake the built SPA into the API container for `aspire publish`.
- The `Aspire.Npgsql` client integration wiring up `NpgsqlDataSource`, OpenTelemetry,
  and health checks from the injected `ConnectionStrings__quotesdb`.
- Named resource-command arguments and dashboard file inputs using the same
  API-backed, transactional import operation.

## Project Layout

```
apphost.cs            single-file C# AppHost (the orchestrator)
aspire.config.json    points aspire at apphost.cs
apphost.run.json      launch profiles (dashboard/OTLP ports)
Api/                  ASP.NET Core Minimal API (list, create, and import quotes)
Api.Tests/            shared validation regression tests
demo/                 valid and invalid JSON import fixtures
ServiceDefaults/      shared OpenTelemetry / health / service-discovery wiring
frontend/             Vite + React SPA
```
