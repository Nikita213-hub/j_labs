package com.example.lab1.dto;

import com.example.lab1.domain.UserRole;

public record UserSummaryDto(
        Long id,
        String username,
        UserRole role
) {
}
