package com.example.lab1.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String body,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt
) {
}
