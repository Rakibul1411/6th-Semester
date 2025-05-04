package com.example.bookapi.application.interactor;

import com.example.bookapi.domain.entity.Book;
import com.example.bookapi.domain.repository.BookRepository;
import com.example.bookapi.domain.usecase.BookUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookInteractor implements BookUseCase {
    private final BookRepository repo;

    public BookInteractor(BookRepository repo) {
        this.repo = repo;
    }

    @Override
    public Book createBook(String title, String author) {
        Book b = new Book(null, title, author);
        return repo.save(b);
    }

    @Override
    public Optional<Book> getBook(UUID id) {
        return repo.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    @Override
    public Optional<Book> updateBook(UUID id, String title, String author) {
        return repo.findById(id)
                .map(existing -> {
                    Book updated = new Book(id, title, author);
                    return repo.save(updated);
                });
    }

    @Override
    public boolean deleteBook(UUID id) {
        if (repo.findById(id).isEmpty()) return false;
        repo.deleteById(id);
        return true;
    }
}
