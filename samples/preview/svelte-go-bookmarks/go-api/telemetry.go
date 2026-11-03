package main

import (
	"context"
	"errors"
	"log/slog"
	"os"

	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/contrib/exporters/autoexport"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/log/global"
	"go.opentelemetry.io/otel/propagation"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
)

// setupTelemetry sends traces, metrics, and logs to the OTLP endpoint Aspire
// injects. autoexport reads the standard OTEL_* variables (endpoint, protocol,
// headers, certificate, service name), so nothing here is Aspire-specific.
// Logs are always written to stdout as well, for the dashboard's console view.
func setupTelemetry(ctx context.Context) (func(context.Context) error, error) {
	console := slog.NewTextHandler(os.Stdout, nil)
	if os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT") == "" {
		slog.SetDefault(slog.New(console))
		return func(context.Context) error { return nil }, nil
	}

	spans, err := autoexport.NewSpanExporter(ctx)
	if err != nil {
		return nil, err
	}
	metrics, err := autoexport.NewMetricReader(ctx)
	if err != nil {
		return nil, err
	}
	logs, err := autoexport.NewLogExporter(ctx)
	if err != nil {
		return nil, err
	}

	tracerProvider := sdktrace.NewTracerProvider(sdktrace.WithBatcher(spans))
	meterProvider := sdkmetric.NewMeterProvider(sdkmetric.WithReader(metrics))
	loggerProvider := sdklog.NewLoggerProvider(sdklog.WithProcessor(sdklog.NewBatchProcessor(logs)))

	otel.SetTracerProvider(tracerProvider)
	otel.SetMeterProvider(meterProvider)
	otel.SetTextMapPropagator(propagation.TraceContext{})
	global.SetLoggerProvider(loggerProvider)

	// Every slog call goes to stdout and, with trace correlation, to the dashboard.
	slog.SetDefault(slog.New(slog.NewMultiHandler(console, otelslog.NewHandler("bookmarks-api"))))

	return func(ctx context.Context) error {
		return errors.Join(tracerProvider.Shutdown(ctx), meterProvider.Shutdown(ctx), loggerProvider.Shutdown(ctx))
	}, nil
}
