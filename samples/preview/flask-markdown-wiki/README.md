# Flask Markdown wiki: Python AppHost + Dockerfile

The AppHost is written in **Python** (preview), and this is the repository's
**Dockerfile** example: Aspire builds the Flask wiki from `src/Dockerfile` and
runs it as a container next to Redis, which caches rendered pages.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `wiki` | container built from `src/Dockerfile` | `add_dockerfile` |
| `cache` | Redis container (TLS, generated password) | `add_redis` |

```python
cache = builder.add_redis("cache")

(
    builder.add_dockerfile("wiki", "./src")
    .with_reference(cache)
    .wait_for(cache)
    .with_otlp_exporter()
    .with_http_endpoint(target_port=8080, env="PORT")
)
```

Compare [django-htmx-polls](../django-htmx-polls), where the same AppHost
language runs Django natively with `add_python_app` instead.

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, Python 3.12+ with
[uv](https://docs.astral.sh/uv/) (for the AppHost), Docker.

```bash
cd samples/preview/flask-markdown-wiki
aspire run
```

Open **wiki**, edit the Home page, and create a page. In the dashboard:

- **Traces**: `GET /page/<slug>` with Redis `GET`/`SET` and SQLite spans; a cache
  hit skips the database.
- **Structured logs**: `Created page …`, `Saved page …`.

## How it is wired

- `with_reference(cache)` injects `CACHE_URI` (`rediss://…@cache.dev.internal:6379`).
  `redis.Redis.from_url` connects with full TLS verification: Aspire mounts its
  development certificate into the container and lists it in `SSL_CERT_DIR`.
- `with_otlp_exporter()` points the container at the dashboard. Python's gRPC
  ignores `SSL_CERT_DIR`, so [`main.py`](src/main.py) sets
  `OTEL_EXPORTER_OTLP_CERTIFICATE` to that mounted certificate before starting the
  OpenTelemetry distro.
- `PIP_INDEX_URL`, when set in your shell, is forwarded to `pip install` in the
  image build for private package feeds.
- Pages live in SQLite inside the container; recreating it resets them. Editing or
  creating a page invalidates its cached HTML, and rendered Markdown is sanitized
  with `nh3`.

## Tests

```bash
cd src
uv run --with-requirements requirements.txt python -m unittest -v test_wiki
```
