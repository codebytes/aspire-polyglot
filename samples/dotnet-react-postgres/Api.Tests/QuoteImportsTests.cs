using System.Text;
using System.Text.Json;

namespace Api.Tests;

public class QuoteImportsTests
{
    [Theory]
    [InlineData("1", 1)]
    [InlineData("5", 5)]
    [InlineData("100", 100)]
    public void SeedCountAcceptsWholeNumbersInRange(string value, int expected)
    {
        Assert.True(QuoteImports.TryParseCount(value, out var count));
        Assert.Equal(expected, count);
    }

    [Theory]
    [InlineData(null)]
    [InlineData("")]
    [InlineData("0")]
    [InlineData("-1")]
    [InlineData("101")]
    [InlineData("1.5")]
    [InlineData("not a number")]
    [InlineData("9999999999999999")]
    public void SeedCountRejectsInvalidValues(string? value)
    {
        Assert.False(QuoteImports.TryParseCount(value, out _));
    }

    [Theory]
    [InlineData(1)]
    [InlineData(5)]
    [InlineData(100)]
    public void SeedPayloadContainsExactlyTheRequestedQuotes(int count)
    {
        var quotes = QuoteImports.Parse(QuoteImports.CreateSeedPayload(count));
        Assert.Equal(count, quotes.Length);
        Assert.Equal(count, quotes.Select(quote => quote.Text).Distinct().Count());
        Assert.All(quotes, quote => Assert.Equal("Aspire demo", quote.Author));
    }

    [Fact]
    public void ParseTrimsValuesAndPreservesUnicode()
    {
        var quotes = QuoteImports.Parse(Encoding.UTF8.GetBytes(
            """[{"text":"  Caf\u00e9 is ready.  ","author":"  Demo  "}]"""));
        Assert.Equal(new NewQuote("Caf\u00e9 is ready.", "Demo"), Assert.Single(quotes));
    }

    [Theory]
    [InlineData("")]
    [InlineData("not json")]
    [InlineData("{}")]
    [InlineData("null")]
    [InlineData("[]")]
    [InlineData("[null]")]
    [InlineData("""[{"text":"Missing author"}]""")]
    [InlineData("""[{"text":" ","author":"Demo"}]""")]
    [InlineData("""[{"text":"Quote","author":null}]""")]
    [InlineData("""[{"text":42,"author":"Demo"}]""")]
    [InlineData("""[{"text":"Quote","author":"Demo","unexpected":true}]""")]
    public void ParseRejectsInvalidDocuments(string json)
    {
        Assert.Throws<InvalidDataException>(() => QuoteImports.Parse(Encoding.UTF8.GetBytes(json)));
    }

    [Fact]
    public void ParseRejectsTheWholeBatchWhenALaterRowIsInvalid()
    {
        var error = Assert.Throws<InvalidDataException>(() => QuoteImports.Parse(Encoding.UTF8.GetBytes(
            """[{"text":"Valid","author":"Demo"},{"text":"Invalid"}]""")));
        Assert.Contains("Quote 2", error.Message);
    }

    [Fact]
    public void ParseRejectsMoreThanOneHundredQuotes()
    {
        var quotes = Enumerable.Repeat(new NewQuote("Quote", "Demo"), QuoteImports.MaxQuotes + 1);
        Assert.Throws<InvalidDataException>(() => QuoteImports.Parse(JsonSerializer.SerializeToUtf8Bytes(quotes)));
    }

    [Fact]
    public void ParseEnforcesFieldLengthLimits()
    {
        var valid = new NewQuote(new string('t', QuoteImports.MaxTextLength), new string('a', QuoteImports.MaxAuthorLength));
        Assert.Single(QuoteImports.Parse(JsonSerializer.SerializeToUtf8Bytes(new[] { valid })));
        foreach (var invalid in new[]
        {
            valid with { Text = valid.Text + "t" },
            valid with { Author = valid.Author + "a" }
        })
        {
            Assert.Throws<InvalidDataException>(() => QuoteImports.Parse(JsonSerializer.SerializeToUtf8Bytes(new[] { invalid })));
        }
    }

    [Fact]
    public async Task ReadsAFileExactlyAtTheLimit()
    {
        var json = Encoding.UTF8.GetBytes("""[{"text":"Quote","author":"Demo"}]""");
        var payload = Enumerable.Repeat((byte)' ', QuoteImports.MaxFileBytes).ToArray();
        json.CopyTo(payload, 0);
        using var stream = new MemoryStream(payload);
        var read = await QuoteImports.ReadPayloadAsync(stream, CancellationToken.None);
        Assert.Equal(QuoteImports.MaxFileBytes, read.Length);
        Assert.Single(QuoteImports.Parse(read));
    }

    [Fact]
    public async Task RejectsAFileOneByteOverTheLimit()
    {
        using var stream = new MemoryStream(new byte[QuoteImports.MaxFileBytes + 1]);
        await Assert.ThrowsAsync<InvalidDataException>(() => QuoteImports.ReadPayloadAsync(stream, CancellationToken.None));
    }

    [Fact]
    public void AcceptsUtf8ByteOrderMark()
    {
        var json = Encoding.UTF8.GetBytes("""[{"text":"Quote","author":"Demo"}]""");
        var payload = Encoding.UTF8.GetPreamble().Concat(json).ToArray();
        Assert.Single(QuoteImports.Parse(payload));
    }

    [Fact]
    public async Task ReadingHonorsCancellation()
    {
        using var stream = new MemoryStream([1, 2, 3]);
        await Assert.ThrowsAnyAsync<OperationCanceledException>(
            () => QuoteImports.ReadPayloadAsync(stream, new CancellationToken(canceled: true)));
    }
}
