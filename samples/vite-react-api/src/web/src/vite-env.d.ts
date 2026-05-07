/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_OTEL_EXPORTER_OTLP_ENDPOINT?: string;
  readonly VITE_OTEL_EXPORTER_OTLP_HEADERS?: string;
  readonly VITE_OTEL_SERVICE_NAME?: string;
  readonly VITE_OTEL_RESOURCE_ATTRIBUTES?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
