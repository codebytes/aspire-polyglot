// Go-authored AppHost (preview): Svelte frontend + Go API + PostgreSQL.
// The same first-party integrations a C# AppHost uses are generated into
// .aspire/modules by `aspire restore`.
package main

import (
	"log"

	"apphost/modules/aspire"
)

func main() {
	builder, err := aspire.CreateBuilder()
	if err != nil {
		log.Fatalf("create builder: %v", err)
	}

	// PostgreSQL container plus a database. Referencing it injects BOOKMARKSDB_URI.
	db := builder.AddPostgres("pg").AddDatabase("bookmarksdb")

	// The Go API runs natively with `go run .` (Aspire.Hosting.Go).
	api := builder.AddGoApp("api", "./go-api").
		WithReference(db).
		WaitFor(db).
		WithHttpEndpoint(&aspire.WithHttpEndpointOptions{Env: aspire.StringPtr("PORT")}).
		WithHttpHealthCheck(&aspire.WithHttpHealthCheckOptions{Path: aspire.StringPtr("/health")}).
		WithExternalHttpEndpoints()

	// Vite dev server for the Svelte SPA; it proxies /api to the referenced API.
	builder.AddViteApp("frontend", "./frontend").
		WithReference(api).
		WaitFor(api)

	app, err := builder.Build()
	if err != nil {
		log.Fatalf("build: %v", err)
	}
	if err := app.Run(); err != nil {
		log.Fatalf("run: %v", err)
	}
}
