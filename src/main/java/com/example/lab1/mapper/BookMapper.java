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
