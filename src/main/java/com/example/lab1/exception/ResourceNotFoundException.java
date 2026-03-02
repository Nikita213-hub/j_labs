package com.example.lab1.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String type, Object identifier) {
        super(type + " not found: " + identifier);
    }
}
