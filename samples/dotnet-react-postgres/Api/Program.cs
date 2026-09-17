using Api;
using Npgsql;

var builder = WebApplication.CreateBuilder(args);

// OpenTelemetry, health checks, and service discovery via the shared ServiceDefaults project.
builder.AddServiceDefaults();

// Aspire Npgsql client integration. The connection name "quotesdb" matches the
// database resource declared in apphost.cs; Aspire injects the connection string
// as ConnectionStrings__quotesdb and this call also wires up OTel + health checks.
builder.AddNpgsqlDataSource("quotesdb");

// The Vite dev server runs on a different origin during development.
builder.Services.AddCors(options =>
    options.AddDefaultPolicy(p => p.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader()));

var app = builder.Build();

app.UseCors();
app.MapDefaultEndpoints();

// Ensure the schema exists and seed a couple of rows on first run.
await using (var scope = app.Services.CreateAsyncScope())
{
    var db = scope.ServiceProvider.GetRequiredService<NpgsqlDataSource>();

    await using (var create = db.CreateCommand(
        """
        CREATE TABLE IF NOT EXISTS quotes (
            id         SERIAL PRIMARY KEY,
            text       TEXT NOT NULL,
            author     TEXT NOT NULL,
            created_at TIMESTAMPTZ NOT NULL DEFAULT now()
        );
        """))
    {
        await create.ExecuteNonQueryAsync();
    }

    await using var count = db.CreateCommand("SELECT COUNT(*) FROM quotes;");
    if (Convert.ToInt64(await count.ExecuteScalarAsync()) == 0)
    {
        await using var seed = db.CreateCommand(
            "INSERT INTO quotes (text, author) VALUES (@t1, @a1), (@t2, @a2);");
        seed.Parameters.AddWithValue("t1", "Simple things should be simple, complex things should be possible.");
        seed.Parameters.AddWithValue("a1", "Alan Kay");
        seed.Parameters.AddWithValue("t2", "Programs must be written for people to read.");
        seed.Parameters.AddWithValue("a2", "Harold Abelson");
        await seed.ExecuteNonQueryAsync();
    }
}

app.MapGet("/api/quotes", async (NpgsqlDataSource db) =>
{
    var quotes = new List<Quote>();
    await using var cmd = db.CreateCommand(
        "SELECT id, text, author, created_at FROM quotes ORDER BY created_at DESC, id DESC;");
    await using var reader = await cmd.ExecuteReaderAsync();
    while (await reader.ReadAsync())
    {
        quotes.Add(new Quote(
            reader.GetInt32(0),
            reader.GetString(1),
            reader.GetString(2),
            reader.GetFieldValue<DateTimeOffset>(3)));
    }
    return Results.Ok(quotes);
});

app.MapPost("/api/quotes", async (NpgsqlDataSource db, NewQuote input, CancellationToken cancellationToken) =>
{
    if (string.IsNullOrWhiteSpace(input.Text) || string.IsNullOrWhiteSpace(input.Author))
    {
        return Results.BadRequest(new { error = "text and author are required" });
    }

    var created = await QuoteStore.CreateAsync(db, input, cancellationToken);
    return Results.Created($"/api/quotes/{created.Id}", created);
});

// The batch endpoint supports local resource-command demos, not production administration.
if (app.Environment.IsDevelopment())
{
    app.MapPost("/api/quotes/import", async (
        HttpRequest request, NpgsqlDataSource db, ILogger<Program> logger,
        CancellationToken cancellationToken) =>
    {
        if (!request.HasJsonContentType())
        {
            logger.LogWarning("Quote import rejected: Content-Type must be application/json");
            return Results.BadRequest(new { error = "Content-Type must be application/json." });
        }

        NewQuote[] quotes;
        try
        {
            var payload = await QuoteImports.ReadPayloadAsync(request.Body, cancellationToken);
            quotes = QuoteImports.Parse(payload);
        }
        catch (InvalidDataException exception)
        {
            logger.LogWarning("Quote import rejected: {Reason}", exception.Message);
            return Results.BadRequest(new { error = exception.Message });
        }

        var result = await QuoteStore.ImportAsync(db, quotes, cancellationToken);
        logger.LogInformation("Imported {Count} quotes in one transaction", result.Added);
        return Results.Ok(result);
    });
}

app.Run();
