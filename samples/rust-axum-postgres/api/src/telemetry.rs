//! OpenTelemetry setup. Aspire injects the standard OTEL_* variables (endpoint,
//! protocol, headers, service name) and adds its development certificate to
//! SSL_CERT_DIR, which rustls reads when native roots are enabled.

use opentelemetry::{global, trace::TracerProvider as _};
use opentelemetry_appender_tracing::layer::OpenTelemetryTracingBridge;
use opentelemetry_otlp::{
    LogExporter, MetricExporter, SpanExporter, WithTonicConfig,
    tonic_types::transport::ClientTlsConfig,
};
use opentelemetry_sdk::{
    logs::SdkLoggerProvider, metrics::SdkMeterProvider, propagation::TraceContextPropagator,
    trace::SdkTracerProvider,
};
use tracing_subscriber::{EnvFilter, Layer, layer::SubscriberExt, util::SubscriberInitExt};

/// Keeps the providers alive; `shutdown` flushes buffered telemetry.
#[derive(Default)]
pub struct Telemetry {
    providers: Option<(SdkTracerProvider, SdkMeterProvider, SdkLoggerProvider)>,
}

impl Telemetry {
    pub fn shutdown(self) {
        if let Some((tracer, meter, logger)) = self.providers {
            let _ = tracer.shutdown();
            let _ = meter.shutdown();
            let _ = logger.shutdown();
        }
    }
}

pub fn init() -> Result<Telemetry, Box<dyn std::error::Error>> {
    // The exporters' own HTTP/2 stack must not log into the exporter.
    let filter = || EnvFilter::new("info,h2=warn,hyper=warn,tonic=warn,tower=warn");
    let console = tracing_subscriber::fmt::layer().with_filter(filter());

    if std::env::var_os("OTEL_EXPORTER_OTLP_ENDPOINT").is_none() {
        tracing_subscriber::registry().with(console).init();
        return Ok(Telemetry::default());
    }

    // Trust the platform roots (including SSL_CERT_DIR) for https:// endpoints.
    let tls = || ClientTlsConfig::new().with_native_roots();
    let tracer = SdkTracerProvider::builder()
        .with_batch_exporter(
            SpanExporter::builder()
                .with_tonic()
                .with_tls_config(tls())
                .build()?,
        )
        .build();
    let meter = SdkMeterProvider::builder()
        .with_periodic_exporter(
            MetricExporter::builder()
                .with_tonic()
                .with_tls_config(tls())
                .build()?,
        )
        .build();
    let logger = SdkLoggerProvider::builder()
        .with_batch_exporter(
            LogExporter::builder()
                .with_tonic()
                .with_tls_config(tls())
                .build()?,
        )
        .build();

    global::set_text_map_propagator(TraceContextPropagator::new());
    global::set_meter_provider(meter.clone());

    tracing_subscriber::registry()
        .with(console)
        .with(
            tracing_opentelemetry::layer()
                .with_tracer(tracer.tracer("guestbook"))
                .with_filter(filter()),
        )
        .with(OpenTelemetryTracingBridge::new(&logger).with_filter(filter()))
        .init();

    Ok(Telemetry {
        providers: Some((tracer, meter, logger)),
    })
}
