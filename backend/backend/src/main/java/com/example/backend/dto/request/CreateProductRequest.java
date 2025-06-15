package com.example.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(@NotBlank String name, String image,
    @NotNull @Positive BigDecimal price, @NotNull @PositiveOrZero Integer stock,
    @NotBlank String description, String category, @PositiveOrZero BigDecimal rating) {
}
