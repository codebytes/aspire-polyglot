package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public List<Note> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        logger.info("Fetched {} notes", notes.size());
        return notes;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        Note saved = noteRepository.save(note);
        logger.info("Created note id={} title=\"{}\"", saved.getId(), saved.getTitle());
        return saved;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return noteRepository.findById(id)
            .map(note -> {
                logger.info("Fetched note id={}", id);
                return ResponseEntity.ok(note);
            })
            .orElseGet(() -> {
                logger.warn("Note id={} not found", id);
                return ResponseEntity.notFound().build();
            });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            logger.info("Deleted note id={}", id);
            return ResponseEntity.ok().build();
        }
        logger.warn("Delete failed — note id={} not found", id);
        return ResponseEntity.notFound().build();
    }
}
