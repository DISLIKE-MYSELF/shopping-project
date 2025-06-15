package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.security.Timestamp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentResponse(@NotNull Long id, @NotNull BigDecimal amount,
    @NotBlank String address, @NotBlank String paymentMethod, @NotBlank String status,
    Timestamp paymentTime, @NotNull Timestamp createdAt, @NotNull Timestamp updatedAt) {
}
