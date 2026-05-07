package main

import (
	"log"

	"apphost/modules/aspire"
)

func float64Ptr(v float64) *float64 { return &v }
func stringPtr(v string) *string    { return &v }

func main() {
	builder, err := aspire.CreateBuilder(nil)
	if err != nil {
		log.Fatalf("Failed to create builder: %v", err)
	}

	// PostgreSQL container
	pg, err := builder.AddContainer("pg", "postgres:16")
	if err != nil {
		log.Fatalf("Failed to add postgres: %v", err)
	}
	pg.WithEnvironment("POSTGRES_USER", "postgres")
	pg.WithEnvironment("POSTGRES_PASSWORD", "postgres")
	pg.WithEnvironment("POSTGRES_DB", "bookmarksdb")
	pg.WithHttpEndpoint(nil, float64Ptr(5432), stringPtr("tcp"), stringPtr("tcp"), nil)

	// Go API via Dockerfile
	api, err := builder.AddDockerfile("api", "./go-api", nil, nil)
	if err != nil {
		log.Fatalf("Failed to add api: %v", err)
	}
	_, err = api.WithOtlpExporter()
	if err != nil {
		log.Fatalf("Failed to configure OTLP exporter for api: %v", err)
	}
	// Manually wire connection string since plain containers don't support WithReference
	pgEndpoint, err := pg.GetEndpoint("tcp")
	if err != nil {
		log.Fatalf("Failed to get pg endpoint: %v", err)
	}
	api.WithEnvironmentEndpoint("PG_HOST", pgEndpoint)
	api.WithEnvironment("PG_USER", "postgres")
	api.WithEnvironment("PG_PASSWORD", "postgres")
	api.WithEnvironment("PG_DB", "bookmarksdb")
	api.WithHttpEndpoint(nil, float64Ptr(8080), stringPtr("http"), nil, nil)
	api.WithExternalHttpEndpoints()

	// Svelte frontend via npm
	frontend, err := builder.AddExecutable("frontend", "npm", "./frontend", []string{"run", "dev"})
	if err != nil {
		log.Fatalf("Failed to add frontend: %v", err)
	}
	apiEndpoint, err := api.GetEndpoint("http")
	if err != nil {
		log.Fatalf("Failed to get api endpoint: %v", err)
	}
	frontend.WithEnvironmentEndpoint("services__api__http__0", apiEndpoint)
	// Browsers can't speak gRPC OTLP, so emit HTTP/protobuf and let
	// vite.config.js re-export OTEL_* as VITE_OTEL_* for the SPA.
	if _, err := frontend.WithOtlpExporter(); err != nil {
		log.Fatalf("Failed to configure OTLP exporter for frontend: %v", err)
	}
	if _, err := frontend.WithOtlpExporterProtocol(aspire.OtlpProtocolHttpProtobuf); err != nil {
		log.Fatalf("Failed to set OTLP protocol for frontend: %v", err)
	}
	// name="http", env="PORT": Aspire allocates a port and injects it into PORT
	// so vite.config.js can bind to it. (Previous arg order put "PORT" in the
	// endpoint name slot, leaving Vite on its default port and the dashboard
	// URL unreachable.)
	frontend.WithHttpEndpoint(nil, nil, stringPtr("http"), stringPtr("PORT"), nil)
	frontend.WithExternalHttpEndpoints()
	if _, err := frontend.WaitFor(aspire.NewIResource(api.Handle(), api.Client())); err != nil {
		log.Fatalf("Failed to wait for api: %v", err)
	}

	app, err := builder.Build()
	if err != nil {
		log.Fatalf("Failed to build: %v", err)
	}

	if err := app.Run(nil); err != nil {
		log.Fatalf("Failed to run: %v", err)
	}
}
