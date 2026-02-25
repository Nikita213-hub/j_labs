package com.example.lab1.dto;

import com.example.lab1.domain.BugPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BugRequest(
        @NotBlank
        @Size(max = 120)
        String title,
        @NotBlank
        @Size(max = 2_000)
        String description,
        @NotNull
        BugPriority priority,
        @NotBlank
        @Size(max = 60)
        String reporter,
        @Size(max = 60)
        String assignee
) {
}
