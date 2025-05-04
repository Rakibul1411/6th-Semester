package com.example.bookapi.domain.usecase;

import com.example.bookapi.domain.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookUseCase {
    Book createBook(String title, String author);
    Optional<Book> getBook(UUID id);
    List<Book> getAllBooks();
    Optional<Book> updateBook(UUID id, String title, String author);
    boolean deleteBook(UUID id);
}
