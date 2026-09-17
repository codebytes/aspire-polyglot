# Flask Markdown Wiki

A simple Markdown-based wiki application built with Flask, SQLite, and Redis caching, orchestrated by Aspire.

## Features

- **Markdown Editing**: Write and edit wiki pages using Markdown syntax
- **SQLite Backend**: Lightweight database for storing wiki pages
- **Redis Cache**: Aspire-managed Redis caches rendered HTML for fast page loads
- **Simple UI**: Clean, responsive interface for viewing and editing pages
- **Page Management**: Create new pages, edit existing ones, and view all pages
- **Aspire Orchestration**: Managed by Aspire for easy deployment and monitoring
- **Graceful Fallback**: Works without Redis — cache is optional

## Architecture

```
┌─────────────────────────────────────────┐
│         Aspire AppHost                  │
│   (Orchestration & Service Discovery)   │
└────────────┬──────────────┬─────────────┘
             │              │
             │ Python App   │ Container
             ▼              ▼
┌──────────────────┐  ┌───────────┐
│   Flask Wiki     │  │   Redis   │
│  ┌────────────┐  │  │  (cache)  │
│  │  Waitress  │  │  └───────────┘
│  │     ↓      │  │        ▲
│  │   Views  ──┼──┼────────┘
│  │     ↓      │  │  cached HTML
│  │  SQLite    │  │
│  └────────────┘  │
└──────────────────┘
```

## Project Structure

```
flask-markdown-wiki/
├── apphost.py              # Preview Python AppHost
├── aspire.config.json     # SDK version and preview feature flags
├── .aspire/modules/       # CLI-generated Python hosting bindings
├── src/
│   ├── main.py             # Flask application
│   ├── Dockerfile          # Containerized Flask workload
│   └── requirements.txt    # Python dependencies
└── README.md
```

## Prerequisites

- Aspire CLI 13.5.3 and .NET 10 SDK
- Python 3.12+ and [uv](https://docs.astral.sh/uv/)
- Docker (for Flask and Redis containers)

The Python AppHost is experimental; its feature flags are local to
`aspire.config.json`. Let the Aspire CLI restore `.aspire/modules/` rather than
editing generated bindings.

## Running the Application

### With Aspire (Recommended)

1. From the sample root directory, run:
   ```bash
   aspire run --apphost apphost.py
   ```

2. Open the Aspire dashboard (URL will be displayed in the terminal)

3. Access the wiki application through the Aspire dashboard or directly at the assigned port

For worktrees use `aspire start --apphost apphost.py --isolated`. Stop this sample
with `aspire stop --apphost apphost.py`.

### Standalone (Development)

1. Navigate to the src directory:
   ```bash
   cd src
   ```

2. Install Python dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Run the Flask application:
   ```bash
   python main.py
   ```

4. Open your browser to `http://localhost:8080`

## Usage

### Viewing Pages

- Navigate to `/` to see a list of all wiki pages
- Click on any page title to view its content (rendered from Markdown)

### Creating Pages

1. Click "New Page" in the navigation
2. Enter a page title
3. Write content in Markdown format
4. Click "Create Page"

### Editing Pages

1. Navigate to any page
2. Click the "Edit" button
3. Modify the Markdown content
4. Click "Save" to update

### Health Check

- The application provides a health check endpoint at `/health`
- Returns JSON with service status

## Database

The application uses SQLite with a simple schema:

```sql
CREATE TABLE pages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    slug TEXT UNIQUE NOT NULL,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

The database is automatically initialized on startup with a default "Home" page.
In the Aspire run, `wiki.db` lives in the Flask container. This demo does not mount
a persistent SQLite volume, so recreating the container resets its pages.
Creating or editing a page invalidates cached HTML for its slug, including cache
entries that survived an earlier Flask container.

## Dependencies

- **Flask**: Web framework
- **markdown**: Markdown to HTML conversion
- **waitress**: Production-ready WSGI server
- **redis**: Redis client for caching rendered pages

## Configuration

The application reads the following environment variables:

- `PORT`: HTTP port to listen on (default: 8080)
- `ConnectionStrings__cache`: Redis connection string (injected by Aspire)

When orchestrated by Aspire, these are automatically configured. Without Aspire, the app runs without caching.

## Markdown Support

The wiki supports standard Markdown syntax including:

- Headers (`#`, `##`, `###`, etc.)
- Bold (`**text**`) and italic (`*text*`)
- Lists (ordered and unordered)
- Links (`[text](url)`)
- Code blocks (` ```language ` and inline backticks)
- Blockquotes (`>`)
- And more!

## Development

To modify the application:

1. Edit `src/main.py` for application logic
2. HTML templates are defined inline using `render_template_string`
3. Styling is embedded in the base template
4. Database is automatically managed (SQLite file: `wiki.db`)

Run the SQLite/cache regression tests with `python -m unittest -v test_wiki`
from `src/` after installing its requirements.

## License

This is a sample application for demonstrating Aspire orchestration with Python applications.
