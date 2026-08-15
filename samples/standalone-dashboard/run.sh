#!/usr/bin/env bash
# One command to show off the STANDALONE Aspire dashboard — no AppHost, no .NET.
#
# Builds and starts:
#   - the Aspire dashboard container (UI + OTLP ingestion)
#   - three polyglot OTEL emitters (Node.js, Python, Go)
#
# Then open http://localhost:18888 and watch Structured Logs, Traces, and Metrics
# fill up with data from all three languages.
set -euo pipefail
cd "$(dirname "$0")"

echo "Starting standalone Aspire dashboard + polyglot OTEL workers..."
docker compose up --build -d

cat <<'EOF'

  Aspire dashboard (standalone):  http://localhost:18888
  OTLP/gRPC endpoint:             http://localhost:4317
  OTLP/HTTP endpoint:             http://localhost:4318

  Emitting telemetry: node-worker, python-worker, go-worker
  Follow logs:  docker compose logs -f
  Tear down:    docker compose down
EOF

# Best-effort: open the dashboard in the default browser.
( command -v xdg-open >/dev/null && xdg-open http://localhost:18888 ) 2>/dev/null || \
( command -v open >/dev/null && open http://localhost:18888 ) 2>/dev/null || true
