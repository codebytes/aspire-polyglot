package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/redis/go-redis/v9"
)

func main() {
	port := envOr("PORT", "8080")
	redisHost := envOr("REDIS_HOST", "localhost")
	redisPort := envOr("REDIS_PORT", "6379")

	rdb := redis.NewClient(&redis.Options{
		Addr: fmt.Sprintf("%s:%s", redisHost, redisPort),
	})
	ctx := context.Background()

	http.HandleFunc("/api/health", func(w http.ResponseWriter, _ *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintln(w, `{"status":"healthy"}`)
	})

	// Every request bumps a counter in Redis and returns the running total.
	http.HandleFunc("/api/hits", func(w http.ResponseWriter, _ *http.Request) {
		n, err := rdb.Incr(ctx, "hits").Result()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			log.Printf("redis error: %v", err)
			return
		}
		w.Header().Set("Content-Type", "application/json")
		fmt.Fprintf(w, `{"hits":%d}`, n)
	})

	log.Printf("hit-counter API listening on :%s (redis %s:%s)", port, redisHost, redisPort)
	log.Fatal(http.ListenAndServe(":"+port, nil))
}

func envOr(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
