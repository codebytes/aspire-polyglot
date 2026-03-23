import os
import json
import logging

logger = logging.getLogger(__name__)

# --- OpenTelemetry setup (must run before FastAPI app creation) ---
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry import trace, metrics
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
    from opentelemetry.instrumentation.requests import RequestsInstrumentor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    import logging

    resource = Resource.create({
        "service.name": os.environ.get("OTEL_SERVICE_NAME", "fastapi-api"),
    })

    # Traces
    trace_provider = TracerProvider(resource=resource)
    trace_provider.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))
    trace.set_tracer_provider(trace_provider)

    # Metrics
    metric_reader = PeriodicExportingMetricReader(OTLPMetricExporter())
    meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])
    metrics.set_meter_provider(meter_provider)
    logger_provider = LoggerProvider(resource=resource)
    logger_provider.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter()))
    handler = LoggingHandler(logger_provider=logger_provider)
    logging.getLogger().addHandler(handler)
    logging.getLogger().setLevel(logging.INFO)

    # Auto-instrument requests
    RequestsInstrumentor().instrument()

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import redis
import uvicorn

app = FastAPI()

# Instrument FastAPI app if OTel is enabled
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    FastAPIInstrumentor.instrument_app(app)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Connect to Redis using Aspire connection string
redis_conn_str = os.environ.get("ConnectionStrings__cache", "localhost:6379")
redis_client = redis.Redis.from_url(f"redis://{redis_conn_str}", decode_responses=True)

TODOS_KEY = "todos"

class Todo(BaseModel):
    id: int
    text: str
    completed: bool = False

def get_todos():
    cached = redis_client.get(TODOS_KEY)
    if cached:
        return json.loads(cached)
    return []

def save_todos(todos):
    redis_client.set(TODOS_KEY, json.dumps(todos))

@app.get("/api/health")
def health():
    return {"status": "healthy"}

@app.get("/api/todos")
def list_todos():
    todos = get_todos()
    return {"todos": todos}

@app.post("/api/todos")
def add_todo(todo: Todo):
    todos = get_todos()
    todos.append(todo.model_dump())
    save_todos(todos)
    logger.info("Todo created: id=%d text='%s'", todo.id, todo.text)
    return {"todo": todo}

@app.delete("/api/todos/{todo_id}")
def delete_todo(todo_id: int):
    todos = get_todos()
    todos = [t for t in todos if t["id"] != todo_id]
    save_todos(todos)
    logger.info("Todo deleted: id=%d", todo_id)
    return {"success": True}

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8000))
    logger.info("Starting FastAPI server on port %d", port)
    uvicorn.run(app, host="0.0.0.0", port=port)
