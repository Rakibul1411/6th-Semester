package com.example.bookapi.adapter.in.rest;

import com.example.bookapi.domain.entity.Book;
import com.example.bookapi.domain.usecase.BookUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookUseCase bookUseCase;

    public BookController(BookUseCase bookUseCase) {
        this.bookUseCase = bookUseCase;
    }

    @GetMapping
    public List<Book> list() {
        return bookUseCase.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable UUID id) {
        return bookUseCase.getBook(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Book create(@RequestBody BookRequest req) {
        return bookUseCase.createBook(req.title(), req.author());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable UUID id,
            @RequestBody BookRequest req) {
        return bookUseCase.updateBook(id, req.title(), req.author())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return bookUseCase.deleteBook(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // DTO
    public record BookRequest(String title, String author) { }
}
