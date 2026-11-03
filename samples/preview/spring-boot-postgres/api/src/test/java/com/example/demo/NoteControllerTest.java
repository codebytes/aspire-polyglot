package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:notes;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "otel.traces.exporter=none",
        "otel.metrics.exporter=none",
        "otel.logs.exporter=none"
})
@AutoConfigureMockMvc
class NoteControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private NoteRepository repository;

    @Autowired
    private JsonMapper json;

    @BeforeEach
    void clearNotes() {
        repository.deleteAll();
    }

    @Test
    void preservesNoteCrudAndDateSerializationAcrossFrameworkUpgrade() throws Exception {
        String response = mvc.perform(post("/api/notes")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {"title":"Hello","content":"My first note"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Hello"))
                .andExpect(jsonPath("$.content").value("My first note"))
                .andExpect(jsonPath("$.createdAt").isString())
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).get("id").asLong();

        mvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].title").value("Hello"));
        mvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        mvc.perform(delete("/api/notes/{id}", id)).andExpect(status().isOk());
        mvc.perform(get("/api/notes/{id}", id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/notes")).andExpect(content().json("[]"));
    }

    @Test
    void preservesMissingNoteResponses() throws Exception {
        mvc.perform(get("/api/notes/999")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/notes/999")).andExpect(status().isNotFound());
    }
}
