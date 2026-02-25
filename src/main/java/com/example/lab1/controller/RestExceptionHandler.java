package com.example.lab1.controller;

import com.example.lab1.exception.BugNotFoundException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BugNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBugNotFound(BugNotFoundException exception) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "message", exception.getMessage(),
                "status", HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
