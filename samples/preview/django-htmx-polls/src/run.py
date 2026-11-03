"""Entry point Aspire runs natively (add_python_app): migrate, seed, then serve.

Aspire injects PORT, POLLSDB_URI, and the OTEL_* settings.
"""
import logging
import os

logging.basicConfig(level=logging.INFO, format="%(levelname)s %(name)s: %(message)s")
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "pollsite.settings")

# The OpenTelemetry distro reads Aspire's OTEL_* settings, exports traces,
# metrics, and logs, and instruments Django and psycopg. Run it before Django loads.
if os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
    from opentelemetry.instrumentation.auto_instrumentation import initialize

    initialize()

import django  # noqa: E402

django.setup()

from django.core.management import call_command  # noqa: E402
from django.core.wsgi import get_wsgi_application  # noqa: E402
from waitress import serve  # noqa: E402

call_command("migrate", "--no-input", verbosity=0)
call_command("seed")

port = int(os.environ.get("PORT", 8080))
logging.getLogger("polls").info("Serving polls on port %d", port)
serve(get_wsgi_application(), host="0.0.0.0", port=port)
