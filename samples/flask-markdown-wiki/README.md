# Flask Markdown Wiki

A simple Markdown-based wiki application built with Flask and SQLite, orchestrated by .NET Aspire.

## Features

- **Markdown Editing**: Write and edit wiki pages using Markdown syntax
- **SQLite Backend**: Lightweight database for storing wiki pages
- **Simple UI**: Clean, responsive interface for viewing and editing pages
- **Page Management**: Create new pages, edit existing ones, and view all pages
- **Aspire Orchestration**: Managed by .NET Aspire for easy deployment and monitoring

## Project Structure

```
flask-markdown-wiki/
├── AppHost/
│   ├── Program.cs          # Aspire AppHost configuration
│   └── AppHost.csproj      # AppHost project file
├── src/
│   ├── main.py             # Flask application
│   └── requirements.txt    # Python dependencies
└── README.md
```

## Prerequisites

- .NET 9.0 SDK
- Python 3.8+
- .NET Aspire workload

## Running the Application

### With .NET Aspire (Recommended)

1. Navigate to the AppHost directory:
   ```bash
   cd AppHost
   ```

2. Run the Aspire AppHost:
   ```bash
   dotnet run
   ```

3. Open the Aspire dashboard (URL will be displayed in the terminal)

4. Access the wiki application through the Aspire dashboard or directly at the assigned port

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

## Dependencies

- **Flask**: Web framework
- **markdown**: Markdown to HTML conversion
- **waitress**: Production-ready WSGI server

## Configuration

The application reads the following environment variables:

- `PORT`: HTTP port to listen on (default: 8080)

When orchestrated by Aspire, these are automatically configured.

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

## License

This is a sample application for demonstrating .NET Aspire orchestration with Python applications.
