package com.example.bookapi.adapter.out.persistence;

import com.example.bookapi.domain.entity.Book;
import com.example.bookapi.domain.repository.BookRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BookPersistenceAdapter implements BookRepository {
    private final SpringDataBookRepository jpaRepo;

    public BookPersistenceAdapter(SpringDataBookRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    private Book toDomain(BookEntity e) {
        return new Book(e.getId(), e.getTitle(), e.getAuthor());
    }

    private BookEntity toEntity(Book b) {
        // if domain ID is null, it’s a new book → use the title/author ctor
        if (b.getId() == null) {
            return new BookEntity(b.getTitle(), b.getAuthor());
        }
        // otherwise it’s an update → preserve the ID
        return new BookEntity(b.getId(), b.getTitle(), b.getAuthor());
    }

    @Override
    public Book save(Book book) {
        BookEntity saved = jpaRepo.save(toEntity(book));
        return toDomain(saved);
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return jpaRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return jpaRepo.findAll().stream()
                .map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
