package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"log/slog"
	"net/http"
	"net/url"
	"os"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/XSAM/otelsql"
	_ "github.com/lib/pq"
	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/contrib/instrumentation/runtime"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploggrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/log/global"
	"go.opentelemetry.io/otel/propagation"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	sdkresource "go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.40.0"
)

type Bookmark struct {
	ID        int    `json:"id"`
	URL       string `json:"url"`
	Title     string `json:"title"`
	Tags      string `json:"tags"`
	CreatedAt string `json:"createdAt"`
}

// Storage interface — implemented by both postgres and in-memory backends.
// All methods take a context.Context so otelsql can attach DB spans to the
// caller's request span (and so slog log records correlate with the trace).
type Storage interface {
	GetAll(ctx context.Context) ([]Bookmark, error)
	Create(ctx context.Context, b Bookmark) (Bookmark, error)
	Delete(ctx context.Context, id int) error
	Search(ctx context.Context, query string) ([]Bookmark, error)
}

// --- PostgreSQL backend ---

type pgStore struct {
	db *sql.DB
}

func newPgStore(connStr string) (*pgStore, error) {
	// otelsql.Open wraps lib/pq so every query emits a child span (with statement,
	// db.system, etc.) and feeds db.client.* metrics. Falls back transparently
	// when no tracer/meter is set, so the same path works in standalone mode.
	db, err := otelsql.Open("postgres", connStr,
		otelsql.WithAttributes(semconv.DBSystemNamePostgreSQL),
		otelsql.WithSpanOptions(otelsql.SpanOptions{OmitConnResetSession: true}),
	)
	if err != nil {
		return nil, err
	}
	if _, err := otelsql.RegisterDBStatsMetrics(db, otelsql.WithAttributes(semconv.DBSystemNamePostgreSQL)); err != nil {
		slog.Warn("failed to register otelsql db stats metrics", "error", err)
	}
	if err := db.Ping(); err != nil {
		return nil, err
	}

	_, err = db.ExecContext(context.Background(), `CREATE TABLE IF NOT EXISTS bookmarks (
		id SERIAL PRIMARY KEY,
		url TEXT NOT NULL,
		title TEXT NOT NULL,
		tags TEXT NOT NULL DEFAULT '',
		created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
	)`)
	if err != nil {
		return nil, err
	}

	// Seed if table is empty
	var count int
	db.QueryRowContext(context.Background(), "SELECT COUNT(*) FROM bookmarks").Scan(&count)
	if count == 0 {
		seeds := []Bookmark{
			{URL: "https://dotnet.microsoft.com/apps/aspire", Title: "Aspire", Tags: "dotnet,aspire,cloud"},
			{URL: "https://svelte.dev", Title: "Svelte - Cybernetically enhanced web apps", Tags: "javascript,svelte,frontend"},
			{URL: "https://go.dev", Title: "The Go Programming Language", Tags: "go,golang,backend"},
		}
		for _, s := range seeds {
			db.ExecContext(context.Background(), "INSERT INTO bookmarks (url, title, tags) VALUES ($1, $2, $3)", s.URL, s.Title, s.Tags)
		}
	}

	slog.Info("Connected to PostgreSQL")
	return &pgStore{db: db}, nil
}

func (s *pgStore) GetAll(ctx context.Context) ([]Bookmark, error) {
	rows, err := s.db.QueryContext(ctx, "SELECT id, url, title, tags, created_at FROM bookmarks ORDER BY id")
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	return scanBookmarks(rows)
}

func (s *pgStore) Create(ctx context.Context, b Bookmark) (Bookmark, error) {
	err := s.db.QueryRowContext(ctx,
		"INSERT INTO bookmarks (url, title, tags) VALUES ($1, $2, $3) RETURNING id, created_at",
		b.URL, b.Title, b.Tags,
	).Scan(&b.ID, &b.CreatedAt)
	return b, err
}

func (s *pgStore) Delete(ctx context.Context, id int) error {
	res, err := s.db.ExecContext(ctx, "DELETE FROM bookmarks WHERE id = $1", id)
	if err != nil {
		return err
	}
	n, _ := res.RowsAffected()
	if n == 0 {
		return sql.ErrNoRows
	}
	return nil
}

func (s *pgStore) Search(ctx context.Context, query string) ([]Bookmark, error) {
	q := "%" + strings.ToLower(query) + "%"
	rows, err := s.db.QueryContext(ctx,
		"SELECT id, url, title, tags, created_at FROM bookmarks WHERE LOWER(title) LIKE $1 OR LOWER(tags) LIKE $1 OR LOWER(url) LIKE $1 ORDER BY id",
		q,
	)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	return scanBookmarks(rows)
}

func scanBookmarks(rows *sql.Rows) ([]Bookmark, error) {
	var bookmarks []Bookmark
	for rows.Next() {
		var b Bookmark
		var t time.Time
		if err := rows.Scan(&b.ID, &b.URL, &b.Title, &b.Tags, &t); err != nil {
			return nil, err
		}
		b.CreatedAt = t.Format(time.RFC3339)
		bookmarks = append(bookmarks, b)
	}
	if bookmarks == nil {
		bookmarks = []Bookmark{}
	}
	return bookmarks, rows.Err()
}

// --- In-memory fallback backend ---

type memStore struct {
	mu        sync.RWMutex
	bookmarks []Bookmark
	nextID    int
}

func newMemStore() *memStore {
	s := &memStore{nextID: 1}
	seeds := []Bookmark{
		{URL: "https://dotnet.microsoft.com/apps/aspire", Title: "Aspire", Tags: "dotnet,aspire,cloud"},
		{URL: "https://svelte.dev", Title: "Svelte - Cybernetically enhanced web apps", Tags: "javascript,svelte,frontend"},
		{URL: "https://go.dev", Title: "The Go Programming Language", Tags: "go,golang,backend"},
	}
	for _, seed := range seeds {
		seed.ID = s.nextID
		seed.CreatedAt = time.Now().Format(time.RFC3339)
		s.bookmarks = append(s.bookmarks, seed)
		s.nextID++
	}
	slog.Info("Using in-memory storage (no database connection string found)")
	return s
}

func (s *memStore) GetAll(_ context.Context) ([]Bookmark, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()
	result := make([]Bookmark, len(s.bookmarks))
	copy(result, s.bookmarks)
	return result, nil
}

func (s *memStore) Create(_ context.Context, b Bookmark) (Bookmark, error) {
	s.mu.Lock()
	defer s.mu.Unlock()
	b.ID = s.nextID
	b.CreatedAt = time.Now().Format(time.RFC3339)
	s.bookmarks = append(s.bookmarks, b)
	s.nextID++
	return b, nil
}

func (s *memStore) Delete(_ context.Context, id int) error {
	s.mu.Lock()
	defer s.mu.Unlock()
	for i, bm := range s.bookmarks {
		if bm.ID == id {
			s.bookmarks = append(s.bookmarks[:i], s.bookmarks[i+1:]...)
			return nil
		}
	}
	return sql.ErrNoRows
}

func (s *memStore) Search(_ context.Context, query string) ([]Bookmark, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()
	q := strings.ToLower(query)
	var results []Bookmark
	for _, bm := range s.bookmarks {
		if strings.Contains(strings.ToLower(bm.Title), q) ||
			strings.Contains(strings.ToLower(bm.Tags), q) ||
			strings.Contains(strings.ToLower(bm.URL), q) {
			results = append(results, bm)
		}
	}
	if results == nil {
		results = []Bookmark{}
	}
	return results, nil
}

// --- HTTP handlers ---

var storage Storage

func enableCORS(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type, traceparent, tracestate, baggage")
}

func handleBookmarks(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	ctx := r.Context()
	switch r.Method {
	case "GET":
		bookmarks, err := storage.GetAll(ctx)
		if err != nil {
			slog.ErrorContext(ctx, "list bookmarks failed", "error", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		slog.InfoContext(ctx, "listed bookmarks", "count", len(bookmarks))
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(bookmarks)

	case "POST":
		var b Bookmark
		if err := json.NewDecoder(r.Body).Decode(&b); err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		created, err := storage.Create(ctx, b)
		if err != nil {
			slog.ErrorContext(ctx, "create bookmark failed", "error", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		slog.InfoContext(ctx, "created bookmark", "id", created.ID, "url", created.URL)
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusCreated)
		json.NewEncoder(w).Encode(created)

	default:
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

func handleBookmarkByID(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	if strings.HasPrefix(r.URL.Path, "/api/bookmarks/search") {
		handleSearch(w, r)
		return
	}

	if r.Method != "DELETE" {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	ctx := r.Context()
	path := strings.TrimPrefix(r.URL.Path, "/api/bookmarks/")
	id, err := strconv.Atoi(path)
	if err != nil {
		http.Error(w, "Invalid ID", http.StatusBadRequest)
		return
	}

	if err := storage.Delete(ctx, id); err != nil {
		if err == sql.ErrNoRows {
			slog.WarnContext(ctx, "bookmark not found", "id", id)
			http.Error(w, "Bookmark not found", http.StatusNotFound)
		} else {
			slog.ErrorContext(ctx, "delete bookmark failed", "id", id, "error", err)
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
		return
	}
	slog.InfoContext(ctx, "deleted bookmark", "id", id)
	w.WriteHeader(http.StatusNoContent)
}

func handleSearch(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	ctx := r.Context()
	query := r.URL.Query().Get("q")
	if query == "" {
		bookmarks, err := storage.GetAll(ctx)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(bookmarks)
		return
	}

	results, err := storage.Search(ctx, query)
	if err != nil {
		slog.ErrorContext(ctx, "search failed", "q", query, "error", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	slog.InfoContext(ctx, "searched bookmarks", "q", query, "results", len(results))
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(results)
}

func health(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"status": "healthy"})
}

func initOtel(ctx context.Context) (func(), error) {
	endpoint := os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT")
	if endpoint == "" {
		return func() {}, nil
	}

	// Build a Resource shared by all three signals so the dashboard groups
	// traces, metrics, and logs under the same service. resource.Default()
	// already merges OTEL_SERVICE_NAME and OTEL_RESOURCE_ATTRIBUTES from env;
	// we overlay an explicit service.namespace and use the SAME schema URL
	// the SDK uses internally (semconv v1.40.0) to avoid "conflicting Schema URL"
	// merge errors at startup.
	res, err := sdkresource.Merge(
		sdkresource.Default(),
		sdkresource.NewWithAttributes(
			semconv.SchemaURL,
			semconv.ServiceName(getServiceName()),
			attribute.String("service.namespace", "svelte-go-bookmarks"),
		),
	)
	if err != nil {
		return nil, err
	}

	// Trace exporter
	traceExporter, err := otlptracegrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	tracerProvider := sdktrace.NewTracerProvider(
		sdktrace.WithBatcher(traceExporter),
		sdktrace.WithResource(res),
	)
	otel.SetTracerProvider(tracerProvider)

	// Metric exporter
	metricExporter, err := otlpmetricgrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	meterProvider := sdkmetric.NewMeterProvider(
		sdkmetric.WithReader(sdkmetric.NewPeriodicReader(metricExporter)),
		sdkmetric.WithResource(res),
	)
	otel.SetMeterProvider(meterProvider)

	// Log exporter
	logExporter, err := otlploggrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	loggerProvider := sdklog.NewLoggerProvider(
		sdklog.WithProcessor(sdklog.NewBatchProcessor(logExporter)),
		sdklog.WithResource(res),
	)
	global.SetLoggerProvider(loggerProvider)

	// Propagator — required for traceparent/tracestate over HTTP so the Svelte
	// browser SDK can stitch its spans onto the Go server spans (and vice-versa).
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(
		propagation.TraceContext{},
		propagation.Baggage{},
	))

	// Bridge slog → OTel so structured logs appear in the Aspire dashboard.
	// The bridge reads span context from slog Records emitted via *Context()
	// methods, which is why every handler uses slog.InfoContext(r.Context(), ...).
	slogHandler := otelslog.NewHandler(getServiceName(), otelslog.WithLoggerProvider(loggerProvider))
	slog.SetDefault(slog.New(slogHandler))

	// Go runtime metrics: GC count, goroutines, heap allocations, etc.
	if err := runtime.Start(runtime.WithMinimumReadMemStatsInterval(time.Second)); err != nil {
		slog.Warn("failed to start runtime metrics", "error", err)
	}

	shutdown := func() {
		// Use a fresh context so a cancelled root ctx does not abort flush.
		// 5s is enough for the BSP/BLRP schedule delay (Aspire sets to 1s).
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		_ = tracerProvider.Shutdown(shutdownCtx)
		_ = meterProvider.Shutdown(shutdownCtx)
		_ = loggerProvider.Shutdown(shutdownCtx)
	}

	slog.Info("OpenTelemetry initialized", "endpoint", endpoint, "service", getServiceName())
	return shutdown, nil
}

func getServiceName() string {
	if name := os.Getenv("OTEL_SERVICE_NAME"); name != "" {
		return name
	}
	return "go-bookmarks-api"
}

// buildPgConnString assembles a lib/pq DSN from the PG_* env vars Aspire
// injects (since plain AddContainer for Postgres doesn't auto-wire a
// CONNECTIONSTRINGS__ value). Aspire's WithEnvironmentEndpoint emits a URL
// like "http://pg.dev.internal:5432" — we strip the scheme back out.
func buildPgConnString() string {
	if cs := os.Getenv("CONNECTIONSTRINGS__bookmarksdb"); cs != "" {
		return cs
	}
	host := os.Getenv("PG_HOST")
	user := os.Getenv("PG_USER")
	pass := os.Getenv("PG_PASSWORD")
	dbname := os.Getenv("PG_DB")
	if host == "" || user == "" || dbname == "" {
		return ""
	}
	if u, err := url.Parse(host); err == nil && u.Host != "" {
		host = u.Host
	}
	return fmt.Sprintf("host=%s user=%s password=%s dbname=%s sslmode=disable",
		strings.SplitN(host, ":", 2)[0],
		user, pass, dbname,
	) + " port=" + portFromHost(host)
}

func portFromHost(host string) string {
	if i := strings.LastIndex(host, ":"); i >= 0 && i < len(host)-1 {
		return host[i+1:]
	}
	return "5432"
}

func main() {
	ctx := context.Background()
	shutdown, err := initOtel(ctx)
	if err != nil {
		slog.Warn("Failed to initialize OpenTelemetry", "error", err)
	} else {
		defer shutdown()
	}

	// Try PostgreSQL first, fall back to in-memory.
	connStr := buildPgConnString()
	if connStr != "" {
		pg, err := newPgStore(connStr)
		if err != nil {
			slog.Warn("PostgreSQL connection failed, falling back to in-memory", "error", err)
			storage = newMemStore()
		} else {
			storage = pg
		}
	} else {
		storage = newMemStore()
	}

	mux := http.NewServeMux()
	mux.HandleFunc("/api/bookmarks", handleBookmarks)
	mux.HandleFunc("/api/bookmarks/", handleBookmarkByID)
	mux.HandleFunc("/api/bookmarks/search", handleSearch)
	mux.HandleFunc("/health", health)

	// Wrap the whole mux once. otelhttp.WithSpanNameFormatter gives each span
	// a stable name like "GET /api/bookmarks" instead of the full URL, so the
	// Aspire dashboard "Routes" view groups requests properly.
	handler := otelhttp.NewHandler(mux, "http.server",
		otelhttp.WithSpanNameFormatter(func(_ string, r *http.Request) string {
			return r.Method + " " + r.URL.Path
		}),
	)

	slog.Info("Go Bookmarks API listening", "port", 8080)
	if err := http.ListenAndServe(":8080", handler); err != nil {
		log.Fatal(err)
	}
}
