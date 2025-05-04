package com.example.bookapi.adapter.out.persistence;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue   // ← Let Hibernate/DB generate the UUID
    private UUID id;

    private String title;
    private String author;

    // JPA requires a no-arg constructor
    protected BookEntity() {}

    // used for inserts
    public BookEntity(String title, String author) {
        this.title  = title;
        this.author = author;
    }

    // used for updates
    public BookEntity(UUID id, String title, String author) {
        this.id     = id;
        this.title  = title;
        this.author = author;
    }

    // getters & setters...
    public UUID getId()       { return id; }
    public void setId(UUID id){ this.id = id; }
    public String getTitle()  { return title; }
    public void setTitle(String t){ this.title = t; }
    public String getAuthor() { return author; }
    public void setAuthor(String a){ this.author = a; }
}