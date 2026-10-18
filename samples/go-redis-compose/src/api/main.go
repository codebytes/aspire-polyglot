package main

import (
	"context"
	"fmt"
	"log"
	"log/slog"
	"net/http"
	"os"

	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
)

func main() {
	if err := run(); err != nil {
		log.Fatal(err)
	}
}

func run() error {
	ctx := context.Background()
	shutdown, err := initTelemetry(ctx)
	if err != nil {
		return fmt.Errorf("initialize telemetry: %w", err)
	}
	defer func() {
		if err := shutdown(context.Background()); err != nil {
			log.Printf("flush telemetry: %v", err)
		}
	}()
	port := envOr("PORT", "8080")
	redisHost := envOr("REDIS_HOST", "localhost")
	redisPort := envOr("REDIS_PORT", "6379")

	rdb := redis.NewClient(&redis.Options{
		Addr: fmt.Sprintf("%s:%s", redisHost, redisPort),
	})
	defer rdb.Close()

	http.HandleFunc("/api/health", func(w http.ResponseWriter, _ *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintln(w, `{"status":"healthy"}`)
	})

	// Every request bumps a counter in Redis and returns the running total.
	http.HandleFunc("/api/hits", func(w http.ResponseWriter, r *http.Request) {
		n, err := rdb.Incr(r.Context(), "hits").Result()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			slog.ErrorContext(r.Context(), "Redis increment failed", "error", err)
			return
		}
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintf(w, `{"hits":%d}`, n)
		slog.InfoContext(r.Context(), "Hit counted", "hits", n)
	})

	slog.Info("Hit-counter API listening", "port", port, "redis", rdb.Options().Addr)
	return http.ListenAndServe(":"+port, otelhttp.NewHandler(http.DefaultServeMux, "hitcounter"))
}

func envOr(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
