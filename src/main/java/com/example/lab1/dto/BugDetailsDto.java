package com.example.lab1.dto;

import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BugDetailsDto(
        UUID id,
        String title,
        String description,
        BugStatus status,
        BugPriority priority,
        ProjectDto project,
        UserSummaryDto reporter,
        UserSummaryDto assignee,
        List<String> tags,
        List<CommentDto> comments,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
