package com.example.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;

public record CartResponse(@NotNull Long id, @NotNull List<CartItemResponse> cartItems,
    @NotNull LocalDateTime updatedAt) {
}
