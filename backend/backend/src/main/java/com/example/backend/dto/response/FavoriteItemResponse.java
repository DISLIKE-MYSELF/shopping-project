package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FavoriteItemResponse(@NotNull Long id, @NotNull Long productId, @NotBlank String name,
    @NotNull BigDecimal price, String image, @NotNull Integer stock,
    @NotNull LocalDateTime createdAt) {
}
