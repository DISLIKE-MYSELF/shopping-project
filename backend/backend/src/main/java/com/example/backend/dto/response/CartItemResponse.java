package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartItemResponse(@NotNull Long id, @NotBlank String name, @NotNull BigDecimal price,
    @NotNull Integer quantity, String image, @NotNull Integer stock,
    @NotNull LocalDateTime createdAt) {
}
