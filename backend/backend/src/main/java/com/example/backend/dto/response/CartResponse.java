package com.example.backend.dto.response;

import java.sql.Timestamp;
import java.util.List;
import jakarta.validation.constraints.NotNull;

public record CartResponse(@NotNull Long id, @NotNull List<CartItemResponse> cartItems,
    @NotNull Timestamp updatedAt) {
}
