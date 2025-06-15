package com.example.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductRequest(String name, String image, @Positive BigDecimal price,
    @PositiveOrZero Integer stock, String description, String category,
    @PositiveOrZero BigDecimal rating) {
}
