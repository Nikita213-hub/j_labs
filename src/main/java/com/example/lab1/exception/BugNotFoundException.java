package com.example.lab1.exception;

import java.util.UUID;

public class BugNotFoundException extends RuntimeException {

    public BugNotFoundException(UUID id) {
        super("Bug with id %s not found".formatted(id));
    }
}
