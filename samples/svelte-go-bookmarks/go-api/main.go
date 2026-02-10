package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"strings"
	"sync"
	"time"
)

type Bookmark struct {
	ID        int      `json:"id"`
	URL       string   `json:"url"`
	Title     string   `json:"title"`
	Tags      string   `json:"tags"`
	CreatedAt string   `json:"createdAt"`
}

type BookmarkStore struct {
	mu        sync.RWMutex
	bookmarks []Bookmark
	nextID    int
}

var store = &BookmarkStore{
	nextID: 1,
}

func init() {
	// Seed initial bookmarks
	store.bookmarks = []Bookmark{
		{
			ID:        store.nextID,
			URL:       "https://dotnet.microsoft.com/apps/aspire",
			Title:     ".NET Aspire",
			Tags:      "dotnet,aspire,cloud",
			CreatedAt: time.Now().Format(time.RFC3339),
		},
	}
	store.nextID++
	store.bookmarks = append(store.bookmarks, Bookmark{
		ID:        store.nextID,
		URL:       "https://svelte.dev",
		Title:     "Svelte - Cybernetically enhanced web apps",
		Tags:      "javascript,svelte,frontend",
		CreatedAt: time.Now().Format(time.RFC3339),
	})
	store.nextID++
	store.bookmarks = append(store.bookmarks, Bookmark{
		ID:        store.nextID,
		URL:       "https://go.dev",
		Title:     "The Go Programming Language",
		Tags:      "go,golang,backend",
		CreatedAt: time.Now().Format(time.RFC3339),
	})
	store.nextID++
}

func enableCORS(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
}

func getBookmarks(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	store.mu.RLock()
	defer store.mu.RUnlock()

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(store.bookmarks)
}

func createBookmark(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	if r.Method != "POST" {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var bookmark Bookmark
	if err := json.NewDecoder(r.Body).Decode(&bookmark); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	store.mu.Lock()
	bookmark.ID = store.nextID
	bookmark.CreatedAt = time.Now().Format(time.RFC3339)
	store.bookmarks = append(store.bookmarks, bookmark)
	store.nextID++
	store.mu.Unlock()

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(bookmark)
}

func deleteBookmark(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	if r.Method != "DELETE" {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract ID from path /api/bookmarks/{id}
	path := strings.TrimPrefix(r.URL.Path, "/api/bookmarks/")
	id, err := strconv.Atoi(path)
	if err != nil {
		http.Error(w, "Invalid ID", http.StatusBadRequest)
		return
	}

	store.mu.Lock()
	defer store.mu.Unlock()

	for i, bookmark := range store.bookmarks {
		if bookmark.ID == id {
			store.bookmarks = append(store.bookmarks[:i], store.bookmarks[i+1:]...)
			w.WriteHeader(http.StatusNoContent)
			return
		}
	}

	http.Error(w, "Bookmark not found", http.StatusNotFound)
}

func searchBookmarks(w http.ResponseWriter, r *http.Request) {
	enableCORS(w)
	if r.Method == "OPTIONS" {
		w.WriteHeader(http.StatusOK)
		return
	}

	query := strings.ToLower(r.URL.Query().Get("q"))
	if query == "" {
		getBookmarks(w, r)
		return
	}

	store.mu.RLock()
	defer store.mu.RUnlock()

	var results []Bookmark
	for _, bookmark := range store.bookmarks {
		if strings.Contains(strings.ToLower(bookmark.Title), query) ||
			strings.Contains(strings.ToLower(bookmark.Tags), query) ||
			strings.Contains(strings.ToLower(bookmark.URL), query) {
			results = append(results, bookmark)
		}
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(results)
}

func health(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"status": "healthy"})
}

func main() {
	http.HandleFunc("/api/bookmarks", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case "GET":
			getBookmarks(w, r)
		case "POST":
			createBookmark(w, r)
		case "OPTIONS":
			enableCORS(w)
			w.WriteHeader(http.StatusOK)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	http.HandleFunc("/api/bookmarks/", func(w http.ResponseWriter, r *http.Request) {
		if strings.HasPrefix(r.URL.Path, "/api/bookmarks/search") {
			searchBookmarks(w, r)
		} else {
			deleteBookmark(w, r)
		}
	})

	http.HandleFunc("/api/bookmarks/search", searchBookmarks)
	http.HandleFunc("/health", health)

	log.Println("Go Bookmarks API listening on :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
