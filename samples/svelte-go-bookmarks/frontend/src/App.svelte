<script>
  import { onMount } from 'svelte';

  let bookmarks = [];
  let searchQuery = '';
  let newBookmark = {
    url: '',
    title: '',
    tags: ''
  };
  let loading = false;
  let error = '';

  onMount(() => {
    fetchBookmarks();
  });

  async function fetchBookmarks() {
    try {
      loading = true;
      const response = await fetch('/api/bookmarks');
      if (!response.ok) throw new Error('Failed to fetch bookmarks');
      bookmarks = await response.json();
      error = '';
    } catch (e) {
      error = e.message;
    } finally {
      loading = false;
    }
  }

  async function addBookmark() {
    if (!newBookmark.url || !newBookmark.title) {
      error = 'URL and Title are required';
      return;
    }

    try {
      loading = true;
      const response = await fetch('/api/bookmarks', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newBookmark)
      });
      if (!response.ok) throw new Error('Failed to add bookmark');
      
      newBookmark = { url: '', title: '', tags: '' };
      await fetchBookmarks();
      error = '';
    } catch (e) {
      error = e.message;
    } finally {
      loading = false;
    }
  }

  async function deleteBookmark(id) {
    try {
      loading = true;
      const response = await fetch(`/api/bookmarks/${id}`, {
        method: 'DELETE'
      });
      if (!response.ok) throw new Error('Failed to delete bookmark');
      
      await fetchBookmarks();
      error = '';
    } catch (e) {
      error = e.message;
    } finally {
      loading = false;
    }
  }

  async function searchBookmarks() {
    if (!searchQuery.trim()) {
      await fetchBookmarks();
      return;
    }

    try {
      loading = true;
      const response = await fetch(`/api/bookmarks/search?q=${encodeURIComponent(searchQuery)}`);
      if (!response.ok) throw new Error('Failed to search bookmarks');
      bookmarks = await response.json();
      error = '';
    } catch (e) {
      error = e.message;
    } finally {
      loading = false;
    }
  }

  $: if (searchQuery !== undefined) {
    const timer = setTimeout(() => searchBookmarks(), 300);
    return () => clearTimeout(timer);
  }
</script>

<main>
  <div class="container">
    <header>
      <h1>📚 Bookmark Manager</h1>
      <p class="subtitle">Powered by .NET Aspire + Go + Svelte</p>
    </header>

    {#if error}
      <div class="error">{error}</div>
    {/if}

    <section class="add-section">
      <h2>Add New Bookmark</h2>
      <form on:submit|preventDefault={addBookmark}>
        <div class="form-group">
          <input
            type="url"
            placeholder="URL (e.g., https://example.com)"
            bind:value={newBookmark.url}
            required
          />
        </div>
        <div class="form-group">
          <input
            type="text"
            placeholder="Title"
            bind:value={newBookmark.title}
            required
          />
        </div>
        <div class="form-group">
          <input
            type="text"
            placeholder="Tags (comma-separated, e.g., tech,javascript,tutorial)"
            bind:value={newBookmark.tags}
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Adding...' : 'Add Bookmark'}
        </button>
      </form>
    </section>

    <section class="search-section">
      <input
        type="search"
        placeholder="🔍 Search bookmarks by title, tags, or URL..."
        bind:value={searchQuery}
      />
    </section>

    <section class="bookmarks-section">
      <h2>My Bookmarks ({bookmarks.length})</h2>
      {#if loading && bookmarks.length === 0}
        <p class="loading">Loading bookmarks...</p>
      {:else if bookmarks.length === 0}
        <p class="empty">No bookmarks found. Add your first one above!</p>
      {:else}
        <div class="bookmarks-grid">
          {#each bookmarks as bookmark (bookmark.id)}
            <div class="bookmark-card">
              <div class="bookmark-header">
                <h3>{bookmark.title}</h3>
                <button
                  class="delete-btn"
                  on:click={() => deleteBookmark(bookmark.id)}
                  disabled={loading}
                  title="Delete bookmark"
                >
                  ×
                </button>
              </div>
              <a href={bookmark.url} target="_blank" rel="noopener noreferrer" class="bookmark-url">
                {bookmark.url}
              </a>
              {#if bookmark.tags}
                <div class="tags">
                  {#each bookmark.tags.split(',') as tag}
                    <span class="tag">{tag.trim()}</span>
                  {/each}
                </div>
              {/if}
              <div class="bookmark-date">
                Added: {new Date(bookmark.createdAt).toLocaleDateString()}
              </div>
            </div>
          {/each}
        </div>
      {/if}
    </section>
  </div>
</main>

<style>
  :global(body) {
    margin: 0;
    padding: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
  }

  main {
    padding: 2rem;
  }

  .container {
    max-width: 1200px;
    margin: 0 auto;
  }

  header {
    text-align: center;
    color: white;
    margin-bottom: 3rem;
  }

  h1 {
    font-size: 3rem;
    margin: 0;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  }

  .subtitle {
    font-size: 1.1rem;
    opacity: 0.9;
    margin-top: 0.5rem;
  }

  .error {
    background: #ff4444;
    color: white;
    padding: 1rem;
    border-radius: 8px;
    margin-bottom: 1rem;
    text-align: center;
  }

  section {
    background: white;
    border-radius: 12px;
    padding: 2rem;
    margin-bottom: 2rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  h2 {
    margin-top: 0;
    color: #333;
    font-size: 1.5rem;
  }

  .form-group {
    margin-bottom: 1rem;
  }

  input[type="text"],
  input[type="url"],
  input[type="search"] {
    width: 100%;
    padding: 0.75rem;
    border: 2px solid #e0e0e0;
    border-radius: 8px;
    font-size: 1rem;
    box-sizing: border-box;
    transition: border-color 0.3s;
  }

  input:focus {
    outline: none;
    border-color: #667eea;
  }

  button {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 0.75rem 2rem;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
    transition: transform 0.2s, opacity 0.2s;
  }

  button:hover:not(:disabled) {
    transform: translateY(-2px);
  }

  button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  .search-section input {
    font-size: 1.1rem;
  }

  .bookmarks-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 1.5rem;
  }

  .bookmark-card {
    border: 2px solid #e0e0e0;
    border-radius: 8px;
    padding: 1.5rem;
    transition: transform 0.2s, box-shadow 0.2s;
  }

  .bookmark-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  }

  .bookmark-header {
    display: flex;
    justify-content: space-between;
    align-items: start;
    margin-bottom: 0.75rem;
  }

  .bookmark-card h3 {
    margin: 0;
    color: #333;
    font-size: 1.25rem;
    flex: 1;
  }

  .delete-btn {
    background: #ff4444;
    color: white;
    border: none;
    width: 32px;
    height: 32px;
    border-radius: 50%;
    font-size: 1.5rem;
    cursor: pointer;
    padding: 0;
    line-height: 1;
    flex-shrink: 0;
    margin-left: 0.5rem;
  }

  .delete-btn:hover:not(:disabled) {
    background: #cc0000;
  }

  .bookmark-url {
    color: #667eea;
    text-decoration: none;
    word-break: break-all;
    display: block;
    margin-bottom: 0.75rem;
    font-size: 0.9rem;
  }

  .bookmark-url:hover {
    text-decoration: underline;
  }

  .tags {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    margin-bottom: 0.75rem;
  }

  .tag {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 12px;
    font-size: 0.85rem;
  }

  .bookmark-date {
    color: #999;
    font-size: 0.85rem;
  }

  .loading,
  .empty {
    text-align: center;
    color: #999;
    padding: 2rem;
  }
</style>
