# Go OTEL emitter for the standalone Aspire dashboard demo (multi-stage build).
FROM golang:1.25-alpine AS build
WORKDIR /src
COPY go.mod go.sum ./
RUN go mod download
COPY main.go ./
RUN CGO_ENABLED=0 GOOS=linux go build -o /out/worker .

FROM alpine:3.20
RUN adduser -D -u 10001 app
USER app
COPY --from=build /out/worker /worker
ENTRYPOINT ["/worker"]
