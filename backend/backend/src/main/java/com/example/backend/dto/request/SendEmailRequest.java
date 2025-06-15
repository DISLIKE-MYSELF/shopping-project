package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendEmailRequest(@NotNull Long userId, @NotBlank String subject,
    @NotBlank String content) {
}
