"""TODO API: FastAPI + Redis.

Aspire runs this natively with uvicorn (addUvicornApp) and injects:
  CACHE_URI  redis[s]://:password@host:port for the "cache" resource
  OTEL_*     where to send traces, metrics, and logs
"""
import json
import logging
import os

logging.basicConfig(level=logging.INFO, format="%(levelname)s %(name)s: %(message)s")

# The OpenTelemetry distro reads Aspire's OTEL_* settings, exports traces,
# metrics, and logs, and instruments FastAPI and Redis. Run it before importing them.
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry.instrumentation.auto_instrumentation import initialize

    initialize()

import redis  # noqa: E402
from fastapi import FastAPI  # noqa: E402
from pydantic import BaseModel  # noqa: E402

app = FastAPI(title="TODO API")
logger = logging.getLogger("todos")
redis_client = redis.Redis.from_url(os.environ.get("CACHE_URI", "redis://localhost:6379"), decode_responses=True)

TODOS_KEY = "todos"


class Todo(BaseModel):
    id: int
    text: str
    completed: bool = False


def get_todos():
    cached = redis_client.get(TODOS_KEY)
    return json.loads(cached) if cached else []


def update_todos(update):
    """Read-modify-write under WATCH so concurrent requests never overwrite each other."""

    def mutate(pipe):
        cached = pipe.get(TODOS_KEY)
        updated = update(json.loads(cached) if cached else [])
        pipe.multi()
        pipe.set(TODOS_KEY, json.dumps(updated))

    redis_client.transaction(mutate, TODOS_KEY)


@app.get("/api/health")
def health():
    redis_client.ping()
    return {"status": "healthy"}


@app.get("/api/todos")
def list_todos():
    return {"todos": get_todos()}


@app.post("/api/todos")
def add_todo(todo: Todo):
    update_todos(lambda todos: [*todos, todo.model_dump()])
    logger.info("Created todo %d: %s", todo.id, todo.text)
    return {"todo": todo}


@app.delete("/api/todos/{todo_id}")
def delete_todo(todo_id: int):
    update_todos(lambda todos: [t for t in todos if t["id"] != todo_id])
    logger.info("Deleted todo %d", todo_id)
    return {"success": True}
