package com.kmgtechnologies.librarymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Author is required") String author,
        @Min(value = 1450, message = "Publication year must be at least 1450") int publicationYear
) {}