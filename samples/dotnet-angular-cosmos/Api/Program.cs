using Api.Models;
using Microsoft.Azure.Cosmos;

var builder = WebApplication.CreateBuilder(args);

builder.AddServiceDefaults();

// Add Cosmos DB client using Aspire integration
builder.AddAzureCosmosClient("recipesdb");

// Add CORS for Angular frontend
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

var app = builder.Build();

app.UseCors();
app.MapDefaultEndpoints();

// Initialize Cosmos DB container and seed data
var cosmosClient = app.Services.GetRequiredService<CosmosClient>();
var database = await cosmosClient.CreateDatabaseIfNotExistsAsync("recipesdb");
var container = await database.Database.CreateContainerIfNotExistsAsync("recipes", "/id");

// Seed sample recipes if container is empty
var query = new QueryDefinition("SELECT VALUE COUNT(1) FROM c");
var iterator = container.Container.GetItemQueryIterator<int>(query);
var count = 0;
if (iterator.HasMoreResults)
{
    var response = await iterator.ReadNextAsync();
    count = response.FirstOrDefault();
}

if (count == 0)
{
    var sampleRecipes = new[]
    {
        new Recipe
        {
            Id = Guid.NewGuid().ToString(),
            Title = "Classic Spaghetti Carbonara",
            Description = "A traditional Italian pasta dish with eggs, cheese, and pancetta",
            Ingredients = new List<string>
            {
                "400g spaghetti",
                "200g pancetta or guanciale",
                "4 large eggs",
                "100g Pecorino Romano cheese",
                "Black pepper",
                "Salt"
            },
            Instructions = "1. Cook pasta according to package directions. 2. Fry pancetta until crispy. 3. Beat eggs with cheese. 4. Drain pasta, mix with pancetta, remove from heat. 5. Quickly stir in egg mixture. 6. Season with black pepper and serve.",
            CookTimeMinutes = 20,
            Category = "Italian"
        },
        new Recipe
        {
            Id = Guid.NewGuid().ToString(),
            Title = "Chocolate Chip Cookies",
            Description = "Chewy, delicious homemade cookies",
            Ingredients = new List<string>
            {
                "2 1/4 cups all-purpose flour",
                "1 tsp baking soda",
                "1 tsp salt",
                "1 cup butter, softened",
                "3/4 cup sugar",
                "3/4 cup brown sugar",
                "2 large eggs",
                "2 tsp vanilla extract",
                "2 cups chocolate chips"
            },
            Instructions = "1. Preheat oven to 375°F. 2. Mix flour, baking soda, and salt. 3. Beat butter and sugars until creamy. 4. Add eggs and vanilla. 5. Gradually blend in flour mixture. 6. Stir in chocolate chips. 7. Drop rounded tablespoons onto ungreased cookie sheets. 8. Bake 9-11 minutes.",
            CookTimeMinutes = 25,
            Category = "Dessert"
        },
        new Recipe
        {
            Id = Guid.NewGuid().ToString(),
            Title = "Thai Green Curry",
            Description = "Fragrant and spicy Thai curry with coconut milk",
            Ingredients = new List<string>
            {
                "2 tbsp green curry paste",
                "400ml coconut milk",
                "300g chicken breast, sliced",
                "1 cup bamboo shoots",
                "1 red bell pepper, sliced",
                "2 Thai eggplants",
                "2 tbsp fish sauce",
                "1 tbsp palm sugar",
                "Thai basil leaves",
                "Kaffir lime leaves"
            },
            Instructions = "1. Heat half the coconut milk in a wok. 2. Add curry paste and fry until fragrant. 3. Add chicken and cook until opaque. 4. Add remaining coconut milk and bring to simmer. 5. Add vegetables and cook until tender. 6. Season with fish sauce and sugar. 7. Add basil and lime leaves. 8. Serve with jasmine rice.",
            CookTimeMinutes = 30,
            Category = "Thai"
        }
    };

    foreach (var recipe in sampleRecipes)
    {
        await container.Container.CreateItemAsync(recipe, new PartitionKey(recipe.Id));
    }
}

// GET /api/recipes - List all recipes
app.MapGet("/api/recipes", async (CosmosClient client) =>
{
    var container = client.GetContainer("recipesdb", "recipes");
    var query = new QueryDefinition("SELECT * FROM c");
    var iterator = container.GetItemQueryIterator<Recipe>(query);
    var recipes = new List<Recipe>();

    while (iterator.HasMoreResults)
    {
        var response = await iterator.ReadNextAsync();
        recipes.AddRange(response);
    }

    return Results.Ok(recipes);
});

// GET /api/recipes/{id} - Get recipe by ID
app.MapGet("/api/recipes/{id}", async (string id, CosmosClient client) =>
{
    try
    {
        var container = client.GetContainer("recipesdb", "recipes");
        var response = await container.ReadItemAsync<Recipe>(id, new PartitionKey(id));
        return Results.Ok(response.Resource);
    }
    catch (CosmosException ex) when (ex.StatusCode == System.Net.HttpStatusCode.NotFound)
    {
        return Results.NotFound();
    }
});

// POST /api/recipes - Create recipe
app.MapPost("/api/recipes", async (Recipe recipe, CosmosClient client) =>
{
    var container = client.GetContainer("recipesdb", "recipes");
    var newRecipe = recipe with { Id = Guid.NewGuid().ToString() };
    var response = await container.CreateItemAsync(newRecipe, new PartitionKey(newRecipe.Id));
    return Results.Created($"/api/recipes/{newRecipe.Id}", response.Resource);
});

// PUT /api/recipes/{id} - Update recipe
app.MapPut("/api/recipes/{id}", async (string id, Recipe recipe, CosmosClient client) =>
{
    try
    {
        var container = client.GetContainer("recipesdb", "recipes");
        var updatedRecipe = recipe with { Id = id };
        var response = await container.ReplaceItemAsync(updatedRecipe, id, new PartitionKey(id));
        return Results.Ok(response.Resource);
    }
    catch (CosmosException ex) when (ex.StatusCode == System.Net.HttpStatusCode.NotFound)
    {
        return Results.NotFound();
    }
});

// DELETE /api/recipes/{id} - Delete recipe
app.MapDelete("/api/recipes/{id}", async (string id, CosmosClient client) =>
{
    try
    {
        var container = client.GetContainer("recipesdb", "recipes");
        await container.DeleteItemAsync<Recipe>(id, new PartitionKey(id));
        return Results.NoContent();
    }
    catch (CosmosException ex) when (ex.StatusCode == System.Net.HttpStatusCode.NotFound)
    {
        return Results.NotFound();
    }
});

// GET /api/recipes/search?q= - Search recipes
app.MapGet("/api/recipes/search", async (string? q, CosmosClient client) =>
{
    if (string.IsNullOrWhiteSpace(q))
    {
        return Results.Ok(new List<Recipe>());
    }

    var container = client.GetContainer("recipesdb", "recipes");
    var query = new QueryDefinition(
        "SELECT * FROM c WHERE CONTAINS(LOWER(c.Title), @searchTerm) OR ARRAY_CONTAINS(c.Ingredients, @searchTerm, true)")
        .WithParameter("@searchTerm", q.ToLower());
    
    var iterator = container.GetItemQueryIterator<Recipe>(query);
    var recipes = new List<Recipe>();

    while (iterator.HasMoreResults)
    {
        var response = await iterator.ReadNextAsync();
        recipes.AddRange(response);
    }

    return Results.Ok(recipes);
});

// GET /health - Health check
app.MapGet("/health", () => Results.Ok(new { status = "healthy" }));

app.Run();
