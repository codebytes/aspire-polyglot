#!/usr/bin/env python3
import os

# --- OpenTelemetry setup (must run before Flask app creation) ---
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry import trace
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.instrumentation.flask import FlaskInstrumentor
    from opentelemetry.instrumentation.requests import RequestsInstrumentor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    import logging

    resource = Resource.create({
        "service.name": os.environ.get("OTEL_SERVICE_NAME", "flask-wiki"),
    })

    # Traces
    trace_provider = TracerProvider(resource=resource)
    trace_provider.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))
    trace.set_tracer_provider(trace_provider)

    # Metrics
    metric_reader = PeriodicExportingMetricReader(OTLPMetricExporter())
    meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])

    # Logs
    logger_provider = LoggerProvider(resource=resource)
    logger_provider.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter()))
    handler = LoggingHandler(logger_provider=logger_provider)
    logging.getLogger().addHandler(handler)
    logging.getLogger().setLevel(logging.INFO)

    # Auto-instrument requests
    RequestsInstrumentor().instrument()
    _flask_instrumentor = FlaskInstrumentor()

import logging
import sqlite3
from datetime import datetime
from flask import Flask, request, redirect, url_for, render_template_string
import markdown
import re

logger = logging.getLogger(__name__)

app = Flask(__name__)

# Instrument Flask app if OTel is enabled
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    _flask_instrumentor.instrument_app(app)

# Redis cache setup — optional, used when Aspire provides a connection string
redis_client = None
CACHE_TTL = 3600  # 1 hour

def _init_redis():
    """Initialize Redis client from Aspire connection string."""
    global redis_client
    conn_str = os.environ.get("ConnectionStrings__cache")
    if conn_str:
        try:
            import redis
            redis_client = redis.Redis.from_url(
                f"redis://{conn_str}", decode_responses=True
            )
            redis_client.ping()
            logger.info("Redis cache connected: %s", conn_str)
        except Exception as e:
            logger.warning("Redis unavailable, running without cache: %s", e)
            redis_client = None
    else:
        logger.info("No Redis connection string found, running without cache")

def _cache_key(slug):
    return f"wiki:html:{slug}"

def get_cached_html(slug):
    """Retrieve cached rendered HTML for a page."""
    if redis_client is None:
        return None
    try:
        return redis_client.get(_cache_key(slug))
    except Exception:
        return None

def set_cached_html(slug, html):
    """Store rendered HTML in cache."""
    if redis_client is None:
        return
    try:
        redis_client.set(_cache_key(slug), html, ex=CACHE_TTL)
    except Exception:
        pass

def invalidate_cache(slug):
    """Remove cached HTML when a page is edited."""
    if redis_client is None:
        return
    try:
        redis_client.delete(_cache_key(slug))
    except Exception:
        pass

# Database setup
DB_PATH = "wiki.db"

def get_db():
    """Get database connection."""
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn

def init_db():
    """Initialize database and seed with a Home page."""
    conn = get_db()
    cursor = conn.cursor()
    
    # Create pages table
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS pages (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            slug TEXT UNIQUE NOT NULL,
            title TEXT NOT NULL,
            content TEXT NOT NULL,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    """)
    
    # Seed with a Home page if it doesn't exist
    cursor.execute("SELECT COUNT(*) as count FROM pages WHERE slug = ?", ("home",))
    if cursor.fetchone()["count"] == 0:
        cursor.execute("""
            INSERT INTO pages (slug, title, content, updated_at)
            VALUES (?, ?, ?, ?)
        """, (
            "home",
            "Home",
            "# Welcome to the Markdown Wiki\n\nThis is your home page. Click **Edit** to modify it!\n\n## Features\n\n- Simple Markdown editing\n- SQLite backend\n- Orchestrated by Aspire\n- Create new pages\n- Edit existing pages",
            datetime.now()
        ))
    
    conn.commit()
    conn.close()

def slugify(text):
    """Convert text to URL-safe slug."""
    text = text.lower()
    text = re.sub(r'[^\w\s-]', '', text)
    text = re.sub(r'[\s_-]+', '-', text)
    text = re.sub(r'^-+|-+$', '', text)
    return text

# HTML Templates
BASE_TEMPLATE = """
<!DOCTYPE html>
<html>
<head>
    <title>{{ title }} - Markdown Wiki</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
            line-height: 1.6;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            border-bottom: 2px solid #0078d4;
            padding-bottom: 10px;
        }
        nav {
            margin-bottom: 20px;
            padding: 10px 0;
            border-bottom: 1px solid #ddd;
        }
        nav a {
            margin-right: 15px;
            color: #0078d4;
            text-decoration: none;
        }
        nav a:hover {
            text-decoration: underline;
        }
        .page-list {
            list-style: none;
            padding: 0;
        }
        .page-list li {
            margin: 10px 0;
            padding: 10px;
            background-color: #f9f9f9;
            border-radius: 4px;
        }
        .page-list a {
            color: #0078d4;
            text-decoration: none;
            font-weight: 500;
        }
        .page-list a:hover {
            text-decoration: underline;
        }
        .actions {
            margin: 20px 0;
            padding: 10px 0;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
        }
        .actions a, .btn {
            display: inline-block;
            padding: 8px 16px;
            margin-right: 10px;
            background-color: #0078d4;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
        }
        .actions a:hover, .btn:hover {
            background-color: #005a9e;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #545b62;
        }
        textarea {
            width: 100%;
            min-height: 300px;
            padding: 10px;
            font-family: 'Courier New', monospace;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }
        .content {
            margin-top: 20px;
        }
        .timestamp {
            color: #666;
            font-size: 0.9em;
            margin-top: 20px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }
    </style>
</head>
<body>
    <div class="container">
        <nav>
            <a href="/">Home</a>
            <a href="/new">New Page</a>
        </nav>
        {% block content %}{% endblock %}
    </div>
</body>
</html>
"""

INDEX_TEMPLATE = BASE_TEMPLATE.replace("{% block content %}{% endblock %}", """
        <h1>All Wiki Pages</h1>
        <ul class="page-list">
        {% for page in pages %}
            <li>
                <a href="/page/{{ page.slug }}">{{ page.title }}</a>
                <span class="timestamp">Last updated: {{ page.updated_at }}</span>
            </li>
        {% endfor %}
        </ul>
""")

PAGE_TEMPLATE = BASE_TEMPLATE.replace("{% block content %}{% endblock %}", """
        <h1>{{ page.title }}</h1>
        <div class="actions">
            <a href="/page/{{ page.slug }}/edit">Edit</a>
            <a href="/" class="btn-secondary">Back to List</a>
        </div>
        <div class="content">
            {{ content|safe }}
        </div>
        <div class="timestamp">Last updated: {{ page.updated_at }}</div>
""")

EDIT_TEMPLATE = BASE_TEMPLATE.replace("{% block content %}{% endblock %}", """
        <h1>Edit: {{ page.title }}</h1>
        <form method="POST" action="/page/{{ page.slug }}">
            <div class="form-group">
                <label for="content">Markdown Content:</label>
                <textarea name="content" id="content">{{ page.content }}</textarea>
            </div>
            <button type="submit" class="btn">Save</button>
            <a href="/page/{{ page.slug }}" class="btn btn-secondary">Cancel</a>
        </form>
""")

NEW_TEMPLATE = BASE_TEMPLATE.replace("{% block content %}{% endblock %}", """
        <h1>Create New Page</h1>
        <form method="POST" action="/new">
            <div class="form-group">
                <label for="title">Page Title:</label>
                <input type="text" name="title" id="title" required>
            </div>
            <div class="form-group">
                <label for="content">Markdown Content:</label>
                <textarea name="content" id="content"># New Page\n\nStart writing your content here...</textarea>
            </div>
            <button type="submit" class="btn">Create Page</button>
            <a href="/" class="btn btn-secondary">Cancel</a>
        </form>
""")

# Routes
@app.route("/")
def index():
    """List all wiki pages."""
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM pages ORDER BY updated_at DESC")
    pages = cursor.fetchall()
    conn.close()
    return render_template_string(INDEX_TEMPLATE, title="All Pages", pages=pages)

@app.route("/page/<slug>")
def view_page(slug):
    """View a wiki page with rendered Markdown."""
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM pages WHERE slug = ?", (slug,))
    page = cursor.fetchone()
    conn.close()
    
    if not page:
        return f"Page not found: {slug}", 404
    
    # Check cache first, then render and store
    html_content = get_cached_html(slug)
    if html_content is None:
        html_content = markdown.markdown(page["content"], extensions=['extra', 'nl2br'])
        set_cached_html(slug, html_content)
    
    return render_template_string(PAGE_TEMPLATE, title=page["title"], page=page, content=html_content)

@app.route("/page/<slug>/edit")
def edit_page(slug):
    """Show edit form for a wiki page."""
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM pages WHERE slug = ?", (slug,))
    page = cursor.fetchone()
    conn.close()
    
    if not page:
        return f"Page not found: {slug}", 404
    
    return render_template_string(EDIT_TEMPLATE, title=f"Edit {page['title']}", page=page)

@app.route("/page/<slug>", methods=["POST"])
def save_page(slug):
    """Save updated Markdown content."""
    content = request.form.get("content", "")
    
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("""
        UPDATE pages
        SET content = ?, updated_at = ?
        WHERE slug = ?
    """, (content, datetime.now(), slug))
    conn.commit()
    conn.close()
    
    invalidate_cache(slug)
    
    return redirect(url_for("view_page", slug=slug))

@app.route("/new")
def new_page():
    """Show form to create a new page."""
    return render_template_string(NEW_TEMPLATE, title="New Page")

@app.route("/new", methods=["POST"])
def create_page():
    """Create a new wiki page."""
    title = request.form.get("title", "").strip()
    content = request.form.get("content", "")
    
    if not title:
        return "Title is required", 400
    
    slug = slugify(title)
    
    if not slug:
        return "Invalid title", 400
    
    conn = get_db()
    cursor = conn.cursor()
    
    try:
        cursor.execute("""
            INSERT INTO pages (slug, title, content, updated_at)
            VALUES (?, ?, ?, ?)
        """, (slug, title, content, datetime.now()))
        conn.commit()
    except sqlite3.IntegrityError:
        conn.close()
        return f"A page with slug '{slug}' already exists", 400
    
    conn.close()
    return redirect(url_for("view_page", slug=slug))

@app.route("/health")
def health():
    """Health check endpoint."""
    return {"status": "healthy", "service": "markdown-wiki"}, 200

if __name__ == "__main__":
    # Initialize database
    init_db()
    
    # Initialize Redis cache (optional — works without it)
    _init_redis()
    
    # Get port from environment variable
    port = int(os.environ.get("PORT", 8080))
    
    # Use waitress in production, Flask dev server as fallback
    try:
        from waitress import serve
        logger.info("Starting wiki server on port %d with Waitress...", port)
        serve(app, host="0.0.0.0", port=port)
    except ImportError:
        logger.info("Waitress not found, using Flask dev server on port %d...", port)
        app.run(host="0.0.0.0", port=port, debug=True)
