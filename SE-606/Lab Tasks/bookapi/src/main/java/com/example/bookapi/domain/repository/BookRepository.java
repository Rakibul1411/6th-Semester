package com.example.bookapi.domain.repository;

import com.example.bookapi.domain.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(UUID id);
    List<Book> findAll();
    void deleteById(UUID id);
}
