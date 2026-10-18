#!/bin/sh
set -eu

# The agent has its own TLS configuration, separate from the JRE truststore.
if [ -n "${SSL_CERT_DIR:-}" ]; then
  IFS=':'
  for directory in $SSL_CERT_DIR; do
    case "$directory" in
      *aspire*|*Aspire*)
        for pem in "$directory"/*.pem; do
          [ -f "$pem" ] || continue
          cat "$pem" >> /tmp/aspire-otel-ca.pem
        done
        ;;
    esac
  done
  if [ -s /tmp/aspire-otel-ca.pem ]; then
    export OTEL_EXPORTER_OTLP_CERTIFICATE=/tmp/aspire-otel-ca.pem
  fi
fi

exec java -javaagent:/app/opentelemetry-javaagent.jar -jar /app/app.jar "$@"
