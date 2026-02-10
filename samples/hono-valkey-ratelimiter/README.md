# Hono + Valkey — API Rate Limiter

Sliding-window rate limiter using [Hono](https://hono.dev) and [Valkey](https://valkey.io), orchestrated by .NET Aspire.

## What Makes This Different
- **Hono** — lightweight, fast web framework (not Express)
- **Valkey** — open-source Redis fork (not Redis)
- **Rate limiting** — production pattern with sliding windows

## Architecture

This sample demonstrates:
- **Sliding-window rate limiting**: Each IP address gets 10 requests per 60-second window
- **Valkey integration**: Uses sorted sets for efficient time-window tracking
- **Rate limit headers**: Standard HTTP headers (X-RateLimit-*) on every response
- **Graceful degradation**: Rate limiter errors don't block the API

## Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /` | API information and available endpoints |
| `GET /api/quote` | Random inspirational quote |
| `GET /api/status` | Current rate limit status for your IP |
| `GET /api/quotes/random/:count` | N random quotes (max 5) |
| `GET /health` | Health check (no rate limit) |

## Run

```bash
cd AppHost && dotnet run
```

The Aspire dashboard will open automatically. Click on the `api` endpoint to access the API.

## Test Rate Limiting

Make rapid requests to trigger the rate limiter:

```bash
# Make 12 requests (will hit the 10-request limit)
for i in $(seq 1 12); do 
  curl -s http://localhost:<port>/api/quote | jq
  sleep 0.5
done
```

When rate limited, you'll see a 429 response:

```json
{
  "error": "Too many requests",
  "retryAfter": 45
}
```

Check your current rate limit status:

```bash
curl http://localhost:<port>/api/status | jq
```

Response:
```json
{
  "ip": "::1",
  "remaining": 7,
  "resetIn": 60,
  "limit": 10
}
```

## Rate Limit Headers

Every API response includes standard rate limit headers:

```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 5
X-RateLimit-Reset: 1707598234
Retry-After: 30
```

## How It Works

### Sliding Window Algorithm

The rate limiter uses a **sorted set** in Valkey to track requests:

1. **Key structure**: `ratelimit:{ip_address}`
2. **Score**: Unix timestamp in milliseconds
3. **Value**: Unique request identifier

For each request:
1. Remove entries older than the 60-second window
2. Count remaining entries in the window
3. If count ≥ 10, reject with 429
4. Otherwise, add the current request and continue

This provides **true sliding windows** (not fixed buckets) for accurate rate limiting.

### Valkey Commands Used

- `ZREMRANGEBYSCORE` — Remove old entries outside the time window
- `ZCARD` — Count current requests in the window
- `ZADD` — Add new request timestamp
- `EXPIRE` — Auto-cleanup after window expires
- `ZRANGE` — Get oldest entry for retry calculation

## Aspire Patterns

- **`AddValkey()`** — Valkey container resource (Redis-compatible)
- **`AddNpmApp()`** — Node.js application with Hono
- **`WithReference(valkey)`** — Injects connection string as `CONNECTIONSTRINGS__CACHE`
- **`WithHttpEndpoint(env: "PORT")`** — Dynamic port binding
- **`WithExternalHttpEndpoints()`** — Expose to host machine

## Dependencies

### AppHost (C#)
- Aspire.Hosting.NodeJs 9.2.1
- Aspire.Hosting.Valkey 9.2.1

### API (Node.js)
- hono — Web framework
- @hono/node-server — Node.js adapter for Hono
- ioredis — Valkey/Redis client

## Why Hono?

Hono is a modern, lightweight web framework that's:
- **Fast** — Optimized for edge/serverless but works everywhere
- **Small** — Tiny bundle size
- **TypeScript-first** — Great DX with full type safety
- **Standard** — Uses Web Standard APIs
- **Flexible** — Works on Node.js, Deno, Bun, Cloudflare Workers, etc.

## Why Valkey?

Valkey is a high-performance data structure server that's:
- **Redis-compatible** — Drop-in replacement for Redis
- **Open source** — Linux Foundation project
- **Community-driven** — No licensing concerns
- **Actively developed** — Regular updates and improvements

Perfect for caching, rate limiting, and real-time features in modern applications.

## Production Considerations

For production deployments:
1. **Use managed Valkey/Redis** — Azure Cache for Redis, AWS ElastiCache, etc.
2. **Distributed rate limiting** — Ensure all API instances share the same Valkey
3. **Configure limits per tier** — Different limits for authenticated vs. anonymous users
4. **Monitor**: Track 429 responses to tune limits
5. **Implement token buckets** — For burst handling alongside sliding windows
6. **Add IP allowlists** — Bypass limits for trusted IPs/services

## Learn More

- [Hono Documentation](https://hono.dev)
- [Valkey Documentation](https://valkey.io)
- [.NET Aspire](https://learn.microsoft.com/dotnet/aspire)
- [Rate Limiting Patterns](https://cloud.google.com/architecture/rate-limiting-strategies-techniques)
