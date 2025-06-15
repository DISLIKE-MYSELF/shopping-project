package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductRequest(@NotBlank String name, String image,
    @NotNull @Positive Double price, @NotNull @PositiveOrZero Integer stock,
    @NotBlank String description, String category, Double rating) {
}
