// Preloaded by `npm start` (node --import ./src/otel.ts). The OpenTelemetry SDK
// reads Aspire's OTEL_* settings (endpoint, protocol, headers, service name) and
// exports traces, metrics, and logs. The loader hook lets the instrumentations
// below patch ES module imports.
import { register } from "node:module";
import { NodeSDK } from "@opentelemetry/sdk-node";
import { HttpInstrumentation } from "@opentelemetry/instrumentation-http";
import { IORedisInstrumentation } from "@opentelemetry/instrumentation-ioredis";
import { PinoInstrumentation } from "@opentelemetry/instrumentation-pino";

register("@opentelemetry/instrumentation/hook.mjs", import.meta.url);

const sdk = new NodeSDK({
  instrumentations: [
    new HttpInstrumentation(), // a server span per request
    new IORedisInstrumentation(), // a child span per Redis command
    new PinoInstrumentation(), // pino records -> OTLP logs with trace context
  ],
});
sdk.start();
process.on("SIGTERM", () => sdk.shutdown().finally(() => process.exit(0)));
