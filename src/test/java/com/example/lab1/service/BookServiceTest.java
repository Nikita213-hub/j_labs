package com.example.lab1.service;

import com.example.lab1.domain.Book;
import com.example.lab1.dto.BookDto;
import com.example.lab1.dto.BookRequest;
import com.example.lab1.exception.BookNotFoundException;
import com.example.lab1.mapper.BookMapper;
import com.example.lab1.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnBooksByFilter() {
        Book book = new Book(UUID.randomUUID(), "Clean Architecture", "Robert C. Martin", 2017);
        when(bookRepository.findByAuthorContainingIgnoreCase("martin")).thenReturn(List.of(book));
        BookDto dto = new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear());
        when(bookMapper.toDtoList(List.of(book))).thenReturn(List.of(dto));

        List<BookDto> result = bookService.findBooks("martin");

        assertThat(result).containsExactly(dto);
    }

    @Test
    void shouldThrowWhenBookMissing() {
        UUID id = UUID.randomUUID();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(id))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void shouldCreateBookFromRequest() {
        BookRequest request = new BookRequest("Domain-Driven Design", "Eric Evans", 2004);
        Book entity = new Book(null, request.title(), request.author(), request.publicationYear());
        Book saved = new Book(UUID.randomUUID(), request.title(), request.author(), request.publicationYear());
        BookDto dto = new BookDto(saved.getId(), saved.getTitle(), saved.getAuthor(), saved.getPublicationYear());

        when(bookMapper.toEntity(request)).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(dto);

        BookDto result = bookService.create(request);

        assertThat(result).isEqualTo(dto);
    }
}
