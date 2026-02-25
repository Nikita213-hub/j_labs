package com.example.lab1.repository;

import com.example.lab1.domain.Book;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final Map<UUID, Book> storage = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadSampleData() {
        save(new Book(null, "Clean Code", "Robert C. Martin", 2008));
        save(new Book(null, "Effective Java", "Joshua Bloch", 2018));
        save(new Book(null, "Designing Data-Intensive Applications", "Martin Kleppmann", 2017));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        String normalized = author.toLowerCase();
        return storage.values().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().toLowerCase().contains(normalized))
                .toList();
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Book save(Book book) {
        UUID bookId = book.getId() == null ? UUID.randomUUID() : book.getId();
        book.setId(bookId);
        storage.put(bookId, book);
        return book;
    }
}
