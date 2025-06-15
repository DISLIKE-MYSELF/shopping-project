package com.example.backend.dto.response;

import java.util.List;
import jakarta.validation.constraints.NotNull;

public record OrdersResponse(@NotNull List<OrderResponse> orderResponses) {
}
