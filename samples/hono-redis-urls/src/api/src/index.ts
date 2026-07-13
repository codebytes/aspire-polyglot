import "./instrumentation.ts";
import { serve } from "@hono/node-server";
import { Hono } from "hono";
import { cors } from "hono/cors";
import { Redis } from "ioredis";
import { customAlphabet } from "nanoid";

const port = Number(process.env.PORT ?? 8080);

// Aspire injects REDIS_HOST / REDIS_PORT (see apphost.ts). In-container the
// hostname "cache" resolves to the Redis container on the shared network.
const redis = new Redis({
  host: process.env.REDIS_HOST ?? "localhost",
  port: Number(process.env.REDIS_PORT ?? 6379),
});

const newCode = customAlphabet("0123456789abcdefghijklmnopqrstuvwxyz", 7);

const URLS_KEY = "urls";
const urlKey = (code: string) => `url:${code}`;

interface ShortUrl {
  code: string;
  url: string;
  clicks: number;
  createdAt: string;
}

const app = new Hono();
app.use("*", cors());

app.get("/api/health", (c) => c.json({ status: "healthy" }));

// Create a short code for a URL.
app.post("/api/shorten", async (c) => {
  const body = await c.req.json<{ url?: string }>().catch(() => ({} as { url?: string }));
  const url = body.url?.trim();

  if (!url || !/^https?:\/\/.+/i.test(url)) {
    return c.json({ error: "Provide a valid http(s) URL." }, 400);
  }

  const code = newCode();
  const createdAt = new Date().toISOString();
  await redis.hset(urlKey(code), { url, clicks: "0", createdAt });
  await redis.lpush(URLS_KEY, code);
  console.log(`Shortened ${url} -> ${code}`);

  return c.json({ code, url, clicks: 0, createdAt } satisfies ShortUrl, 201);
});

// List the most recent short URLs.
app.get("/api/urls", async (c) => {
  const codes = await redis.lrange(URLS_KEY, 0, 49);
  const urls: ShortUrl[] = [];

  for (const code of codes) {
    const data = await redis.hgetall(urlKey(code));
    if (data.url) {
      urls.push({
        code,
        url: data.url,
        clicks: Number(data.clicks ?? 0),
        createdAt: data.createdAt ?? "",
      });
    }
  }

  return c.json({ urls });
});

// Follow a short code: increment its click count and redirect.
// Uses an "/r/" prefix so the web (Vite) server can proxy it to this API
// without colliding with the SPA's own client-side routes.
app.get("/r/:code", async (c) => {
  const code = c.req.param("code");
  const data = await redis.hgetall(urlKey(code));

  if (!data.url) {
    return c.text("Short link not found", 404);
  }

  await redis.hincrby(urlKey(code), "clicks", 1);
  return c.redirect(data.url, 302);
});

serve({ fetch: app.fetch, hostname: "0.0.0.0", port }, () => {
  console.log(`URL shortener API listening on port ${port}`);
});
