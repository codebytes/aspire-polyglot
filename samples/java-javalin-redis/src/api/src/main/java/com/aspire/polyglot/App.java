package com.aspire.polyglot;

import io.javalin.Javalin;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A tiny Javalin leaderboard API backed by Redis sorted sets.
 *
 * Aspire injects REDIS_HOST / REDIS_PORT (see apphost.cs). Because this app runs
 * as a container on Aspire's network, it reaches Redis by the container hostname
 * "cache". PORT is the port Aspire tells the app to listen on.
 */
public class App {

    private static final String KEY = "leaderboard";

    public static void main(String[] args) {
        int port = Integer.parseInt(env("PORT", "8080"));
        String redisHost = env("REDIS_HOST", "localhost");
        int redisPort = Integer.parseInt(env("REDIS_PORT", "6379"));

        JedisPooled redis = new JedisPooled(redisHost, redisPort);

        Javalin app = Javalin.create().start("0.0.0.0", port);

        // Minimal leaderboard UI served straight from Javalin (no extra container).
        app.get("/", ctx -> ctx.html(PAGE));

        app.get("/health", ctx -> ctx.json(Map.of("status", "healthy")));

        // Submit a score: adds/updates the player in the sorted set.
        app.post("/api/scores", ctx -> {
            ScoreDto dto = ctx.bodyAsClass(ScoreDto.class);
            redis.zadd(KEY, dto.score, dto.player);
            ctx.status(201).json(dto);
        });

        // Top 10 players by score, highest first.
        app.get("/api/leaderboard", ctx -> {
            List<Tuple> top = redis.zrevrangeWithScores(KEY, 0, 9);
            List<Map<String, Object>> board = new ArrayList<>();
            int rank = 1;
            for (Tuple t : top) {
                board.add(Map.of(
                        "rank", rank++,
                        "player", t.getElement(),
                        "score", (long) t.getScore()));
            }
            ctx.json(Map.of("leaderboard", board));
        });
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? fallback : v;
    }

    public static class ScoreDto {
        public String player;
        public double score;
    }

    // A tiny self-contained leaderboard page. Talks to the same service's
    // /api endpoints, so the whole demo is one Java container.
    private static final String PAGE = """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Aspire Java Leaderboard</title>
              <style>
                :root { font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; color: #e6e6e6; background: #12141c; }
                body { margin: 0; min-height: 100vh; }
                .wrap { max-width: 620px; margin: 0 auto; padding: 3rem 1.5rem; }
                h1 { margin: 0 0 .25rem; font-size: 1.9rem; }
                .sub { margin: 0 0 1.75rem; color: #9aa0b4; font-size: .95rem; }
                form { display: flex; gap: .5rem; margin-bottom: 1.25rem; }
                input { padding: .65rem .8rem; border: 1px solid #2c2f3c; border-radius: 8px; background: #1b1e29; color: #e6e6e6; font-size: 1rem; }
                #player { flex: 1; }
                #score { width: 6rem; }
                button { padding: .65rem 1.1rem; border: none; border-radius: 8px; background: #f89820; color: #0b0d14; font-weight: 600; font-size: 1rem; cursor: pointer; }
                button:hover { background: #ffad3f; }
                table { width: 100%; border-collapse: collapse; }
                th, td { text-align: left; padding: .6rem .8rem; border-bottom: 1px solid #2c2f3c; }
                th { color: #9aa0b4; font-weight: 600; font-size: .8rem; text-transform: uppercase; letter-spacing: .04em; }
                td.rank { width: 3rem; color: #f89820; font-weight: 700; }
                td.score { text-align: right; font-variant-numeric: tabular-nums; }
                .empty { color: #9aa0b4; text-align: center; padding: 1.5rem; }
              </style>
            </head>
            <body>
              <main class="wrap">
                <h1>&#127942; Aspire Java Leaderboard</h1>
                <p class="sub">Javalin &middot; Redis sorted set &mdash; orchestrated by a single-file C# AppHost.</p>
                <form id="form">
                  <input id="player" placeholder="Player name" required />
                  <input id="score" type="number" placeholder="Score" required />
                  <button type="submit">Submit</button>
                </form>
                <table>
                  <thead><tr><th>#</th><th>Player</th><th style="text-align:right">Score</th></tr></thead>
                  <tbody id="board"></tbody>
                </table>
              </main>
              <script>
                const board = document.getElementById('board');
                async function load() {
                  const res = await fetch('/api/leaderboard');
                  const data = await res.json();
                  const rows = data.leaderboard || [];
                  board.innerHTML = rows.length
                    ? rows.map(r => `<tr><td class="rank">${r.rank}</td><td>${r.player}</td><td class="score">${r.score}</td></tr>`).join('')
                    : '<tr><td class="empty" colspan="3">No scores yet &mdash; submit one above.</td></tr>';
                }
                document.getElementById('form').addEventListener('submit', async (e) => {
                  e.preventDefault();
                  const player = document.getElementById('player').value.trim();
                  const score = Number(document.getElementById('score').value);
                  if (!player) return;
                  await fetch('/api/scores', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ player, score })
                  });
                  document.getElementById('player').value = '';
                  document.getElementById('score').value = '';
                  load();
                });
                load();
              </script>
            </body>
            </html>
            """;
}
