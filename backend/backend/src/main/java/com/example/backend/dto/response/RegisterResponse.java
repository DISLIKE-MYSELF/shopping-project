package com.example.backend.dto.response;

import jakarta.validation.constraints.NotBlank;

public record RegisterResponse(@NotBlank String redirectedUrl) {
}
