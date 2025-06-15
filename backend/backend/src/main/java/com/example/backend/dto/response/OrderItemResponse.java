package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemResponse(@NotNull Long id, @NotBlank String name, @NotNull BigDecimal price,
    @NotNull Integer quantity, String image, @NotNull Timestamp createdAt) {
}
