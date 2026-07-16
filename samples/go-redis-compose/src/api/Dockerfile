FROM golang:1.23-alpine AS build
WORKDIR /src
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 go build -o /out/api .

FROM alpine:3.20
WORKDIR /app
COPY --from=build /out/api /app/api
EXPOSE 8080
CMD ["/app/api"]
