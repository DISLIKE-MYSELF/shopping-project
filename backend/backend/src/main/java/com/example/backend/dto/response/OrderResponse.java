package com.example.backend.dto.response;

import java.security.Timestamp;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderResponse(@NotNull Long id, @NotBlank String status,
    @NotNull List<OrderItemResponse> orderItemResponses, @NotNull Timestamp createdAt,
    @NotNull Timestamp updatedAt) {
}
