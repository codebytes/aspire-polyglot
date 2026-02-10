# Django + HTMX Polls App

A modern voting polls application demonstrating **server-rendered interactivity** using Django and HTMX, orchestrated by .NET Aspire.

## What's Special About This Sample?

This sample showcases:

- **Django** web framework for rapid Python-based web development
- **HTMX** for interactive UI updates without writing JavaScript
- **Waitress** WSGI server for production-ready Python hosting
- **.NET Aspire** orchestration of Python applications

### Why HTMX?

HTMX allows you to build interactive, modern web UIs using **server-rendered HTML** instead of JSON APIs and heavy JavaScript frameworks. When you vote on a poll, HTMX sends an HTTP request and swaps in the updated HTML — no React, Vue, or Angular needed!

## Architecture

```
┌─────────────────────────────────────────┐
│         .NET Aspire AppHost             │
│   (Orchestration & Service Discovery)   │
└────────────────┬────────────────────────┘
                 │
                 │ Python App
                 ▼
┌─────────────────────────────────────────┐
│         Django + HTMX                   │
│  ┌─────────────────────────────────┐   │
│  │  Waitress WSGI Server           │   │
│  │  ↓                              │   │
│  │  Django Views                   │   │
│  │  ↓                              │   │
│  │  SQLite Database                │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

## Features

- 📊 **Create Polls** with multiple choice answers
- 🗳️ **Vote in Real-Time** with HTMX-powered partial updates
- 📈 **Animated Bar Charts** showing vote percentages
- 🎨 **Beautiful UI** with gradient backgrounds and smooth animations
- 🚀 **Zero JavaScript** written — all interactivity via HTMX attributes

## Getting Started

### Prerequisites

- .NET 9.0 SDK
- Python 3.11+
- pip

### Run the Application

1. **Install Python dependencies:**
   ```bash
   cd src
   pip install -r requirements.txt
   cd ..
   ```

2. **Run with Aspire:**
   ```bash
   aspire run
   ```

3. **Access the app:**
   - Open the Aspire dashboard (shown in terminal)
   - Click on the `polls` endpoint
   - Start voting!

The database will be automatically created and seeded with sample polls on first run.

## How HTMX Works Here

### Voting Without Page Reload

When you click a vote button, here's what happens:

1. **HTML Button** with HTMX attributes:
   ```html
   <button 
       hx-post="/poll/1/vote/3/"
       hx-target="#results"
       hx-swap="innerHTML"
   >Vote</button>
   ```

2. **Django View** increments vote count and returns HTML:
   ```python
   def vote(request, poll_id, choice_id):
       choice.votes += 1
       choice.save()
       return render(request, 'results_partial.html', {...})
   ```

3. **HTMX** replaces `#results` div with the server's HTML response
4. **CSS Transitions** animate the bar chart changes

No JSON, no JavaScript fetch, no state management — just HTML over the wire!

## Project Structure

```
django-htmx-polls/
├── apphost.py                  # Aspire orchestration
├── .aspire/
│   └── settings.json
├── src/
│   ├── run.py                  # Waitress WSGI entry point
│   ├── manage.py               # Django management
│   ├── requirements.txt        # Python dependencies
│   ├── pollsite/               # Django project
│   │   ├── settings.py
│   │   ├── urls.py
│   │   └── wsgi.py
│   └── polls/                  # Django app
│       ├── models.py           # Poll & Choice models
│       ├── views.py            # View functions
│       ├── urls.py             # URL routing
│       ├── templates/          # HTML templates
│       └── management/
│           └── commands/
│               └── seed.py     # Database seeding
└── README.md
```

## Key Files Explained

### apphost.py

Uses `AddPythonApp()` to run Django via the `run.py` wrapper script. Aspire sets the `PORT` environment variable, and the Python app binds to it.

### src/run.py

Starts Django using Waitress WSGI server instead of Django's development server. This is more suitable for containerization and production-like hosting.

### src/polls/views.py

Django views return either:
- **Full HTML pages** (for initial page loads)
- **HTML partials** (for HTMX updates)

The `vote()` view returns just the results HTML, which HTMX injects into the page.

### src/polls/templates/polls/detail.html

The poll detail page uses HTMX attributes:
- `hx-post`: Send POST request on click
- `hx-target`: Element to update with response
- `hx-swap`: How to swap content (innerHTML, outerHTML, etc.)

## Technologies

- **Backend:** Django 5.0, Python 3.11+
- **Frontend:** HTMX 2.0, vanilla CSS
- **Server:** Waitress WSGI server
- **Database:** SQLite
- **Orchestration:** .NET Aspire 9.2
- **Static Files:** WhiteNoise

## Learn More

- [HTMX Documentation](https://htmx.org/docs/)
- [Django Documentation](https://docs.djangoproject.com/)
- [.NET Aspire Documentation](https://learn.microsoft.com/en-us/dotnet/aspire/)
- [Waitress WSGI Server](https://docs.pylonsproject.org/projects/waitress/)

## Why This Approach?

Traditional SPAs require:
- JSON APIs
- JavaScript state management
- Complex build tools
- Larger bundle sizes

HTMX + Django gives you:
- ✅ Server-rendered HTML (SEO-friendly)
- ✅ Simple mental model (request → HTML → swap)
- ✅ No build step
- ✅ Progressive enhancement
- ✅ Full Python ecosystem

Perfect for internal tools, admin panels, and content-driven applications where you want interactivity without SPA complexity!
