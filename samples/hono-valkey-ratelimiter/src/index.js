import { Hono } from 'hono';
import { serve } from '@hono/node-server';
import Redis from 'ioredis';
import { quotes } from './quotes.js';

const app = new Hono();

// Parse Aspire connection string
function parseConnectionString(connectionString) {
  if (!connectionString) {
    throw new Error('CONNECTIONSTRINGS__CACHE environment variable not set');
  }
  
  // Aspire connection string format: host:port
  const [host, port] = connectionString.split(':');
  return { host, port: parseInt(port || '6379') };
}

// Initialize Valkey client
const { host, port } = parseConnectionString(process.env.CONNECTIONSTRINGS__CACHE);
const redis = new Redis({ host, port });

redis.on('error', (err) => {
  console.error('Valkey connection error:', err);
});

redis.on('connect', () => {
  console.log(`Connected to Valkey at ${host}:${port}`);
});

// Rate limiter configuration
const RATE_LIMIT = 10;
const WINDOW_SECONDS = 60;

// Rate limiter middleware - sliding window
async function rateLimiter(c, next) {
  const ip = c.req.header('x-forwarded-for') || 'unknown';
  const key = `ratelimit:${ip}`;
  const now = Date.now();
  const windowStart = now - (WINDOW_SECONDS * 1000);

  try {
    // Remove old entries outside the window
    await redis.zremrangebyscore(key, 0, windowStart);
    
    // Count current requests in window
    const requestCount = await redis.zcard(key);
    
    // Check if rate limit exceeded
    if (requestCount >= RATE_LIMIT) {
      // Get oldest entry to calculate reset time
      const oldest = await redis.zrange(key, 0, 0, 'WITHSCORES');
      const resetTime = oldest.length > 0 ? parseInt(oldest[1]) + (WINDOW_SECONDS * 1000) : now + (WINDOW_SECONDS * 1000);
      const retryAfter = Math.ceil((resetTime - now) / 1000);
      
      c.header('X-RateLimit-Limit', RATE_LIMIT.toString());
      c.header('X-RateLimit-Remaining', '0');
      c.header('X-RateLimit-Reset', Math.ceil(resetTime / 1000).toString());
      c.header('Retry-After', retryAfter.toString());
      
      return c.json({
        error: 'Too many requests',
        retryAfter: retryAfter
      }, 429);
    }
    
    // Add current request to the window
    await redis.zadd(key, now, `${now}-${Math.random()}`);
    await redis.expire(key, WINDOW_SECONDS);
    
    // Calculate remaining requests
    const remaining = RATE_LIMIT - requestCount - 1;
    const resetTime = now + (WINDOW_SECONDS * 1000);
    
    // Add rate limit headers
    c.header('X-RateLimit-Limit', RATE_LIMIT.toString());
    c.header('X-RateLimit-Remaining', remaining.toString());
    c.header('X-RateLimit-Reset', Math.ceil(resetTime / 1000).toString());
    
    // Store rate limit info in context for status endpoint
    c.set('rateLimitInfo', {
      ip,
      remaining,
      resetIn: WINDOW_SECONDS,
      limit: RATE_LIMIT
    });
    
    await next();
  } catch (error) {
    console.error('Rate limiter error:', error);
    await next(); // Continue on error to not block the app
  }
}

// Apply rate limiter to all routes except health
app.use('/api/*', rateLimiter);

// GET /api/quote - Random quote
app.get('/api/quote', (c) => {
  const randomQuote = quotes[Math.floor(Math.random() * quotes.length)];
  return c.json(randomQuote);
});

// GET /api/status - Rate limit status
app.get('/api/status', (c) => {
  const rateLimitInfo = c.get('rateLimitInfo') || {
    ip: 'unknown',
    remaining: RATE_LIMIT,
    resetIn: WINDOW_SECONDS,
    limit: RATE_LIMIT
  };
  return c.json(rateLimitInfo);
});

// GET /api/quotes/random/:count - Multiple random quotes
app.get('/api/quotes/random/:count', (c) => {
  const count = Math.min(parseInt(c.req.param('count')) || 1, 5);
  
  // Shuffle and get N quotes
  const shuffled = [...quotes].sort(() => 0.5 - Math.random());
  const selected = shuffled.slice(0, count);
  
  return c.json({
    count: selected.length,
    quotes: selected
  });
});

// GET /health - Health check (no rate limit)
app.get('/health', (c) => {
  return c.json({ status: 'healthy' });
});

// Root endpoint
app.get('/', (c) => {
  return c.json({
    name: 'Hono + Valkey Rate Limiter API',
    endpoints: {
      quote: '/api/quote',
      status: '/api/status',
      multipleQuotes: '/api/quotes/random/:count',
      health: '/health'
    },
    rateLimit: {
      limit: RATE_LIMIT,
      windowSeconds: WINDOW_SECONDS
    }
  });
});

// Start server
const port = parseInt(process.env.PORT || '3000');
console.log(`Starting Hono server on port ${port}...`);

serve({
  fetch: app.fetch,
  port
});

console.log(`Server running at http://localhost:${port}`);
