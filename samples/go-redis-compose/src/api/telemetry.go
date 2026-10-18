package main

import (
	"context"
	"errors"
	"log"
	"log/slog"
	"os"

	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploggrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/propagation"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
)

func initTelemetry(ctx context.Context) (func(context.Context) error, error) {
	if os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT") == "" {
		return func(context.Context) error { return nil }, nil
	}
	res, err := resource.Merge(resource.Default(), resource.NewSchemaless(
		attribute.String("service.name", envOr("OTEL_SERVICE_NAME", "hitcounter")),
	))
	if err != nil {
		return nil, err
	}
	var shutdowns []func(context.Context) error
	shutdown := func(ctx context.Context) error {
		var failures []error
		for _, stop := range shutdowns {
			failures = append(failures, stop(ctx))
		}
		return errors.Join(failures...)
	}
	traceExporter, err := otlptracegrpc.New(ctx)
	if err != nil {
		return nil, err
	}
	tp := sdktrace.NewTracerProvider(sdktrace.WithResource(res), sdktrace.WithBatcher(traceExporter))
	shutdowns = append(shutdowns, tp.Shutdown)
	otel.SetTracerProvider(tp)
	otel.SetTextMapPropagator(propagation.TraceContext{})

	metricExporter, err := otlpmetricgrpc.New(ctx)
	if err != nil {
		return nil, errors.Join(err, shutdown(ctx))
	}
	mp := sdkmetric.NewMeterProvider(sdkmetric.WithResource(res),
		sdkmetric.WithReader(sdkmetric.NewPeriodicReader(metricExporter)))
	shutdowns = append(shutdowns, mp.Shutdown)
	otel.SetMeterProvider(mp)

	logExporter, err := otlploggrpc.New(ctx)
	if err != nil {
		return nil, errors.Join(err, shutdown(ctx))
	}
	lp := sdklog.NewLoggerProvider(sdklog.WithResource(res),
		sdklog.WithProcessor(sdklog.NewBatchProcessor(logExporter)))
	shutdowns = append(shutdowns, lp.Shutdown)
	slog.SetDefault(slog.New(otelslog.NewHandler("hitcounter", otelslog.WithLoggerProvider(lp))))
	// Keep server and shutdown errors on stderr, even after the OTel provider closes.
	log.SetOutput(os.Stderr)
	return shutdown, nil
}
