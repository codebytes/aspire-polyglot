// Hit-counter API: every request increments a Redis counter.
//
// Aspire runs this natively with `go run .` (AddGoApp) and injects:
//   - PORT       the port to listen on
//   - CACHE_URI  redis[s]://:password@host:port for the "cache" resource
//   - OTEL_*     where to send traces, metrics, and logs
package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/redis/go-redis/extra/redisotel/v9"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	shutdownTelemetry, err := setupTelemetry(ctx)
	if err != nil {
		slog.Error("telemetry setup failed", "error", err)
		os.Exit(1)
	}
	defer func() { _ = shutdownTelemetry(context.Background()) }()

	options, err := redis.ParseURL(envOr("CACHE_URI", "redis://localhost:6379"))
	if err != nil {
		slog.Error("invalid CACHE_URI", "error", err)
		os.Exit(1)
	}
	cache := redis.NewClient(options)
	defer cache.Close()
	// Redis commands become child spans of the HTTP request in the dashboard.
	if err := errors.Join(redisotel.InstrumentTracing(cache), redisotel.InstrumentMetrics(cache)); err != nil {
		slog.Warn("redis instrumentation failed", "error", err)
	}

	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", func(w http.ResponseWriter, r *http.Request) {
		if err := cache.Ping(r.Context()).Err(); err != nil {
			http.Error(w, "redis unavailable", http.StatusServiceUnavailable)
			return
		}
		fmt.Fprint(w, "Healthy")
	})
	mux.HandleFunc("GET /api/hits", func(w http.ResponseWriter, r *http.Request) {
		hits, err := cache.Incr(r.Context(), "hits").Result()
		if err != nil {
			slog.ErrorContext(r.Context(), "redis increment failed", "error", err)
			http.Error(w, "redis unavailable", http.StatusServiceUnavailable)
			return
		}
		slog.InfoContext(r.Context(), "hit counted", "hits", hits)
		w.Header().Set("Content-Type", "application/json")
		_ = json.NewEncoder(w).Encode(map[string]int64{"hits": hits})
	})
	mux.HandleFunc("GET /{$}", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		fmt.Fprint(w, page)
	})

	port := envOr("PORT", "8080")
	server := &http.Server{
		Addr:              ":" + port,
		Handler:           otelhttp.NewHandler(mux, "hitcounter"),
		ReadHeaderTimeout: 5 * time.Second,
	}
	go func() {
		slog.Info("hit-counter API listening", "port", port, "redis", options.Addr)
		if err := server.ListenAndServe(); !errors.Is(err, http.ErrServerClosed) {
			slog.Error("server stopped", "error", err)
			stop()
		}
	}()

	<-ctx.Done()
	slog.Info("shutting down")
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	_ = server.Shutdown(shutdownCtx)
}

func envOr(key, fallback string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return fallback
}

const page = `<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Go Hit Counter</title>
<style>body{font-family:system-ui,sans-serif;background:#12141c;color:#e6e6e6;display:grid;place-items:center;min-height:100vh;margin:0}
main{text-align:center}h1{font-size:4rem;margin:.2em 0;color:#00add8}button{font-size:1rem;padding:.6em 1.2em;border:0;border-radius:8px;background:#00add8;color:#0b0d14;cursor:pointer}</style>
</head>
<body><main>
<p>Go &middot; Redis &mdash; run natively by a C# AppHost</p>
<h1 id="hits">&hellip;</h1>
<button id="hit">Count a hit</button>
</main>
<script>
const show = async () => { const r = await fetch('/api/hits'); document.getElementById('hits').textContent = (await r.json()).hits; };
document.getElementById('hit').addEventListener('click', show);
show();
</script>
</body>
</html>`
