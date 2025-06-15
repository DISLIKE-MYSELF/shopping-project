package com.example.backend.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull @NotEmpty List<Long> cartItemIds, String address) {
}
