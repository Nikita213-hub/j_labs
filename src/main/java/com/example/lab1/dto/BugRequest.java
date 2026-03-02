package com.example.lab1.dto;

import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record BugRequest(
        @NotBlank
        @Size(max = 120)
        String title,
        @NotBlank
        @Size(max = 2_000)
        String description,
        @NotNull
        BugPriority priority,
        BugStatus status,
        @NotNull
        Long projectId,
        @NotNull
        Long reporterId,
        Long assigneeId,
        Set<Long> tagIds
) {
}
