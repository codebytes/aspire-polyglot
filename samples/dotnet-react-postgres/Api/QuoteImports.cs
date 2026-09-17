using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace Api;

public static class QuoteImports
{
    public const int MaxQuotes = 100;
    public const int MaxFileBytes = 128 * 1024;
    public const int MaxTextLength = 1000;
    public const int MaxAuthorLength = 200;
    public const string CountError = "Count must be a whole number from 1 to 100.";

    private static readonly JsonSerializerOptions JsonOptions = new(JsonSerializerDefaults.Web)
    {
        UnmappedMemberHandling = JsonUnmappedMemberHandling.Disallow
    };

    public static bool TryParseCount(string? value, out int count) =>
        int.TryParse(value, NumberStyles.Integer, CultureInfo.InvariantCulture, out count)
        && count is >= 1 and <= MaxQuotes;

    public static byte[] CreateSeedPayload(int count)
    {
        ArgumentOutOfRangeException.ThrowIfLessThan(count, 1);
        ArgumentOutOfRangeException.ThrowIfGreaterThan(count, MaxQuotes);
        var batch = Guid.NewGuid().ToString("N")[..8];
        var quotes = Enumerable.Range(1, count)
            .Select(index => new NewQuote($"Demo quote {index} from seed batch {batch}.", "Aspire demo"))
            .ToArray();
        return JsonSerializer.SerializeToUtf8Bytes(quotes, JsonOptions);
    }

    public static async Task<byte[]> ReadPayloadAsync(Stream stream, CancellationToken cancellationToken)
    {
        // Read one extra byte to enforce the limit even on non-seekable request streams.
        var buffer = new byte[MaxFileBytes + 1];
        var length = await stream.ReadAtLeastAsync(
            buffer, buffer.Length, throwOnEndOfStream: false, cancellationToken);
        if (length > MaxFileBytes)
        {
            throw new InvalidDataException("The JSON file must not exceed 128 KiB.");
        }

        return buffer[..length];
    }

    public static NewQuote[] Parse(ReadOnlySpan<byte> payload)
    {
        if (payload.Length > MaxFileBytes)
        {
            throw new InvalidDataException("The JSON file must not exceed 128 KiB.");
        }

        if (payload.StartsWith("\uFEFF"u8))
        {
            payload = payload[3..];
        }

        NewQuote?[]? quotes;
        try
        {
            quotes = JsonSerializer.Deserialize<NewQuote?[]>(payload, JsonOptions);
        }
        catch (JsonException exception)
        {
            throw new InvalidDataException(
                "Use a JSON array of objects containing only string fields \"text\" and \"author\".",
                exception);
        }

        if (quotes is null || quotes.Length is < 1 or > MaxQuotes)
        {
            throw new InvalidDataException("Import between 1 and 100 quotes.");
        }

        var normalized = new NewQuote[quotes.Length];
        for (var index = 0; index < quotes.Length; index++)
        {
            var quote = quotes[index];
            if (quote is null || string.IsNullOrWhiteSpace(quote.Text) || string.IsNullOrWhiteSpace(quote.Author))
            {
                throw new InvalidDataException($"Quote {index + 1} must have non-empty text and author.");
            }

            var text = quote.Text.Trim();
            var author = quote.Author.Trim();
            if (text.Length > MaxTextLength || author.Length > MaxAuthorLength)
            {
                throw new InvalidDataException(
                    $"Quote {index + 1} exceeds the limit of {MaxTextLength} text characters or {MaxAuthorLength} author characters.");
            }

            normalized[index] = new NewQuote(text, author);
        }

        return normalized;
    }
}

public record QuoteImportResult(int Added, int[] Ids);
