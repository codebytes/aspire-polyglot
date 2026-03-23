import os
import sys

# --- OpenTelemetry setup (must run before Django setup) ---
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry import trace
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.instrumentation.django import DjangoInstrumentor
    from opentelemetry.instrumentation.requests import RequestsInstrumentor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    import logging

    resource = Resource.create({
        "service.name": os.environ.get("OTEL_SERVICE_NAME", "django-polls"),
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

    # Auto-instrument Django and requests
    DjangoInstrumentor().instrument()
    RequestsInstrumentor().instrument()

# Set Django settings module
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "pollsite.settings")

# Run migrations automatically on startup
import django
django.setup()
from django.core.management import call_command
print("Running database migrations...")
call_command("migrate", "--no-input", verbosity=1)

# Import Django's WSGI application
from django.core.wsgi import get_wsgi_application
from waitress import serve

app = get_wsgi_application()

# Get port from environment (Aspire sets this)
port = int(os.environ.get("PORT", 8000))
print(f"Starting Django polls app on port {port}")
serve(app, host="0.0.0.0", port=port)
