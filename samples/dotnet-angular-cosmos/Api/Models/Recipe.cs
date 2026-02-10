namespace Api.Models;

public record Recipe
{
    public string Id { get; init; } = Guid.NewGuid().ToString();
    public string Title { get; init; } = "";
    public string Description { get; init; } = "";
    public List<string> Ingredients { get; init; } = [];
    public string Instructions { get; init; } = "";
    public int CookTimeMinutes { get; init; }
    public string Category { get; init; } = "Other";
}
