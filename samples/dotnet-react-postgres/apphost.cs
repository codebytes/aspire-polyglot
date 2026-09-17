#:sdk Aspire.AppHost.Sdk@13.5.3
#:property AspireUseCliBundle=true
#:property TreatProjectReferencesAsResources=false
#:package Aspire.Hosting.PostgreSQL@13.5.3
#:package Aspire.Hosting.JavaScript@13.5.3
#:project Api/Api.csproj

using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json;
using Api;
using Aspire.Hosting.ApplicationModel;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using Microsoft.Extensions.Logging;

// Single-file C# AppHost (apphost.cs). No .csproj, no Program.cs — the whole
// dev-time orchestrator is this one file. `aspire run` compiles and runs it.
// The #: directives above pull in the AppHost SDK, the hosting integrations,
// and a normal Api assembly reference so commands share its validation and DTOs.
// Register the project resource by path because references are not resource metadata.

var builder = DistributedApplication.CreateBuilder(args);
builder.Services.AddHttpClient("quote-commands", client => client.Timeout = TimeSpan.FromSeconds(30));

// PostgreSQL container with a persistent data volume and one database.
var db = builder.AddPostgres("postgres")
    .WithDataVolume()
    .AddDatabase("quotesdb");

// ASP.NET Core Minimal API. WithReference injects the connection string and
// WaitFor holds startup until Postgres reports healthy.
var api = builder.AddProject("api", "Api/Api.csproj")
    .WithReference(db)
    .WaitFor(db)
    .WithHttpHealthCheck("/health")
    .WithExternalHttpEndpoints();

api.WithCommand("seed", "Seed quotes", async context =>
{
    if (!QuoteImports.TryParseCount(context.Arguments.GetString("count"), out var count))
    {
        context.Logger.LogWarning("Seed quotes rejected: {Reason}", QuoteImports.CountError);
        return CommandResults.Failure(QuoteImports.CountError);
    }

    return await SendBatchAsync(QuoteImports.CreateSeedPayload(count), count, context);
}, commandOptions: new CommandOptions
{
    Description = "Append 1-100 demo quotes. Available from the dashboard and CLI.",
    IsHighlighted = true,
    UpdateState = CommandState,
    Arguments =
    [
        new InteractionInput
        {
            Name = "count", Label = "Number of quotes (1-100)",
            InputType = InputType.Number, Required = true, Value = "5"
        }
    ],
    ValidateArguments = context =>
    {
        if (!QuoteImports.TryParseCount(context.Inputs.GetString("count"), out _))
        {
            context.AddValidationError("count", QuoteImports.CountError);
        }
        return Task.CompletedTask;
    }
});

api.WithCommand("import-quotes", "Import quotes", async context =>
{
    var interaction = context.Services.GetRequiredService<IInteractionService>();
    if (!interaction.IsAvailable)
    {
        context.Logger.LogWarning("Import quotes requires an interactive dashboard");
        return CommandResults.Failure("Open the Aspire dashboard to import a JSON file.");
    }

    var result = await interaction.PromptInputAsync(
        "Import quotes",
        "Choose a JSON array of 1-100 quotes with text and author fields (maximum 128 KiB).",
        new InteractionInput
        {
            Name = "quotes", Label = "Quotes JSON file", InputType = InputType.File,
            Required = true, FileFilter = ".json", MaxFileSize = QuoteImports.MaxFileBytes,
            AllowMultipleFiles = false
        },
        cancellationToken: context.CancellationToken);

    if (result.Canceled)
    {
        context.Logger.LogInformation("Quote import canceled; no quotes were added");
        return CommandResults.Canceled();
    }

    var files = result.Data?.Files;
    if (files is null || files.Count != 1)
    {
        context.Logger.LogWarning("Quote import rejected: select exactly one JSON file");
        return CommandResults.Failure("Select exactly one JSON file.");
    }

    try
    {
        await using var stream = files[0].OpenRead();
        var payload = await QuoteImports.ReadPayloadAsync(stream, context.CancellationToken);
        var quotes = QuoteImports.Parse(payload);
        return await SendBatchAsync(payload, quotes.Length, context);
    }
    catch (InvalidDataException exception)
    {
        context.Logger.LogWarning("Quote import rejected: {Reason}", exception.Message);
        return CommandResults.Failure(exception.Message);
    }
    catch (IOException exception)
    {
        context.Logger.LogError(exception, "Could not read the uploaded quotes file");
        return CommandResults.Failure("Could not read the uploaded file. Select it again and retry.");
    }
}, commandOptions: new CommandOptions
{
    Description = "Append quotes from a JSON file using the dashboard file picker.",
    IsHighlighted = true,
    Visibility = ResourceCommandVisibility.UI,
    UpdateState = CommandState
});

// Vite + React dev server. AddViteApp runs `npm run dev` and injects the API
// endpoint via service discovery so vite.config.ts can proxy /api to it.
var web = builder.AddViteApp("web", "./frontend")
    .WithReference(api)
    .WaitFor(api);

// For `aspire publish`, bake the built SPA into the API container under wwwroot.
api.PublishWithContainerFiles(web, "wwwroot");

builder.Build().Run();

static ResourceCommandState CommandState(UpdateCommandStateContext context) =>
    context.ResourceSnapshot.HealthStatus == HealthStatus.Healthy
        ? ResourceCommandState.Enabled
        : ResourceCommandState.Disabled;

async Task<ExecuteCommandResult> SendBatchAsync(
    byte[] payload, int expectedCount, ExecuteCommandContext context)
{
    using var client = context.Services.GetRequiredService<IHttpClientFactory>().CreateClient("quote-commands");
    using var content = new ByteArrayContent(payload);
    content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
    try
    {
        using var response = await client.PostAsync(
            $"{api.GetEndpoint("http").Url}/api/quotes/import", content, context.CancellationToken);
        if (!response.IsSuccessStatusCode)
        {
            context.Logger.LogError("Quotes API returned HTTP {Status}", (int)response.StatusCode);
            return CommandResults.Failure(
                $"Quotes API returned HTTP {(int)response.StatusCode}. Check the api resource logs and refresh the board before retrying.");
        }

        var result = await response.Content.ReadFromJsonAsync<QuoteImportResult>(context.CancellationToken);
        if (result?.Ids is null || result.Added != expectedCount || result.Ids.Length != expectedCount)
        {
            context.Logger.LogError("Quotes API returned an unexpected import result");
            return CommandResults.Failure("Unexpected import response. Refresh the board before retrying.");
        }

        context.Logger.LogInformation("Added {Count} quotes", result.Added);
        return CommandResults.Success(
            $"Added {result.Added} quotes. Refresh the Quotes Board.",
            JsonSerializer.Serialize(result, new JsonSerializerOptions(JsonSerializerDefaults.Web) { WriteIndented = true }),
            CommandResultFormat.Json,
            displayImmediately: true);
    }
    catch (HttpRequestException exception)
    {
        context.Logger.LogError(exception, "Could not reach the Quotes API");
        return CommandResults.Failure("Could not reach the Quotes API. Check its health and refresh the board before retrying.");
    }
    catch (JsonException exception)
    {
        context.Logger.LogError(exception, "Could not read the Quotes API response");
        return CommandResults.Failure("Could not read the import result. Refresh the board before retrying.");
    }
    catch (OperationCanceledException exception)
    {
        if (context.CancellationToken.IsCancellationRequested)
        {
            context.Logger.LogInformation("Quote command canceled");
            return CommandResults.Canceled();
        }

        context.Logger.LogError(exception, "Quotes API request timed out");
        return CommandResults.Failure("The Quotes API timed out. Refresh the board before retrying.");
    }
}
