using Npgsql;

namespace Api;

public static class QuoteStore
{
    private const string InsertSql =
        "INSERT INTO quotes (text, author) VALUES (@text, @author) RETURNING id, text, author, created_at;";

    public static async Task<Quote> CreateAsync(
        NpgsqlDataSource db, NewQuote quote, CancellationToken cancellationToken)
    {
        await using var command = db.CreateCommand(InsertSql);
        return await InsertAsync(command, quote, cancellationToken);
    }

    public static async Task<QuoteImportResult> ImportAsync(
        NpgsqlDataSource db, IReadOnlyList<NewQuote> quotes, CancellationToken cancellationToken)
    {
        await using var connection = await db.OpenConnectionAsync(cancellationToken);
        await using var transaction = await connection.BeginTransactionAsync(cancellationToken);
        var ids = new int[quotes.Count];
        for (var index = 0; index < quotes.Count; index++)
        {
            await using var command = new NpgsqlCommand(InsertSql, connection, transaction);
            var created = await InsertAsync(command, quotes[index], cancellationToken);
            ids[index] = created.Id;
        }

        await transaction.CommitAsync(cancellationToken);
        return new QuoteImportResult(ids.Length, ids);
    }

    private static async Task<Quote> InsertAsync(
        NpgsqlCommand command, NewQuote quote, CancellationToken cancellationToken)
    {
        command.Parameters.AddWithValue("text", quote.Text.Trim());
        command.Parameters.AddWithValue("author", quote.Author.Trim());
        await using var reader = await command.ExecuteReaderAsync(cancellationToken);
        if (!await reader.ReadAsync(cancellationToken))
        {
            throw new InvalidOperationException("PostgreSQL did not return the inserted quote.");
        }

        return new Quote(
            reader.GetInt32(0),
            reader.GetString(1),
            reader.GetString(2),
            reader.GetFieldValue<DateTimeOffset>(3));
    }
}
