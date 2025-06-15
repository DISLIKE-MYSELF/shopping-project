package com.example.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequest(@NotNull Long orderId, @NotNull @Positive BigDecimal amount,
    @NotBlank String paymentMethod, String status) {
}
