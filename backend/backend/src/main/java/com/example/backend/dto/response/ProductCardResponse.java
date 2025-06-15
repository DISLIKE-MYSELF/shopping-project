package com.example.backend.dto.response;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCardResponse(@NotNull Long id, @NotBlank String name, String image,
    @NotNull BigDecimal price, @NotNull Integer stock) {
}
