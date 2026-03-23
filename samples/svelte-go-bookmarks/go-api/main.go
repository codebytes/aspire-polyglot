package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"
	"sync"
	"time"

	_ "github.com/lib/pq"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploggrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/log/global"
	"go.opentelemetry.io/otel/propagation"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
)

type Bookmark struct {
	ID        int    `json:"id"`
	URL       string `json:"url"`
	Title     string `json:"title"`
	Tags      string `json:"tags"`
	CreatedAt string `json:"createdAt"`
}

// Storage interface — implemented by both postgres and in-memory backends
type Storage interface {
	GetAll() ([]Bookmark, error)
	Create(b Bookmark) (Bookmark, error)
	Delete(id int) error
	Search(query string) ([]Bookmark, error)
}

// --- PostgreSQL backend ---

type pgStore struct {
	db *sql.DB
}

func newPgStore(connStr string) (*pgStore, error) {
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}
	if err := db.Ping(); err != nil {
		return nil, err
	}

	_, err = db.Exec(`CREATE TABLE IF NOT EXISTS bookmarks (
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
	db.QueryRow("SELECT COUNT(*) FROM bookmarks").Scan(&count)
	if count == 0 {
		seeds := []Bookmark{
			{URL: "https://dotnet.microsoft.com/apps/aspire", Title: "Aspire", Tags: "dotnet,aspire,cloud"},
			{URL: "https://svelte.dev", Title: "Svelte - Cybernetically enhanced web apps", Tags: "javascript,svelte,frontend"},
			{URL: "https://go.dev", Title: "The Go Programming Language", Tags: "go,golang,backend"},
		}
		for _, s := range seeds {
			db.Exec("INSERT INTO bookmarks (url, title, tags) VALUES ($1, $2, $3)", s.URL, s.Title, s.Tags)
		}
	}

	log.Println("Connected to PostgreSQL")
	return &pgStore{db: db}, nil
}

func (s *pgStore) GetAll() ([]Bookmark, error) {
	rows, err := s.db.Query("SELECT id, url, title, tags, created_at FROM bookmarks ORDER BY id")
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	return scanBookmarks(rows)
}

func (s *pgStore) Create(b Bookmark) (Bookmark, error) {
	err := s.db.QueryRow(
		"INSERT INTO bookmarks (url, title, tags) VALUES ($1, $2, $3) RETURNING id, created_at",
		b.URL, b.Title, b.Tags,
	).Scan(&b.ID, &b.CreatedAt)
	return b, err
}

func (s *pgStore) Delete(id int) error {
	res, err := s.db.Exec("DELETE FROM bookmarks WHERE id = $1", id)
	if err != nil {
		return err
	}
	n, _ := res.RowsAffected()
	if n == 0 {
		return sql.ErrNoRows
	}
	return nil
}

func (s *pgStore) Search(query string) ([]Bookmark, error) {
	q := "%" + strings.ToLower(query) + "%"
	rows, err := s.db.Query(
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
	log.Println("Using in-memory storage (no database connection string found)")
	return s
}

func (s *memStore) GetAll() ([]Bookmark, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()
	result := make([]Bookmark, len(s.bookmarks))
	copy(result, s.bookmarks)
	return result, nil
}

func (s *memStore) Create(b Bookmark) (Bookmark, error) {
	s.mu.Lock()
	defer s.mu.Unlock()
	b.ID = s.nextID
	b.CreatedAt = time.Now().Format(time.RFC3339)
	s.bookmarks = append(s.bookmarks, b)
	s.nextID++
	return b, nil
}

func (s *memStore) Delete(id int) error {
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

func (s *memStore) Search(query string) ([]Bookmark, error) {
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
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
}

func handleBookmarks(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	switch r.Method {
	case "GET":
		bookmarks, err := storage.GetAll()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(bookmarks)

	case "POST":
		var b Bookmark
		if err := json.NewDecoder(r.Body).Decode(&b); err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		created, err := storage.Create(b)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
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

	path := strings.TrimPrefix(r.URL.Path, "/api/bookmarks/")
	id, err := strconv.Atoi(path)
	if err != nil {
		http.Error(w, "Invalid ID", http.StatusBadRequest)
		return
	}

	if err := storage.Delete(id); err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "Bookmark not found", http.StatusNotFound)
		} else {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
		return
	}
	w.WriteHeader(http.StatusNoContent)
}

func handleSearch(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	query := r.URL.Query().Get("q")
	if query == "" {
		bookmarks, err := storage.GetAll()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(bookmarks)
		return
	}

	results, err := storage.Search(query)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
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

	// Trace exporter
	traceExporter, err := otlptracegrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	tracerProvider := sdktrace.NewTracerProvider(
		sdktrace.WithBatcher(traceExporter),
	)
	otel.SetTracerProvider(tracerProvider)

	// Metric exporter
	metricExporter, err := otlpmetricgrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	meterProvider := sdkmetric.NewMeterProvider(
		sdkmetric.WithReader(sdkmetric.NewPeriodicReader(metricExporter)),
	)
	otel.SetMeterProvider(meterProvider)

	// Log exporter
	logExporter, err := otlploggrpc.New(ctx)
	if err != nil {
		return nil, err
	}

	loggerProvider := sdklog.NewLoggerProvider(
		sdklog.WithProcessor(sdklog.NewBatchProcessor(logExporter)),
	)
	global.SetLoggerProvider(loggerProvider)

	// Propagator
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(
		propagation.TraceContext{},
		propagation.Baggage{},
	))

	shutdown := func() {
		ctx := context.Background()
		tracerProvider.Shutdown(ctx)
		meterProvider.Shutdown(ctx)
		loggerProvider.Shutdown(ctx)
	}

	log.Printf("OpenTelemetry initialized, exporting to %s", endpoint)
	return shutdown, nil
}

func main() {
	ctx := context.Background()
	shutdown, err := initOtel(ctx)
	if err != nil {
		log.Printf("Warning: failed to initialize OpenTelemetry: %v", err)
	} else {
		defer shutdown()
	}

	// Try PostgreSQL first, fall back to in-memory
	connStr := os.Getenv("CONNECTIONSTRINGS__bookmarksdb")
	if connStr != "" {
		pg, err := newPgStore(connStr)
		if err != nil {
			log.Printf("PostgreSQL connection failed (%v), falling back to in-memory", err)
			storage = newMemStore()
		} else {
			storage = pg
		}
	} else {
		storage = newMemStore()
	}

	http.Handle("/api/bookmarks", otelhttp.NewHandler(http.HandlerFunc(handleBookmarks), "handleBookmarks"))
	http.Handle("/api/bookmarks/", otelhttp.NewHandler(http.HandlerFunc(handleBookmarkByID), "handleBookmarkByID"))
	http.Handle("/api/bookmarks/search", otelhttp.NewHandler(http.HandlerFunc(handleSearch), "handleSearch"))
	http.Handle("/health", otelhttp.NewHandler(http.HandlerFunc(health), "health"))

	log.Println("Go Bookmarks API listening on :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
