import { createBuilder, OtlpProtocol } from './.modules/aspire.js';

const builder = await createBuilder();

// Run the Express API and expose its HTTP endpoint externally.
const app = await builder
    .addNodeApp("app", "./api", "src/index.ts")
    .withHttpEndpoint({ env: "PORT" })
    .withExternalHttpEndpoints();

// Run the Vite frontend after the API and inject the API URL for local proxying.
//
// `addViteApp` automatically injects OTEL_EXPORTER_OTLP_ENDPOINT and friends
// pointing at the dashboard's gRPC listener. Browsers can't speak gRPC, so we
// flip the protocol to HTTP/protobuf via `withOtlpExporter({ protocol: ... })`.
// The vite dev server then sees `OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf` and
// an HTTP/protobuf endpoint URL, which `vite.config.ts` re-exports as VITE_OTEL_*
// so `import.meta.env` exposes the values to the browser SDK.
const frontend = await builder
    .addViteApp("frontend", "./frontend")
    .withOtlpExporter({ protocol: OtlpProtocol.HttpProtobuf })
    .withReference(app)
    .waitFor(app);

// Bundle the frontend build output into the API container for publish/deploy.
await app.publishWithContainerFiles(frontend, "./static");

await builder.build().run();
