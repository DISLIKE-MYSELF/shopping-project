package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentResponse(@NotNull Long id, @NotNull BigDecimal amount,
    @NotBlank String paymentMethod, @NotBlank String status, LocalDateTime paymentTime,
    @NotNull LocalDateTime createdAt, @NotNull LocalDateTime updatedAt) {
}
