"""Python OpenTelemetry emitter for the standalone Aspire dashboard demo.

No AppHost, no framework: just the OTEL SDK pointed at the OTLP endpoint Aspire
exposes. Config comes from the standard env vars:
  OTEL_EXPORTER_OTLP_ENDPOINT, OTEL_EXPORTER_OTLP_PROTOCOL, OTEL_SERVICE_NAME
"""
import logging
import os
import random
import time

from opentelemetry import metrics, trace
from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry._logs import set_logger_provider
from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.trace import Status, StatusCode

SERVICE = os.environ.get("OTEL_SERVICE_NAME", "python-worker")
resource = Resource.create({"service.name": SERVICE})

# ---- Traces ----
tracer_provider = TracerProvider(resource=resource)
tracer_provider.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))
trace.set_tracer_provider(tracer_provider)
tracer = trace.get_tracer(SERVICE)

# ---- Metrics ----
reader = PeriodicExportingMetricReader(OTLPMetricExporter(), export_interval_millis=5000)
metrics.set_meter_provider(MeterProvider(resource=resource, metric_readers=[reader]))
meter = metrics.get_meter(SERVICE)
jobs_processed = meter.create_counter("jobs.processed", description="Number of jobs processed")
job_duration = meter.create_histogram("job.duration", unit="ms", description="Job processing time")

# ---- Logs (bridge Python logging -> OTEL) ----
logger_provider = LoggerProvider(resource=resource)
logger_provider.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter()))
set_logger_provider(logger_provider)
logging.basicConfig(
    level=logging.INFO,
    handlers=[LoggingHandler(level=logging.INFO, logger_provider=logger_provider), logging.StreamHandler()],
)
log = logging.getLogger(SERVICE)

REGIONS = ["us-east", "us-west", "eu-central", "ap-south"]


def process_job(job_id: int) -> None:
    """One unit of work: a parent span with two children, a metric, and a log."""
    region = random.choice(REGIONS)
    start = time.time()
    with tracer.start_as_current_span("process-job") as span:
        span.set_attribute("job.id", job_id)
        span.set_attribute("job.region", region)

        with tracer.start_as_current_span("fetch-records") as child:
            time.sleep(0.03 + random.random() * 0.12)
            child.set_attribute("records.count", random.randint(0, 500))

        with tracer.start_as_current_span("transform") as child:
            time.sleep(0.02 + random.random() * 0.08)
            # ~1 in 8 jobs fails, so the dashboard shows error traces too.
            if random.random() < 0.12:
                child.set_status(Status(StatusCode.ERROR, "transform failed: schema mismatch"))
                span.set_status(Status(StatusCode.ERROR))
                log.error("Job %s failed in %s", job_id, region)

    elapsed_ms = (time.time() - start) * 1000
    jobs_processed.add(1, {"region": region})
    job_duration.record(elapsed_ms, {"region": region})
    log.info("Processed job %s in %s (%.0fms)", job_id, region, elapsed_ms)


if __name__ == "__main__":
    print(f"[python-worker] emitting telemetry to {os.environ.get('OTEL_EXPORTER_OTLP_ENDPOINT')}", flush=True)
    counter = 0
    while True:
        counter += 1
        process_job(counter)
        time.sleep(2)
