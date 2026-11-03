// Preloaded by `npm start` (node --import ./src/otel.ts). The OpenTelemetry SDK
// reads Aspire's OTEL_* settings (endpoint, protocol, headers, service name) and
// exports traces, metrics, and logs. The loader hook lets it instrument ES module
// imports too, such as ioredis and pino.
import { register } from "node:module";
import { NodeSDK } from "@opentelemetry/sdk-node";
import { getNodeAutoInstrumentations } from "@opentelemetry/auto-instrumentations-node";

register("@opentelemetry/instrumentation/hook.mjs", import.meta.url);

const sdk = new NodeSDK({
  instrumentations: getNodeAutoInstrumentations({
    // Keep traces focused on requests and Redis rather than socket internals.
    "@opentelemetry/instrumentation-dns": { enabled: false },
    "@opentelemetry/instrumentation-net": { enabled: false },
  }),
});
sdk.start();
process.on("SIGTERM", () => sdk.shutdown().finally(() => process.exit(0)));
