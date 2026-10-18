import logging
import os
from pathlib import Path
import ssl


def configure_telemetry(app):
    if not os.environ.get("OTEL_EXPORTER_OTLP_ENDPOINT"):
        return

    import grpc
    from opentelemetry import metrics, trace
    from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
    from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
    from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
    from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor

    # gRPC uses its own CA bundle rather than Python's SSL_CERT_DIR.
    certificates = []
    ca_file = ssl.get_default_verify_paths().cafile
    if ca_file:
        certificates.append(Path(ca_file).read_bytes())
    for directory in os.environ.get("SSL_CERT_DIR", "").split(os.pathsep):
        if "aspire" in directory.lower():
            certificates.extend(path.read_bytes() for path in sorted(Path(directory).glob("*.pem")))
    options = {}
    if certificates:
        options["credentials"] = grpc.ssl_channel_credentials(b"\n".join(certificates))

    resource = Resource.create({"service.name": os.environ.get("OTEL_SERVICE_NAME", "text-analyzer")})
    traces = TracerProvider(resource=resource)
    traces.add_span_processor(BatchSpanProcessor(OTLPSpanExporter(**options)))
    trace.set_tracer_provider(traces)
    metrics.set_meter_provider(MeterProvider(resource=resource, metric_readers=[
        PeriodicExportingMetricReader(OTLPMetricExporter(**options)),
    ]))
    logs = LoggerProvider(resource=resource)
    logs.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter(**options)))
    logging.getLogger().addHandler(LoggingHandler(logger_provider=logs))
    logging.getLogger().setLevel(logging.INFO)
    FastAPIInstrumentor.instrument_app(app)
