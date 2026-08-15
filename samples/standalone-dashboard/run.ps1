<#
.SYNOPSIS
  Show off the STANDALONE Aspire dashboard — no AppHost, no .NET.

.DESCRIPTION
  Builds and starts the Aspire dashboard container plus three polyglot
  OpenTelemetry emitters (Node.js, Python, Go), then opens the dashboard.
  Watch Structured Logs, Traces, and Metrics fill up with data from all
  three languages.

  Tear down with:  docker compose down
#>
[CmdletBinding()]
param()

$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

Write-Host "Starting standalone Aspire dashboard + polyglot OTEL workers..." -ForegroundColor Cyan
docker compose up --build -d

Write-Host @"

  Aspire dashboard (standalone):  http://localhost:18888
  OTLP/gRPC endpoint:             http://localhost:4317
  OTLP/HTTP endpoint:             http://localhost:4318

  Emitting telemetry: node-worker, python-worker, go-worker
  Follow logs:  docker compose logs -f
  Tear down:    docker compose down
"@ -ForegroundColor Green

Start-Process "http://localhost:18888"
