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

	api, err := builder.AddDockerfile("api", "./go-api")
	if err != nil {
		log.Fatalf("Failed to add api: %v", err)
	}
	api.WithHttpEndpoint(8080, "http")
	api.WithExternalHttpEndpoints()

	frontend, err := builder.AddNpmApp("frontend", "./frontend", "dev")
	if err != nil {
		log.Fatalf("Failed to add frontend: %v", err)
	}
	frontend.WithEnvironment("services__api__http__0", api.GetEndpoint("http"))
	frontend.WithHttpEndpoint(0, "PORT")
	frontend.WithExternalHttpEndpoints()

	app, err := builder.Build()
	if err != nil {
		log.Fatalf("Failed to build: %v", err)
	}

	if err := app.Run(nil); err != nil {
		log.Fatalf("Failed to run: %v", err)
	}
}
