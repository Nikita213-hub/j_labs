package com.example.lab1.dto;

import java.util.UUID;

public record BookDto(UUID id, String title, String author, int publicationYear) {
}
