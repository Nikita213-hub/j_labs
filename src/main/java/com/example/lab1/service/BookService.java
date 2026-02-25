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
