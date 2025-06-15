package com.example.backend.dto.response;

import java.sql.Timestamp;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderResponse(@NotNull Long id, @NotBlank String status, @NotBlank String address,
    @NotNull List<OrderItemResponse> orderItems, @NotNull Timestamp createdAt,
    @NotNull Timestamp updatedAt) {
}
