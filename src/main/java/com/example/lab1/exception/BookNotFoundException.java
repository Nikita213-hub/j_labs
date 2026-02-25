package com.example.lab1.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(UUID id) {
        super("Book with id %s not found".formatted(id));
    }
}
