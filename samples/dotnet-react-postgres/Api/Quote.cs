namespace Api;

public record Quote(int Id, string Text, string Author, DateTimeOffset CreatedAt);

public record NewQuote(string Text, string Author);
