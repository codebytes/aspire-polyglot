FROM golang:1.27.2-alpine AS build
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY main.go ./
RUN go build -o server .

FROM alpine:3.24.2
COPY --from=build /app/server /server
EXPOSE 8080
CMD ["/server"]
