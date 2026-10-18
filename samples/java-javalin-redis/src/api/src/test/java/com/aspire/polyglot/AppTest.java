package com.aspire.polyglot;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockMakers;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.resps.Tuple;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppTest {
    private final RedisClient redis = mock(RedisClient.class, withSettings().mockMaker(MockMakers.SUBCLASS));
    private final JsonMapper json = JsonMapper.builder().build();
    private final HttpClient client = HttpClient.newHttpClient();
    private Javalin app;

    @BeforeEach
    void startApp() {
        app = App.createApp(redis).start("127.0.0.1", 0);
    }

    @AfterEach
    void stopApp() {
        app.stop();
        verify(redis).close();
    }

    @Test
    void servesHealthAndExistingUi() throws Exception {
        var health = get("/health");
        assertEquals(200, health.statusCode());
        assertEquals("healthy", json.readTree(health.body()).get("status").asText());

        var page = get("/");
        assertEquals(200, page.statusCode());
        assertTrue(page.body().contains("cell.textContent = value"));
        assertTrue(page.body().contains("fetch('/api/leaderboard')"));
    }

    @Test
    void deserializesAndEchoesSubmittedScoreWithJackson3() throws Exception {
        String player = "<img src=x onerror=alert(1)>";
        String body = json.writeValueAsString(java.util.Map.of("player", player, "score", 123.5));
        var request = HttpRequest.newBuilder(uri("/api/scores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(player, json.readTree(response.body()).get("player").asText());
        assertEquals(123.5, json.readTree(response.body()).get("score").asDouble());
        verify(redis).zadd("leaderboard", 123.5, player);
    }

    @Test
    void preservesLeaderboardEnvelopeRanksAndScoreRepresentation() throws Exception {
        when(redis.zrevrangeWithScores("leaderboard", 0, 9)).thenReturn(
                List.of(new Tuple("Alice", 300.75), new Tuple("Dave", 100.0)));
        var response = get("/api/leaderboard");
        var board = json.readTree(response.body()).get("leaderboard");

        assertEquals(200, response.statusCode());
        assertEquals(2, board.size());
        assertEquals(1, board.get(0).get("rank").asInt());
        assertEquals("Alice", board.get(0).get("player").asText());
        assertEquals(300, board.get(0).get("score").asLong());
        assertEquals(2, board.get(1).get("rank").asInt());

        when(redis.zrevrangeWithScores("leaderboard", 0, 9)).thenReturn(List.of());
        assertEquals(0, json.readTree(get("/api/leaderboard").body()).get("leaderboard").size());
    }

    private URI uri(String path) {
        return URI.create("http://127.0.0.1:" + app.port() + path);
    }

    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(uri(path)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
