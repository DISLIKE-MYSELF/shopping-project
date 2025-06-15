package com.example.backend.dto.response;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductResponse(@NotNull Long id, @NotBlank String name, String image,
    @NotNull BigDecimal price, @NotNull Integer stock, @NotNull String description, String category,
    @NotNull Double rating) {
}
