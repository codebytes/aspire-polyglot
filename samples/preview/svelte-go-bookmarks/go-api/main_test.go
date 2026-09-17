package main

import (
	"testing"

	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/sdk/resource"
)

func TestTelemetryResourcePreservesSDKSchema(t *testing.T) {
	t.Setenv("OTEL_SERVICE_NAME", "bookmark-test")
	r, err := telemetryResource()
	if err != nil {
		t.Fatalf("merging application attributes with SDK resource: %v", err)
	}
	if r.SchemaURL() != resource.Default().SchemaURL() {
		t.Fatalf("schema = %q, want SDK schema %q", r.SchemaURL(), resource.Default().SchemaURL())
	}
	attributes := r.Set()
	for key, expected := range map[attribute.Key]string{
		"service.name":      "bookmark-test",
		"service.namespace": "svelte-go-bookmarks",
	} {
		actual, ok := attributes.Value(key)
		if !ok || actual.AsString() != expected {
			t.Errorf("%s = %v, want %s", key, actual, expected)
		}
	}
}
