Файл Lab1Application.java

```java
package com.example.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab1Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab1Application.class, args);
    }
}
```

Файл BookController.java

```java
package com.example.lab1.controller;

import com.example.lab1.dto.BookDto;
import com.example.lab1.dto.BookRequest;
import com.example.lab1.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getBooks(@RequestParam(required = false) String author) {
        return bookService.findBooks(author);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable UUID id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookRequest request) {
        return bookService.create(request);
    }
}
```

Файл BookService.java

```java
package com.example.lab1.service;

import com.example.lab1.domain.Book;
import com.example.lab1.dto.BookDto;
import com.example.lab1.dto.BookRequest;
import com.example.lab1.exception.BookNotFoundException;
import com.example.lab1.mapper.BookMapper;
import com.example.lab1.repository.BookRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findBooks(String authorFilter) {
        List<Book> books = StringUtils.hasText(authorFilter)
                ? bookRepository.findByAuthorContainingIgnoreCase(authorFilter)
                : bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }

    public BookDto findById(UUID id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    public BookDto create(BookRequest request) {
        Book toSave = bookMapper.toEntity(request);
        Book saved = bookRepository.save(toSave);
        return bookMapper.toDto(saved);
    }
}
```

Файл InMemoryBookRepository.java

```java
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
```

Файл BookMapper.java

```java
package com.example.lab1.mapper;

import com.example.lab1.domain.Book;
import com.example.lab1.dto.BookDto;
import com.example.lab1.dto.BookRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public Book toEntity(BookRequest request) {
        return new Book(
                (UUID) null,
                request.title(),
                request.author(),
                request.publicationYear()
        );
    }

    public List<BookDto> toDtoList(List<Book> books) {
        return books.stream().map(this::toDto).toList();
    }
}
```
