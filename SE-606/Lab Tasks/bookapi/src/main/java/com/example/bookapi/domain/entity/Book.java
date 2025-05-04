package com.example.bookapi.domain.entity;

import java.util.UUID;

public class Book {
    private final UUID id;
    private final String title;
    private final String author;

    public Book(UUID id, String title, String author) {
        this.id     = id;
        this.title  = title;
        this.author = author;
    }
    // getters only; immutability preferred
    public UUID getId()       { return id; }
    public String getTitle()  { return title; }
    public String getAuthor() { return author; }
}
