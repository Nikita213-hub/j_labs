package com.example.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BulkProjectRequest(
        @NotBlank
        String projectCode,
        @NotBlank
        String projectName,
        String description,
        @NotNull
        Long reporterId,
        Long assigneeId,
        @NotEmpty
        List<@NotBlank String> bugTitles,
        boolean failAfterFirstBug
) {
}
