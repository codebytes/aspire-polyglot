package main

import (
	"log"

	"apphost/modules/aspire"
)

func main() {
	builder, err := aspire.CreateBuilder(nil)
	if err != nil {
		log.Fatalf("Failed to create builder: %v", err)
	}

	// PostgreSQL container
	pg := builder.AddContainer("pg", "postgres:16")
	pg.WithEnvironment("POSTGRES_USER", "postgres")
	pg.WithEnvironment("POSTGRES_PASSWORD", "postgres")
	pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")
	pgPort := 5432.0
	pg.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
		TargetPort: &pgPort,
		Name:       aspire.StringPtr("tcp"),
	})

	// Go API via Dockerfile
	api := builder.AddDockerfile("api", "./go-api", nil, nil)
	api.WithOtlpExporter()
	// Manually wire connection string since plain containers don't support WithReference
	pgEndpoint := pg.GetEndpoint("tcp")
	api.WithEnvironment("PG_HOST", pgEndpoint)
	api.WithEnvironment("PG_USER", "postgres")
	api.WithEnvironment("PG_PASSWORD", "postgres")
	api.WithEnvironment("PG_DB", "bookmarksdb")
	apiPort := 8080.0
	api.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
		TargetPort: &apiPort,
		Name:       aspire.StringPtr("http"),
	})
	api.WithExternalHttpEndpoints()
	// Ensure API waits for Postgres to be healthy before starting — env var wiring
	// alone does NOT order startup in Aspire 13.4.6 polyglot. Without this WaitFor,
	// on a cold start (uncached postgres:16 image), the API can start first, fail
	// its Postgres Ping, and silently fall back to the in-memory store.
	api.WaitFor(pg)

	// Svelte frontend via npm
	frontend := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})
	apiEndpoint := api.GetEndpoint("http")
	frontend.WithEnvironment("services__api__http__0", apiEndpoint)
	// Vite dev server defaults to 5173. We bind targetPort=5173 explicitly so
	// Aspire knows where to proxy traffic, and inject env="PORT" so vite.config.js
	// can also pull it from process.env.PORT (matching vite-react-api's pattern).
	frontendPort := 5173.0
	frontend.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
		TargetPort: &frontendPort,
		Name:       aspire.StringPtr("http"),
		Env:        aspire.StringPtr("PORT"),
	})
	frontend.WithExternalHttpEndpoints()
	// WaitFor establishes both startup ordering and a Reference relationship
	// visible on the dashboard. (Plain ContainerResources like api can't be
	// used with WithReference — they don't expose a connection string.)
	frontend.WaitFor(api)

	app, err := builder.Build()
	if err != nil {
		log.Fatalf("Failed to build: %v", err)
	}

	if err := app.Run(nil); err != nil {
		log.Fatalf("Failed to run: %v", err)
	}
}
