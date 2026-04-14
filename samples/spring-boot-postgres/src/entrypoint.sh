#!/bin/sh
# Import Aspire dev certificates into JRE truststore
if [ -n "$SSL_CERT_DIR" ]; then
  IFS=':'
  for dir in $SSL_CERT_DIR; do
    case "$dir" in *aspire*|*Aspire*)
      for pem in "$dir"/*.pem; do
        [ -f "$pem" ] || continue
        alias="aspire-$(basename "$pem" .pem)"
        keytool -importcert -cacerts -storepass changeit \
          -noprompt -alias "$alias" -file "$pem" 2>/dev/null || true
      done
    ;; esac
  done
fi

exec java -javaagent:/app/opentelemetry-javaagent.jar -jar /app/app.jar "$@"
