// Bookmarks API backed by PostgreSQL.
//
// Aspire runs this natively with `go run .` (AddGoApp) and injects:
//   - PORT             the port to listen on
//   - BOOKMARKSDB_URI  postgresql://user:password@host:port/bookmarksdb
//   - OTEL_*           where to send traces, metrics, and logs
package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"syscall"
	"time"

	"github.com/XSAM/otelsql"
	_ "github.com/jackc/pgx/v5/stdlib"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	semconv "go.opentelemetry.io/otel/semconv/v1.40.0"
)

type Bookmark struct {
	ID        int       `json:"id"`
	URL       string    `json:"url"`
	Title     string    `json:"title"`
	Tags      string    `json:"tags"`
	CreatedAt time.Time `json:"createdAt"`
}

const columns = "id, url, title, tags, created_at"

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	shutdownTelemetry, err := setupTelemetry(ctx)
	if err != nil {
		slog.Error("telemetry setup failed", "error", err)
		os.Exit(1)
	}
	defer func() { _ = shutdownTelemetry(context.Background()) }()

	// otelsql wraps the pgx driver so every query is a child span of its HTTP request.
	db, err := otelsql.Open("pgx", os.Getenv("BOOKMARKSDB_URI"),
		otelsql.WithAttributes(semconv.DBSystemNamePostgreSQL))
	if err != nil {
		slog.Error("open database failed", "error", err)
		os.Exit(1)
	}
	defer db.Close()
	if err := migrate(ctx, db); err != nil {
		slog.Error("database setup failed", "error", err)
		os.Exit(1)
	}

	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", func(w http.ResponseWriter, r *http.Request) {
		if err := db.PingContext(r.Context()); err != nil {
			http.Error(w, "database unavailable", http.StatusServiceUnavailable)
			return
		}
		w.Write([]byte("Healthy"))
	})

	mux.HandleFunc("GET /api/bookmarks", func(w http.ResponseWriter, r *http.Request) {
		list(w, r, db, "SELECT "+columns+" FROM bookmarks ORDER BY id")
	})

	mux.HandleFunc("GET /api/bookmarks/search", func(w http.ResponseWriter, r *http.Request) {
		q := r.URL.Query().Get("q")
		slog.InfoContext(r.Context(), "searching bookmarks", "q", q)
		list(w, r, db, "SELECT "+columns+` FROM bookmarks
			WHERE title ILIKE '%' || $1 || '%' OR tags ILIKE '%' || $1 || '%' OR url ILIKE '%' || $1 || '%'
			ORDER BY id`, q)
	})

	mux.HandleFunc("POST /api/bookmarks", func(w http.ResponseWriter, r *http.Request) {
		var b Bookmark
		if err := json.NewDecoder(r.Body).Decode(&b); err != nil || b.URL == "" || b.Title == "" {
			http.Error(w, "url and title are required", http.StatusBadRequest)
			return
		}
		err := db.QueryRowContext(r.Context(),
			"INSERT INTO bookmarks (url, title, tags) VALUES ($1, $2, $3) RETURNING id, created_at",
			b.URL, b.Title, b.Tags).Scan(&b.ID, &b.CreatedAt)
		if err != nil {
			fail(w, r, "create bookmark failed", err)
			return
		}
		slog.InfoContext(r.Context(), "created bookmark", "id", b.ID, "url", b.URL)
		writeJSON(w, http.StatusCreated, b)
	})

	mux.HandleFunc("DELETE /api/bookmarks/{id}", func(w http.ResponseWriter, r *http.Request) {
		id, err := strconv.Atoi(r.PathValue("id"))
		if err != nil {
			http.Error(w, "invalid id", http.StatusBadRequest)
			return
		}
		result, err := db.ExecContext(r.Context(), "DELETE FROM bookmarks WHERE id = $1", id)
		if err != nil {
			fail(w, r, "delete bookmark failed", err)
			return
		}
		if n, _ := result.RowsAffected(); n == 0 {
			slog.WarnContext(r.Context(), "bookmark not found", "id", id)
			http.NotFound(w, r)
			return
		}
		slog.InfoContext(r.Context(), "deleted bookmark", "id", id)
		w.WriteHeader(http.StatusNoContent)
	})

	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}
	server := &http.Server{
		Addr: ":" + port,
		// Name spans by route ("GET /api/bookmarks/{id}") so the dashboard groups them.
		Handler: otelhttp.NewHandler(mux, "bookmarks-api", otelhttp.WithSpanNameFormatter(
			func(_ string, r *http.Request) string { return r.Method + " " + r.Pattern })),
		ReadHeaderTimeout: 5 * time.Second,
	}
	go func() {
		slog.Info("bookmarks API listening", "port", port)
		if err := server.ListenAndServe(); !errors.Is(err, http.ErrServerClosed) {
			slog.Error("server stopped", "error", err)
			stop()
		}
	}()

	<-ctx.Done()
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	_ = server.Shutdown(shutdownCtx)
}

// migrate creates the table and seeds a few bookmarks on first run.
func migrate(ctx context.Context, db *sql.DB) error {
	_, err := db.ExecContext(ctx, `CREATE TABLE IF NOT EXISTS bookmarks (
		id         SERIAL PRIMARY KEY,
		url        TEXT NOT NULL,
		title      TEXT NOT NULL,
		tags       TEXT NOT NULL DEFAULT '',
		created_at TIMESTAMPTZ NOT NULL DEFAULT NOW())`)
	if err != nil {
		return err
	}
	result, err := db.ExecContext(ctx, `INSERT INTO bookmarks (url, title, tags)
		SELECT * FROM (VALUES
			('https://aspire.dev', 'Aspire', 'dotnet,aspire,cloud'),
			('https://svelte.dev', 'Svelte', 'javascript,svelte,frontend'),
			('https://go.dev', 'The Go Programming Language', 'go,golang,backend')) AS seed
		WHERE NOT EXISTS (SELECT 1 FROM bookmarks)`)
	if err != nil {
		return err
	}
	seeded, _ := result.RowsAffected()
	slog.InfoContext(ctx, "connected to PostgreSQL", "seeded", seeded)
	return nil
}

func list(w http.ResponseWriter, r *http.Request, db *sql.DB, query string, args ...any) {
	rows, err := db.QueryContext(r.Context(), query, args...)
	if err != nil {
		fail(w, r, "query bookmarks failed", err)
		return
	}
	defer rows.Close()
	bookmarks := []Bookmark{}
	for rows.Next() {
		var b Bookmark
		if err := rows.Scan(&b.ID, &b.URL, &b.Title, &b.Tags, &b.CreatedAt); err != nil {
			fail(w, r, "read bookmarks failed", err)
			return
		}
		bookmarks = append(bookmarks, b)
	}
	if err := rows.Err(); err != nil {
		fail(w, r, "read bookmarks failed", err)
		return
	}
	writeJSON(w, http.StatusOK, bookmarks)
}

func fail(w http.ResponseWriter, r *http.Request, message string, err error) {
	slog.ErrorContext(r.Context(), message, "error", err)
	http.Error(w, message, http.StatusInternalServerError)
}

func writeJSON(w http.ResponseWriter, status int, value any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(value)
}
