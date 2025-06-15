package com.example.backend.dto.response;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String token) {
}
