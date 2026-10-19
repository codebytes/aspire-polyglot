FROM node:26-bookworm-slim AS node
FROM golang:1.27.2-bookworm AS go
FROM eclipse-temurin:25-jdk AS java
FROM docker:29-cli AS docker
FROM mcr.microsoft.com/dotnet/sdk:10.0

COPY --from=node /usr/local/bin/node /usr/local/bin/node
COPY --from=node /usr/local/lib/node_modules /usr/local/lib/node_modules
COPY --from=go /usr/local/go /usr/local/go
COPY --from=java /opt/java/openjdk /opt/java/openjdk
COPY --from=docker /usr/local/bin/docker /usr/local/bin/docker
COPY --from=docker /usr/local/libexec/docker/cli-plugins /usr/local/libexec/docker/cli-plugins

ENV JAVA_HOME=/opt/java/openjdk \
    PATH=/opt/aspire:/usr/local/go/bin:/opt/java/openjdk/bin:$PATH \
    DOTNET_CLI_TELEMETRY_OPTOUT=1 \
    DOTNET_NOLOGO=true

RUN ln -s /usr/local/lib/node_modules/npm/bin/npm-cli.js /usr/local/bin/npm \
    && ln -s /usr/local/lib/node_modules/npm/bin/npx-cli.js /usr/local/bin/npx

WORKDIR /workspace
COPY samples/preview/svelte-go-bookmarks/NuGet.config /tmp/NuGet.config
RUN dotnet tool install Aspire.Cli --version 13.6.1 --tool-path /opt/aspire --configfile /tmp/NuGet.config \
    && rm /tmp/NuGet.config

COPY scripts/smoke-samples.mjs scripts/
COPY samples/preview/svelte-go-bookmarks samples/preview/svelte-go-bookmarks/
COPY samples/preview/spring-boot-postgres samples/preview/spring-boot-postgres/

ENV ASPNETCORE_ENVIRONMENT=Development \
    DOTNET_ENVIRONMENT=Development \
    ASPNETCORE_URLS=http://127.0.0.1:18888 \
    ASPIRE_DASHBOARD_OTLP_ENDPOINT_URL=http://0.0.0.0:4317 \
    ASPIRE_DASHBOARD_OTLP_HTTP_ENDPOINT_URL=http://0.0.0.0:4318 \
    ASPIRE_RESOURCE_SERVICE_ENDPOINT_URL=http://127.0.0.1:18889 \
    ASPIRE_ALLOW_UNSECURED_TRANSPORT=true \
    ASPIRE_ENABLE_CONTAINER_TUNNEL=false \
    AppHost__ContainerHostname=10.254.0.1 \
    ASPIRE_SMOKE_PRIVATE_DAEMON=1

ENTRYPOINT ["node", "--use-system-ca", "scripts/smoke-samples.mjs"]
CMD ["preview/svelte-go-bookmarks", "preview/spring-boot-postgres"]
