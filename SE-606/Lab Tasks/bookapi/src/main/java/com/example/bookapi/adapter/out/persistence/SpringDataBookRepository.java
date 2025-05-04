package com.example.bookapi.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataBookRepository
        extends JpaRepository<BookEntity, UUID> { }
