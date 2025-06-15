package com.example.backend.dto.response;

import jakarta.validation.constraints.NotNull;

public record RegisterResponse(@NotNull Long userId) {
}
