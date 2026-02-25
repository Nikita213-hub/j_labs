package com.example.lab1.dto;

import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record BugDto(
        UUID id,
        String title,
        String description,
        BugStatus status,
        BugPriority priority,
        String reporter,
        String assignee,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
