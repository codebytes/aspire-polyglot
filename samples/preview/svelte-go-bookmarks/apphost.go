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
	pg.WithEndpoint(&aspire.WithEndpointOptions{
		TargetPort: &pgPort,
		Name:       aspire.StringPtr("tcp"),
		Scheme:     aspire.StringPtr("tcp"),
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
	// Raw container resources provide startup ordering, not database health checks.
	api.WaitFor(pg)

	installer := builder.AddExecutable("frontend-install", "npm", "./frontend", []string{"ci", "--no-audit", "--no-fund"})
	frontend := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})
	frontend.WaitForCompletion(installer)
	protocol := aspire.OtlpProtocolHttpProtobuf
	frontend.WithOtlpExporter(&aspire.WithOtlpExporterOptions{Protocol: &protocol})
	apiEndpoint := api.GetEndpoint("http")
	frontend.WithEnvironment("services__api__http__0", apiEndpoint)
	// Aspire assigns the Vite port and supplies it through PORT.
	frontend.WithHttpEndpoint(&aspire.WithHttpEndpointOptions{
		Name: aspire.StringPtr("http"),
		Env:  aspire.StringPtr("PORT"),
	})
	frontend.WithExternalHttpEndpoints()
	frontend.WaitFor(api)

	app, err := builder.Build()
	if err != nil {
		log.Fatalf("Failed to build: %v", err)
	}

	if err := app.Run(nil); err != nil {
		log.Fatalf("Failed to run: %v", err)
	}
}
