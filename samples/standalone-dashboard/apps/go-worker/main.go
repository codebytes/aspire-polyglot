// Go OpenTelemetry emitter for the standalone Aspire dashboard demo.
//
// No AppHost, no framework: just the OTEL SDK pointed at the OTLP endpoint
// Aspire exposes. Config comes from the standard env vars that the OTLP
// exporters read automatically:
//   OTEL_EXPORTER_OTLP_ENDPOINT, OTEL_EXPORTER_OTLP_PROTOCOL, OTEL_SERVICE_NAME
package main

import (
	"context"
	"log"
	"math/rand"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/codes"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploggrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/log/global"
	"go.opentelemetry.io/otel/metric"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.26.0"
)

var regions = []string{"us-east", "us-west", "eu-central", "ap-south"}

func main() {
	ctx := context.Background()

	service := os.Getenv("OTEL_SERVICE_NAME")
	if service == "" {
		service = "go-worker"
	}
	res, err := resource.New(ctx, resource.WithAttributes(semconv.ServiceName(service)))
	if err != nil {
		panic(err)
	}

	// ---- Traces ----
	traceExp, err := otlptracegrpc.New(ctx)
	if err != nil {
		panic(err)
	}
	tp := sdktrace.NewTracerProvider(sdktrace.WithResource(res), sdktrace.WithBatcher(traceExp))
	otel.SetTracerProvider(tp)
	tracer := tp.Tracer(service)

	// ---- Metrics ----
	metricExp, err := otlpmetricgrpc.New(ctx)
	if err != nil {
		panic(err)
	}
	mp := sdkmetric.NewMeterProvider(
		sdkmetric.WithResource(res),
		sdkmetric.WithReader(sdkmetric.NewPeriodicReader(metricExp, sdkmetric.WithInterval(5*time.Second))),
	)
	otel.SetMeterProvider(mp)
	meter := mp.Meter(service)
	jobsProcessed, _ := meter.Int64Counter("jobs.processed", metric.WithDescription("Number of jobs processed"))
	jobDuration, _ := meter.Float64Histogram("job.duration", metric.WithUnit("ms"), metric.WithDescription("Job processing time"))

	// ---- Logs (slog bridge -> OTEL) ----
	logExp, err := otlploggrpc.New(ctx)
	if err != nil {
		panic(err)
	}
	lp := sdklog.NewLoggerProvider(sdklog.WithResource(res), sdklog.WithProcessor(sdklog.NewBatchProcessor(logExp)))
	global.SetLoggerProvider(lp)
	logger := otelslog.NewLogger(service)

	// Clean shutdown flushes any buffered telemetry.
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)
	defer func() {
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		_ = tp.Shutdown(shutdownCtx)
		_ = mp.Shutdown(shutdownCtx)
		_ = lp.Shutdown(shutdownCtx)
	}()

	logger.Info("emitting telemetry", "endpoint", os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT"))
	log.Printf("[go-worker] emitting telemetry to %s", os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT"))

	ticker := time.NewTicker(2 * time.Second)
	defer ticker.Stop()
	jobID := 0
	for {
		select {
		case <-stop:
			return
		case <-ticker.C:
			jobID++
			region := regions[rand.Intn(len(regions))]
			start := time.Now()

			// Parent span with two child spans.
			jobCtx, span := tracer.Start(ctx, "process-job")
			span.SetAttributes(attribute.Int("job.id", jobID), attribute.String("job.region", region))

			_, fetch := tracer.Start(jobCtx, "fetch-records")
			time.Sleep(time.Duration(30+rand.Intn(120)) * time.Millisecond)
			fetch.SetAttributes(attribute.Int("records.count", rand.Intn(500)))
			fetch.End()

			_, transform := tracer.Start(jobCtx, "transform")
			time.Sleep(time.Duration(20+rand.Intn(80)) * time.Millisecond)
			// ~1 in 8 jobs fails, so the dashboard shows error traces too.
			failed := rand.Float64() < 0.12
			if failed {
				transform.SetStatus(codes.Error, "transform failed: schema mismatch")
				span.SetStatus(codes.Error, "job failed")
			}
			transform.End()
			span.End()

			elapsedMs := float64(time.Since(start).Microseconds()) / 1000.0
			jobsProcessed.Add(ctx, 1, metric.WithAttributes(attribute.String("region", region)))
			jobDuration.Record(ctx, elapsedMs, metric.WithAttributes(attribute.String("region", region)))

			if failed {
				logger.Error("job failed", "job.id", jobID, "job.region", region)
				log.Printf("[go-worker] job %d in %s FAILED (%.0fms)", jobID, region, elapsedMs)
			} else {
				logger.Info("processed job", "job.id", jobID, "job.region", region, "duration.ms", elapsedMs)
				log.Printf("[go-worker] job %d in %s took %.0fms", jobID, region, elapsedMs)
			}
		}
	}
}
