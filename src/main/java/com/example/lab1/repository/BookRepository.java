package com.example.lab1.repository;

import com.example.lab1.domain.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    List<Book> findAll();

    List<Book> findByAuthorContainingIgnoreCase(String author);

    Optional<Book> findById(UUID id);

    Book save(Book book);
}
