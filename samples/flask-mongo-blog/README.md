# Flask + MongoDB Blog Engine

A modern blog application built with **Flask** and **MongoDB**, orchestrated by **.NET Aspire**.

## Why This Sample?

This sample demonstrates:

- **MongoDB Integration** - Using `AddMongoDB()` with `.NET Aspire`, distinct from PostgreSQL patterns used in other samples
- **Mongo Express** - Admin UI via `WithMongoExpress()` for database inspection
- **Python + Flask** - Web framework with Jinja2 templating
- **Document Database** - Flexible schema for blog posts with tags, authors, and Markdown content
- **Connection String Patterns** - How MongoDB connection strings differ from SQL databases

## Architecture

```
┌─────────────────────┐
│   .NET Aspire       │
│   apphost.py        │
└──────┬──────────────┘
       │
       ├─────► MongoDB Container (port 27017)
       │       └─── blogdb database
       │
       ├─────► Mongo Express (Web UI)
       │
       └─────► Flask App (blog)
               └─── Python/Flask web server
```

## Key Features

### Blog Functionality
- **Post Listing** - View all posts with pagination
- **Full Post View** - Markdown-rendered content
- **Create Posts** - Form to write new posts with Markdown
- **Tag Filtering** - Browse posts by tag
- **JSON API** - RESTful endpoint for programmatic access
- **Health Check** - MongoDB connection verification

### Technical Highlights

1. **MongoDB Document Model**
   ```json
   {
     "_id": ObjectId,
     "title": "string",
     "content": "markdown text",
     "author": "string",
     "tags": ["array", "of", "tags"],
     "created_at": ISODate
   }
   ```

2. **Connection String Parsing**
   - Aspire injects: `ConnectionStrings__blogdb`
   - Flask reads and connects with `pymongo.MongoClient`
   - No SQL migrations needed - schema-less design

3. **Mongo Express**
   - Accessible via Aspire dashboard
   - Browse collections, documents, indexes
   - Run ad-hoc queries

## Running the Sample

### Prerequisites
- .NET 9.0 SDK
- Python 3.11+
- Docker (for MongoDB container)

### Start with Aspire

```bash
aspire run
```

This will:
1. Start MongoDB container
2. Start Mongo Express admin UI
3. Install Python dependencies (`requirements.txt`)
4. Launch Flask app with Waitress WSGI server

### Access Points

- **Blog**: Check Aspire dashboard for the `blog` endpoint
- **Mongo Express**: Check dashboard for `mongodb` resource endpoint
- **Aspire Dashboard**: Default `http://localhost:15888`

## Connection String Format

### SQL Databases (PostgreSQL)
```
postgresql://user:password@host:port/database
```

### MongoDB
```
mongodb://host:port
```

The database name is specified separately:
- Aspire: `mongo.AddDatabase("blogdb")`
- Python: `client.get_database()` auto-selects from connection string

## Differences from David Fowler's Samples

| Aspect | David Fowler | This Sample |
|--------|-------------|-------------|
| Database | PostgreSQL everywhere | MongoDB |
| Schema | Relational with migrations | Document-based, flexible |
| Admin UI | pgAdmin | Mongo Express |
| Connection | `AddPostgres()` | `AddMongoDB()` |
| Python Usage | Often with Postgres drivers | With PyMongo |

## Sample Data

On first run, the app seeds 3 blog posts:
1. Welcome to Flask + MongoDB Blog
2. Why MongoDB for Blogging?
3. .NET Aspire Orchestration

## Extending the Sample

Ideas for expansion:
- Add full-text search with MongoDB text indexes
- Implement comments as embedded documents
- Add user authentication
- Create an RSS feed
- Implement post editing and deletion
- Add image uploads with GridFS

## Tech Stack

- **.NET Aspire 9.2.1** - Orchestration
- **Flask** - Python web framework
- **MongoDB** - Document database
- **Mongo Express** - Database admin UI
- **Waitress** - Production WSGI server
- **Markdown** - Content formatting
- **Jinja2** - Templating engine

## Files Structure

```
flask-mongo-blog/
├── apphost.py               # Aspire orchestration
├── .aspire/
│   └── settings.json        # Aspire configuration
├── src/
│   ├── main.py              # Flask application
│   ├── requirements.txt     # Python dependencies
│   └── templates/
│       ├── base.html        # Base layout
│       ├── index.html       # Post listing
│       ├── post.html        # Single post view
│       ├── new.html         # Create post form
│       └── tag.html         # Tag filter view
└── README.md
```

## Learning Resources

- [.NET Aspire Documentation](https://learn.microsoft.com/en-us/dotnet/aspire/)
- [MongoDB Python Driver (PyMongo)](https://pymongo.readthedocs.io/)
- [Flask Documentation](https://flask.palletsprojects.com/)
- [Mongo Express](https://github.com/mongo-express/mongo-express)

---

**Polyglot Sample** | Demonstrates MongoDB + Python orchestrated by .NET Aspire
