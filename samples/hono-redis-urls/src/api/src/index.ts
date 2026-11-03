// URL shortener API: Hono + Redis.
//
// Aspire runs this natively with Node.js (addNodeApp) and injects:
//   PORT       the port to listen on
//   CACHE_URI  redis[s]://:password@host:port for the "cache" resource
//   OTEL_*     settings for the OpenTelemetry instrumentation preloaded by `npm start`
import { serve } from "@hono/node-server";
import { Hono } from "hono";
import { Redis } from "ioredis";
import { customAlphabet } from "nanoid";
import { pino } from "pino";

// pino records become dashboard structured logs, linked to the active trace.
const log = pino();
const redis = new Redis(process.env.CACHE_URI ?? "redis://localhost:6379");
const newCode = customAlphabet("0123456789abcdefghijklmnopqrstuvwxyz", 7);

const URLS_KEY = "urls";
const urlKey = (code: string) => `url:${code}`;

const app = new Hono();

app.get("/health", async (c) => c.text((await redis.ping()) === "PONG" ? "Healthy" : "Unhealthy"));

// Create a short code for a URL.
app.post("/api/shorten", async (c) => {
  const body = await c.req.json<{ url?: string }>().catch(() => ({ url: undefined }));
  const url = body.url?.trim();
  if (!url || !/^https?:\/\/.+/i.test(url)) {
    log.warn({ url }, "rejected invalid URL");
    return c.json({ error: "Provide a valid http(s) URL." }, 400);
  }

  const code = newCode();
  const createdAt = new Date().toISOString();
  await redis.multi().hset(urlKey(code), { url, clicks: 0, createdAt }).lpush(URLS_KEY, code).exec();
  log.info({ code, url }, "shortened URL");
  return c.json({ code, url, clicks: 0, createdAt }, 201);
});

// The 50 most recent short URLs.
app.get("/api/urls", async (c) => {
  const codes = await redis.lrange(URLS_KEY, 0, 49);
  const pipeline = redis.pipeline();
  for (const code of codes) pipeline.hgetall(urlKey(code));
  const rows = (await pipeline.exec()) ?? [];
  const urls = rows.flatMap(([, data], i) => {
    const item = data as Record<string, string>;
    return item.url ? [{ code: codes[i], url: item.url, clicks: Number(item.clicks), createdAt: item.createdAt }] : [];
  });
  return c.json({ urls });
});

// Follow a short link: count the click and redirect. The "/r/" prefix lets the
// Vite dev server proxy it without colliding with the SPA's own routes.
app.get("/r/:code", async (c) => {
  const code = c.req.param("code");
  const url = await redis.hget(urlKey(code), "url");
  if (!url) {
    log.warn({ code }, "short link not found");
    return c.text("Short link not found", 404);
  }
  await redis.hincrby(urlKey(code), "clicks", 1);
  log.info({ code, url }, "redirected");
  return c.redirect(url, 302);
});

const port = Number(process.env.PORT ?? 8080);
serve({ fetch: app.fetch, port }, () => log.info({ port }, "URL shortener API listening"));
