# Django + HTMX Polls App

A modern voting polls application demonstrating **server-rendered interactivity** using Django and HTMX, orchestrated by Aspire.

## What's Special About This Sample?

This sample showcases:

- **Django** web framework for rapid Python-based web development
- **HTMX** for interactive UI updates without writing JavaScript
- **Waitress** WSGI server for production-ready Python hosting
- **Aspire** orchestration of Python applications

### Why HTMX?

HTMX allows you to build interactive, modern web UIs using **server-rendered HTML** instead of JSON APIs and heavy JavaScript frameworks. When you vote on a poll, HTMX sends an HTTP request and swaps in the updated HTML — no React, Vue, or Angular needed!

## Architecture

```
┌─────────────────────────────────────────┐
│         Aspire AppHost                  │
│   (Orchestration & Service Discovery)   │
└────────────┬──────────────┬─────────────┘
             │              │
             │ Python App   │ Container
             ▼              ▼
┌──────────────────┐  ┌────────────┐
│  Django + HTMX   │  │ PostgreSQL │
│  ┌────────────┐  │  │  (pollsdb) │
│  │  Waitress  │  │  └────────────┘
│  │     ↓      │  │        ▲
│  │   Views  ──┼──┼────────┘
│  │     ↓      │  │  database
│  │  Django ORM│  │
│  └────────────┘  │
└──────────────────┘

Falls back to SQLite when running standalone (no Aspire).
```

## Features

- 📊 **Create Polls** with multiple choice answers
- 🗳️ **Vote in Real-Time** with HTMX-powered partial updates
- 📈 **Animated Bar Charts** showing vote percentages
- 🎨 **Beautiful UI** with gradient backgrounds and smooth animations
- 🚀 **Small client surface** — voting and dynamic choice fields use HTMX attributes

## Getting Started

### Prerequisites

- [Aspire CLI](https://aspire.dev/get-started/install-cli/) **13.5.3** — must match the SDK this sample is pinned to (`13.5.3`, see `sdk.version` in `aspire.config.json`). Check yours with `aspire --version`. A mismatch causes the errors in [Troubleshooting](#troubleshooting).
- [Docker](https://docs.docker.com/get-docker/) — runs the PostgreSQL container and builds the Django image.
- .NET 10 SDK, Python 3.12+, and [uv](https://docs.astral.sh/uv/) for the preview Python AppHost. Django itself runs in a container.

### Run with Aspire (recommended)

From the **sample root** (not `src/`):

```bash
aspire run
```

For worktrees, use `aspire start --apphost apphost.py --isolated`.
Stop only this sample with `aspire stop --apphost apphost.py`.

That's all — there is no local `pip install` step. The Django app is built from `src/Dockerfile` and its dependencies are installed during the image build. `aspire run` starts PostgreSQL, builds and starts the app, injects the `pollsdb` connection string, and runs database migrations on startup.

Then open the Aspire dashboard (URL shown in the terminal), click the `polls` endpoint, and start voting. The app starts with an empty poll list; run `python manage.py seed` (in the `polls` container, or locally in standalone mode) to load sample polls.

### Run standalone (without Aspire)

With no Aspire-provided connection string, the app falls back to SQLite:

```bash
cd src
pip install -r requirements.txt
python manage.py migrate
python manage.py seed   # optional: load sample polls
python run.py
```

Then open http://localhost:8080.

### Troubleshooting

**`AttributeError: 'IDistributedApplicationBuilder' object has no attribute 'postgres'`** (or `add_postgres`), or **`CapabilityError: Unknown capability: Aspire.Hosting/createBuilderWithOptions`:**

Both mean your Aspire CLI version does not match the SDK this sample is pinned to. The Python AppHost talks to a version-specific backend, and the generated helper module in `.aspire/modules/aspire_app.py` must line up with your CLI — otherwise a capability such as `postgres` (from `Aspire.Hosting.PostgreSQL`) or `createBuilderWithOptions` won't resolve.

To fix it, align the versions:

1. Run `aspire --version` and confirm it is **13.5.3**, matching `sdk.version` in `aspire.config.json`. Install or update the CLI if needed.
2. Run `aspire restore --apphost apphost.py` to regenerate the Python bindings using the configured SDK and packages. Do not edit `.aspire/modules/` manually or discard unrelated working-tree changes.

**"Failed to install the Python dependencies":** you do not need to `pip install` anything to use `aspire run` — the app's dependencies are installed in the container image, not in your shell. Just run `aspire run` from the sample root.

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
       Choice.objects.filter(pk=choice.pk).update(votes=F('votes') + 1)
       return render(request, 'results_partial.html', {...})
   ```

3. **HTMX** replaces `#results` div with the server's HTML response
4. **CSS Transitions** animate the bar chart changes

No JSON API or client-side state management is needed. Both a full detail-page
load and an HTMX vote response render the same results context, so vote counts
remain visible after a refresh. Vote increments are atomic in PostgreSQL.
Repeated **Add Another Choice** clicks assign unique field names; all submitted
choices are retained, including the tenth and later choices.

Run `python manage.py test polls` inside the `polls` container to check rendering,
validation, choice creation, and concurrent voting against a separate test
database. The concurrency test is skipped for standalone SQLite.

## Project Structure

```
django-htmx-polls/
├── apphost.py                  # Aspire orchestration (Python AppHost)
├── aspire.config.json          # Pinned SDK + package versions
├── .aspire/
│   └── modules/
│       └── aspire_app.py       # Generated Aspire helper (create_builder)
├── src/
│   ├── Dockerfile              # Image built and run by add_dockerfile
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

The Python AppHost imports `create_builder` from the generated `.aspire/modules/aspire_app.py` helper, then wires the graph: `add_postgres("pg").add_database("pollsdb")` for the database and `add_dockerfile("polls", "./src")` to build and run Django from `src/Dockerfile`. `with_reference(pollsdb)` injects the connection string, `wait_for(pollsdb)` orders startup, and `with_http_endpoint(target_port=8080, env="PORT")` tells the app which port to bind — Django reads `PORT` in `run.py`.

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

- **Backend:** Django 6, Python 3.12+
- **Frontend:** HTMX 2.0, vanilla CSS
- **Server:** Waitress WSGI server
- **Database:** PostgreSQL (via Aspire) with SQLite fallback
- **Orchestration:** Aspire
- **Static Files:** WhiteNoise

## Learn More

- [HTMX Documentation](https://htmx.org/docs/)
- [Django Documentation](https://docs.djangoproject.com/)
- [Aspire Documentation](https://learn.microsoft.com/en-us/dotnet/aspire/)
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
