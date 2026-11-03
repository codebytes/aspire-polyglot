"""Markdown wiki: Flask + SQLite, with rendered pages cached in Redis.

This app runs in a container that Aspire builds from ./Dockerfile
(add_dockerfile). Aspire injects PORT, CACHE_URI, and the OTEL_* settings.
"""
import logging
import os
import re
import sqlite3
from datetime import datetime
from pathlib import Path

logging.basicConfig(level=logging.INFO, format="%(levelname)s %(name)s: %(message)s")

if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    # Python's gRPC ignores SSL_CERT_DIR, so point the OTLP exporter at the
    # development certificate Aspire mounts (the first SSL_CERT_DIR entry).
    cert_dir = Path(os.environ.get("SSL_CERT_DIR", "").split(":")[0])
    if cert_dir.name and (pem := next(cert_dir.glob("*.pem"), None)):
        os.environ.setdefault("OTEL_EXPORTER_OTLP_CERTIFICATE", str(pem))

    # The OpenTelemetry distro reads the OTEL_* settings and instruments Flask,
    # Redis, and sqlite3. It must run before those libraries are imported.
    # Aspire's Python integration enables log export; set it for this container too.
    os.environ.setdefault("OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED", "true")
    from opentelemetry.instrumentation.auto_instrumentation import initialize

    initialize()

import markdown  # noqa: E402
import nh3  # noqa: E402
import redis  # noqa: E402
from flask import Flask, abort, redirect, render_template_string, request, url_for  # noqa: E402

logger = logging.getLogger("wiki")
app = Flask(__name__)

# Rendered HTML is cached in Redis. CACHE_URI is rediss:// under Aspire; the
# certificate is verified against SSL_CERT_DIR like any other TLS connection.
cache = redis.Redis.from_url(os.environ.get("CACHE_URI", "redis://localhost:6379"), decode_responses=True)
CACHE_TTL = 3600


def _cache_key(slug):
    return f"wiki:html:{slug}"


def get_cached_html(slug):
    return cache.get(_cache_key(slug))


def set_cached_html(slug, html):
    cache.set(_cache_key(slug), html, ex=CACHE_TTL)


def invalidate_cache(slug):
    cache.delete(_cache_key(slug))


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
    seed_home = cursor.fetchone()["count"] == 0
    if seed_home:
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
    if seed_home:
        invalidate_cache("home")

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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32'%3E%3Crect x='5' y='3' width='22' height='26' rx='3' fill='%230078d4'/%3E%3Cpath d='M10 10h12M10 16h12M10 22h8' stroke='white' stroke-width='2'/%3E%3C/svg%3E">
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
        abort(404, description=f"Page not found: {slug}")
    
    # Check cache first, then render and store
    html_content = get_cached_html(slug)
    if html_content is None:
        html_content = markdown.markdown(page["content"], extensions=['extra', 'nl2br'])
        html_content = sanitize_html(html_content)
        set_cached_html(slug, html_content)
    else:
        html_content = sanitize_html(html_content)
    
    return render_template_string(PAGE_TEMPLATE, title=page["title"], page=page, content=html_content)

def sanitize_html(content):
    return nh3.clean(
        content,
        tags={
            "a", "abbr", "blockquote", "br", "code", "dd", "del", "div", "dl", "dt",
            "em", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "img", "li", "ol",
            "p", "pre", "span", "strong", "sub", "sup", "table", "tbody", "td",
            "th", "thead", "tr", "ul",
        },
        attributes={
            "a": {"href", "title"},
            "abbr": {"title"},
            "img": {"src", "alt", "title"},
            "td": {"colspan", "rowspan"},
            "th": {"colspan", "rowspan"},
        },
        url_schemes={"http", "https", "mailto"},
    )

@app.route("/page/<slug>/edit")
def edit_page(slug):
    """Show edit form for a wiki page."""
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM pages WHERE slug = ?", (slug,))
    page = cursor.fetchone()
    conn.close()
    
    if not page:
        abort(404, description=f"Page not found: {slug}")
    
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
    logger.info("Saved page %s", slug)
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
    invalidate_cache(slug)
    logger.info("Created page %s", slug)
    return redirect(url_for("view_page", slug=slug))

@app.route("/health")
def health():
    """Health check endpoint."""
    return {"status": "healthy", "service": "markdown-wiki"}, 200

if __name__ == "__main__":
    from waitress import serve

    init_db()
    port = int(os.environ.get("PORT", 8080))
    logger.info("Serving the wiki on port %d", port)
    serve(app, host="0.0.0.0", port=port)
