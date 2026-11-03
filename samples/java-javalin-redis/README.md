# Java (Javalin) + Redis leaderboard

A single-file C# AppHost runs a [Javalin](https://javalin.io/) leaderboard API as
a **native JVM process** next to a Redis container. Aspire builds the JAR with the
project's Maven wrapper and starts it with the **OpenTelemetry Java agent**, so
traces, metrics, and logs reach the dashboard with zero telemetry code.

| Resource | Runs as | Integration |
|----------|---------|-------------|
| `api` | native `java -jar` (Java 21) | `AddJavaApp` (`Aspire.Hosting.Java`, preview) |
| `api-maven-build` | `./mvnw package` before `api` starts | `WithMavenBuild` |
| `cache` | Redis container | `AddRedis` |

```csharp
builder.AddJavaApp("api", "src/api", "target/leaderboard.jar")
    .WithMavenBuild("package", "-DskipTests")
    .WithOtelAgent()
    .WithReference(cache)
    .WaitFor(cache)
    .WithHttpEndpoint(env: "PORT");
```

## Run

Prerequisites: Aspire CLI 13.6.4, .NET 10 SDK, JDK 21+, Docker. Maven is not
required: `src/api/mvnw` downloads it on first use.

```bash
cd samples/java-javalin-redis
aspire run
```

Open the `api` URL and submit a few scores. In the dashboard:

- **Structured logs**: `Recorded score 300.0 for Dave` from SLF4J/Logback.
- **Traces**: each request with a child span for the Redis `ZADD`/`ZREVRANGE`.
- **Metrics**: HTTP server and JVM runtime metrics from the agent.

Or use curl against the `api` endpoint:

```bash
curl -X POST "$API/api/scores" -H 'Content-Type: application/json' -d '{"player":"Dave","score":300}'
curl "$API/api/leaderboard"
```

## How it is wired

- `WithMavenBuild` adds a build resource the API waits for. The POM copies
  `opentelemetry-javaagent.jar` to `target/agent/`, where `WithOtelAgent()` expects it.
- `WithReference(cache)` injects `CACHE_URI`; the app passes it to `RedisClient.create(URI)`.
- Aspire also gives the JVM a trust store with its development certificate, so the
  agent exports over TLS without any entrypoint scripts.

## Tests

```bash
cd src/api
./mvnw verify
```

Tests exercise the HTTP API with a mocked Redis client; no Redis or Docker needed.

## Layout

```text
apphost.cs                                      single-file C# AppHost
src/api/pom.xml                                 Javalin 7, Jedis 8, Jackson 3, OTel agent copy
src/api/mvnw                                    Maven wrapper used by WithMavenBuild
src/api/src/main/java/com/aspire/polyglot/App.java
src/api/src/main/resources/logback.xml          INFO-level console logging
```
