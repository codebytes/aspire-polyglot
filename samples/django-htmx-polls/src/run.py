"""Django HTMX polls — runtime entry point.

Implements the six-part Python+OTel+Aspire pattern documented in
`.squad/skills/python-otel-full-stack/SKILL.md`:

  1. Always-on stdlib logging (StreamHandler on root, formatter with
     trace_id/span_id placeholders, _OtelTraceFilter that defaults the
     fields to "0" when no span is active) — guarantees stderr output
     when running standalone without Aspire.
  2. OTel block gated on OTEL_EXPORTER_OTLP_ENDPOINT — graceful degrade.
  3. LoggingInstrumentor(set_logging_format=True) — populates the
     otelTraceID/otelSpanID record-factory fields.
  4. Reusable gRPC SSL-credentials helper that trusts the Aspire dev
     cert (Python gRPC ignores SSL_CERT_DIR otherwise).
  5. Framework-logger hygiene (clear & propagate django, django.request,
     django.server, waitress) — avoids double-emit once root has our
     StreamHandler.
  6. Startup banner.
"""
from __future__ import annotations

import glob as _glob
import logging
import os
import sys

# DJANGO_SETTINGS_MODULE must be set BEFORE any Django import (including
# DjangoInstrumentor) — otherwise Django falls back to the dummy DB backend.
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "pollsite.settings")


# ---------- 1. Always-on stdlib logging ----------
LOG_LEVEL = os.environ.get("LOG_LEVEL", "INFO").upper()
LOG_FORMAT = (
    "%(asctime)s [%(levelname)s] %(name)s "
    "[trace_id=%(otelTraceID)s span_id=%(otelSpanID)s] %(message)s"
)


class _OtelTraceFilter(logging.Filter):
    """Default otelTraceID / otelSpanID to '0' when no span is active so the
    formatter never raises KeyError. LoggingInstrumentor populates real
    values via the record factory once it is initialised."""

    def filter(self, record: logging.LogRecord) -> bool:  # noqa: D401
        if not hasattr(record, "otelTraceID"):
            record.otelTraceID = "0"
        if not hasattr(record, "otelSpanID"):
            record.otelSpanID = "0"
        return True


_root = logging.getLogger()
_root.setLevel(LOG_LEVEL)
for _h in list(_root.handlers):
    _root.removeHandler(_h)
_stream = logging.StreamHandler(sys.stderr)
_stream.setFormatter(logging.Formatter(LOG_FORMAT))
_stream.addFilter(_OtelTraceFilter())
_root.addHandler(_stream)

logger = logging.getLogger("django-polls")


def _load_grpc_ssl_credentials():
    """Build gRPC SSL credentials that trust the Aspire dev certificate.

    The Python gRPC library ships its own root certificate bundle and
    ignores SSL_CERT_DIR. Aspire injects its dev-cert PEM via SSL_CERT_DIR.
    We combine system CAs with the Aspire cert and return a
    grpc.ChannelCredentials. Returns None when no Aspire certs are found
    (caller falls back to default credentials)."""
    ssl_cert_dirs = os.environ.get("SSL_CERT_DIR", "")
    if not ssl_cert_dirs:
        return None

    aspire_pems: list[str] = []
    for cert_dir in ssl_cert_dirs.split(":"):
        if "aspire" in cert_dir.lower():
            for pem_path in sorted(_glob.glob(os.path.join(cert_dir, "*.pem"))):
                try:
                    with open(pem_path) as fh:
                        aspire_pems.append(fh.read())
                except OSError:
                    pass

    if not aspire_pems:
        return None

    system_bundle = ""
    for ca_path in ("/etc/ssl/certs/ca-certificates.crt", "/etc/ssl/cert.pem"):
        if os.path.exists(ca_path):
            with open(ca_path) as fh:
                system_bundle = fh.read()
            break

    combined = system_bundle + "\n" + "\n".join(aspire_pems)

    combined_path = os.path.join(os.environ.get("HOME", "/app"), ".aspire-combined-ca.pem")
    try:
        with open(combined_path, "w") as out:
            out.write(combined)
        os.environ["GRPC_DEFAULT_SSL_ROOTS_FILE_PATH"] = combined_path
    except OSError:
        pass

    import grpc

    return grpc.ssl_channel_credentials(root_certificates=combined.encode("utf-8"))


# ---------- 2. OTel gate ----------
_OTEL_ENABLED = bool(os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"))

if _OTEL_ENABLED:
    from opentelemetry import metrics, trace
    from opentelemetry._logs import set_logger_provider
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.instrumentation.django import DjangoInstrumentor
    from opentelemetry.instrumentation.logging import LoggingInstrumentor
    from opentelemetry.instrumentation.psycopg2 import Psycopg2Instrumentor
    from opentelemetry.instrumentation.requests import RequestsInstrumentor
    from opentelemetry.instrumentation.sqlite3 import SQLite3Instrumentor
    from opentelemetry.instrumentation.system_metrics import SystemMetricsInstrumentor
    from opentelemetry.instrumentation.urllib3 import URLLib3Instrumentor
    from opentelemetry.sdk._logs import LoggerProvider
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor

    _grpc_creds = _load_grpc_ssl_credentials()
    _exporter_kwargs = {"credentials": _grpc_creds} if _grpc_creds else {}

    # Resource.create({}) lets OTEL_SERVICE_NAME / OTEL_RESOURCE_ATTRIBUTES
    # auto-populate so Aspire's resource tagging flows through.
    _resource = Resource.create({})

    # Traces
    _tracer_provider = TracerProvider(resource=_resource)
    _tracer_provider.add_span_processor(
        BatchSpanProcessor(OTLPSpanExporter(**_exporter_kwargs))
    )
    trace.set_tracer_provider(_tracer_provider)

    # Metrics
    _metric_reader = PeriodicExportingMetricReader(
        OTLPMetricExporter(**_exporter_kwargs)
    )
    metrics.set_meter_provider(
        MeterProvider(resource=_resource, metric_readers=[_metric_reader])
    )

    # Logs — provider + processor. We deliberately DO NOT attach our own
    # LoggingHandler to the root logger here. opentelemetry-instrumentation-
    # logging >= 0.50b0 auto-attaches a LoggingHandler bound to the global
    # LoggerProvider when LoggingInstrumentor().instrument() is called, so
    # adding one ourselves yields duplicate OTLP log records (we observed
    # every log line appearing twice in the Aspire dashboard during the
    # turn-2 validation run). Just publish the provider globally and let
    # LoggingInstrumentor pick it up via get_logger_provider().
    _logger_provider = LoggerProvider(resource=_resource)
    _logger_provider.add_log_record_processor(
        BatchLogRecordProcessor(OTLPLogExporter(**_exporter_kwargs))
    )
    set_logger_provider(_logger_provider)

    # ---------- 3. THE CRITICAL CALL ----------
    # set_logging_format=True is what makes the LoggingInstrumentor record
    # factory inject otelTraceID / otelSpanID. Without this, the instrumentor
    # is a no-op for record-factory injection and dashboard logs show
    # trace_id="" / span_id="". This call also auto-attaches a LoggingHandler
    # to the root logger (using the LoggerProvider we just set globally) so
    # every logger.info() flows out via OTLP — that's why we don't add one
    # manually above.
    LoggingInstrumentor().instrument(set_logging_format=True)

    # Auto-instrument Django HTTP server spans (must happen before
    # django.setup() so the WSGI handler is wrapped).
    DjangoInstrumentor().instrument()

    # DB-client spans for both Postgres (when connection string is injected
    # by Aspire) and SQLite (the standalone fallback in pollsite/settings.py).
    Psycopg2Instrumentor().instrument()
    SQLite3Instrumentor().instrument()

    # Outgoing HTTP client spans — covers requests + urllib3 (used by Django
    # and most Python SDKs internally).
    RequestsInstrumentor().instrument()
    URLLib3Instrumentor().instrument()

    # Process / system metrics so the dashboard's Metrics tab populates
    # with CPU + memory + GC counters.
    SystemMetricsInstrumentor().instrument()


# ---------- 5. Framework-logger hygiene ----------
# Django installs its own StreamHandler on the `django` logger at import time
# and waitress emits to a separate `waitress` logger. Without normalising
# them, every log line appears twice (once from each framework handler, once
# from our root StreamHandler). Clear handlers, set propagate=True so they
# bubble up through our root handler instead.
#
# IMPORTANT: this MUST happen AFTER django.setup() — Django re-configures
# its loggers via DEFAULT_LOGGING during setup, so any handlers we strip
# beforehand reappear. The cleanup is therefore performed below, just before
# the WSGI server starts.


# ---------- Django setup + migrations ----------
import django  # noqa: E402

django.setup()

from django.conf import settings  # noqa: E402
from django.core.management import call_command  # noqa: E402
from django.core.wsgi import get_wsgi_application  # noqa: E402
from waitress import serve  # noqa: E402

# ---------- 6. Startup banner ----------
_db_engine = settings.DATABASES["default"]["ENGINE"].rsplit(".", 1)[-1]
_db_host = settings.DATABASES["default"].get("HOST", "")
_db_name = settings.DATABASES["default"].get("NAME", "")
_port = int(os.environ.get("PORT", 8080))

logger.info("=" * 70)
logger.info("django-htmx-polls starting")
logger.info("  port             = %s", _port)
logger.info("  service.name     = %s", os.environ.get("OTEL_SERVICE_NAME", "(unset)"))
logger.info("  log level        = %s", LOG_LEVEL)
logger.info(
    "  otlp endpoint    = %s",
    os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT", "(disabled — standalone mode)"),
)
logger.info("  db engine        = %s", _db_engine)
if _db_host:
    logger.info("  db host          = %s", _db_host)
logger.info("  db name          = %s", _db_name)
logger.info("=" * 70)

logger.info("Running database migrations…")
call_command("migrate", "--no-input", verbosity=1)
logger.info("Migrations complete.")

app = get_wsgi_application()

# Framework-logger hygiene — must happen AFTER django.setup() AND
# get_wsgi_application(), because Django re-runs configure_logging during
# both. Strip the StreamHandler that Django attaches to the `django` parent
# logger (which writes unformatted records like "Not Found: /polls/" to
# stderr alongside the propagated, formatted line from our root handler),
# the AdminEmailHandler (silent in DEBUG anyway), and waitress's own handler.
for _name in ("django", "django.request", "django.server", "django.db.backends", "waitress"):
    _lg = logging.getLogger(_name)
    for _h in list(_lg.handlers):
        _lg.removeHandler(_h)
    _lg.propagate = True
    _lg.setLevel(LOG_LEVEL)

logger.info("Serving Django WSGI app via waitress on 0.0.0.0:%d", _port)
serve(app, host="0.0.0.0", port=_port)
