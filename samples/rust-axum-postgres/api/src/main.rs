//! Guestbook API: Axum + PostgreSQL.
//!
//! Aspire runs this natively with `cargo run` (AddRustApp) and injects:
//!   PORT             the port to listen on
//!   GUESTBOOKDB_URI  postgresql://user:password@host:port/guestbookdb
//!   OTEL_*           where to send traces, metrics, and logs

mod telemetry;

use std::sync::Arc;

use axum::{
    Json, Router,
    extract::{MatchedPath, State},
    http::{Request, StatusCode},
    response::{Html, IntoResponse, Response},
    routing::get,
};
use opentelemetry::{KeyValue, global, metrics::Counter};
use serde::{Deserialize, Serialize};
use tokio_postgres::{Client, NoTls};
use tower_http::trace::TraceLayer;
use tracing::{Span, info, instrument, warn};

struct AppState {
    db: Client,
    entries_signed: Counter<u64>,
}

#[derive(Serialize)]
struct Entry {
    id: i32,
    name: String,
    message: String,
    created_at: String,
}

#[derive(Deserialize)]
struct NewEntry {
    name: String,
    message: String,
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let telemetry = telemetry::init()?;

    let uri = std::env::var("GUESTBOOKDB_URI")
        .unwrap_or_else(|_| "postgresql://postgres@localhost:5432/guestbookdb".into());
    let (db, connection) = tokio_postgres::connect(&uri, NoTls).await?;
    tokio::spawn(async move {
        if let Err(error) = connection.await {
            tracing::error!(%error, "postgres connection closed");
        }
    });
    db.batch_execute(
        "CREATE TABLE IF NOT EXISTS entries (
            id         SERIAL PRIMARY KEY,
            name       TEXT NOT NULL,
            message    TEXT NOT NULL,
            created_at TIMESTAMPTZ NOT NULL DEFAULT now())",
    )
    .await?;

    let state = Arc::new(AppState {
        db,
        entries_signed: global::meter("guestbook")
            .u64_counter("guestbook.entries.signed")
            .build(),
    });

    let app = Router::new()
        .route("/", get(|| async { Html(PAGE) }))
        .route("/health", get(health))
        .route("/api/entries", get(list_entries).post(sign))
        .with_state(state)
        // One server span per request, named by route (e.g. "POST /api/entries").
        .layer(
            TraceLayer::new_for_http()
                .make_span_with(|request: &Request<_>| {
                    let route = request
                        .extensions()
                        .get::<MatchedPath>()
                        .map_or(request.uri().path(), |p| p.as_str());
                    tracing::info_span!("request",
                        otel.name = format!("{} {route}", request.method()),
                        otel.kind = "server",
                        http.request.method = %request.method(),
                        http.route = route,
                        http.response.status_code = tracing::field::Empty)
                })
                .on_response(|response: &Response, _latency, span: &Span| {
                    span.record("http.response.status_code", response.status().as_u16());
                }),
        );

    let port = std::env::var("PORT").unwrap_or_else(|_| "8080".into());
    let listener = tokio::net::TcpListener::bind(format!("0.0.0.0:{port}")).await?;
    info!(port, "guestbook API listening");
    axum::serve(listener, app)
        .with_graceful_shutdown(async {
            tokio::signal::ctrl_c().await.ok();
        })
        .await?;

    telemetry.shutdown();
    Ok(())
}

async fn health(State(state): State<Arc<AppState>>) -> StatusCode {
    match state.db.simple_query("SELECT 1").await {
        Ok(_) => StatusCode::OK,
        Err(_) => StatusCode::SERVICE_UNAVAILABLE,
    }
}

// #[instrument] makes each database call a child span of the request.
#[instrument(skip(state))]
async fn list_entries(State(state): State<Arc<AppState>>) -> Result<Json<Vec<Entry>>, AppError> {
    let rows = state
        .db
        .query(
            "SELECT id, name, message, created_at::text FROM entries ORDER BY id DESC LIMIT 20",
            &[],
        )
        .await?;
    let entries: Vec<Entry> = rows
        .iter()
        .map(|row| Entry {
            id: row.get(0),
            name: row.get(1),
            message: row.get(2),
            created_at: row.get(3),
        })
        .collect();
    info!(count = entries.len(), "listed guestbook entries");
    Ok(Json(entries))
}

#[instrument(skip(state, entry), fields(name = %entry.name))]
async fn sign(
    State(state): State<Arc<AppState>>,
    Json(entry): Json<NewEntry>,
) -> Result<(StatusCode, Json<Entry>), AppError> {
    let (name, message) = (entry.name.trim(), entry.message.trim());
    if name.is_empty() || message.is_empty() || name.len() > 100 || message.len() > 500 {
        warn!("rejected an invalid guestbook entry");
        return Err(AppError::BadRequest(
            "name (up to 100) and message (up to 500) are required",
        ));
    }
    let row = state
        .db
        .query_one(
            "INSERT INTO entries (name, message) VALUES ($1, $2) RETURNING id, created_at::text",
            &[&name, &message],
        )
        .await?;
    state
        .entries_signed
        .add(1, &[KeyValue::new("source", "api")]);
    let entry = Entry {
        id: row.get(0),
        name: name.into(),
        message: message.into(),
        created_at: row.get(1),
    };
    info!(id = entry.id, "guestbook signed");
    Ok((StatusCode::CREATED, Json(entry)))
}

enum AppError {
    BadRequest(&'static str),
    Database(tokio_postgres::Error),
}

impl From<tokio_postgres::Error> for AppError {
    fn from(error: tokio_postgres::Error) -> Self {
        AppError::Database(error)
    }
}

impl IntoResponse for AppError {
    fn into_response(self) -> Response {
        match self {
            AppError::BadRequest(message) => (StatusCode::BAD_REQUEST, message).into_response(),
            AppError::Database(error) => {
                tracing::error!(%error, "database query failed");
                (StatusCode::INTERNAL_SERVER_ERROR, "database error").into_response()
            }
        }
    }
}

const PAGE: &str = r#"<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Rust Guestbook</title>
<style>
  :root { font-family: system-ui, sans-serif; color: #e6e6e6; background: #12141c; }
  main { max-width: 620px; margin: 0 auto; padding: 3rem 1.5rem; }
  h1 { margin: 0 0 .25rem; } .sub { color: #9aa0b4; margin: 0 0 1.5rem; }
  form { display: grid; gap: .5rem; margin-bottom: 1.5rem; }
  input, textarea { padding: .6rem; border: 1px solid #2c2f3c; border-radius: 8px; background: #1b1e29; color: inherit; font: inherit; }
  button { padding: .6rem; border: 0; border-radius: 8px; background: #f74c00; color: #fff; font-weight: 600; cursor: pointer; }
  li { list-style: none; padding: .75rem 0; border-bottom: 1px solid #2c2f3c; } ul { padding: 0; }
  small { color: #9aa0b4; }
</style></head>
<body><main>
  <h1>&#129408; Rust Guestbook</h1>
  <p class="sub">Axum &middot; PostgreSQL &mdash; run natively with cargo by a C# AppHost.</p>
  <form id="form">
    <input id="name" placeholder="Your name" maxlength="100" required>
    <textarea id="message" placeholder="Leave a message" maxlength="500" required></textarea>
    <button>Sign the guestbook</button>
  </form>
  <ul id="entries"></ul>
</main>
<script>
const list = document.getElementById('entries');
async function load() {
  const entries = await (await fetch('/api/entries')).json();
  list.replaceChildren(...entries.map(e => {
    const item = document.createElement('li');
    const who = document.createElement('strong'); who.textContent = e.name;
    const when = document.createElement('small'); when.textContent = ' ' + e.created_at;
    const text = document.createElement('div'); text.textContent = e.message;
    item.append(who, when, text);
    return item;
  }));
}
document.getElementById('form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const name = document.getElementById('name'), message = document.getElementById('message');
  await fetch('/api/entries', { method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name: name.value, message: message.value }) });
  message.value = '';
  load();
});
load();
</script>
</body></html>"#;
