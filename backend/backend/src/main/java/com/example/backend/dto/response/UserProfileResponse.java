package com.example.backend.dto.response;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserProfileResponse(@NotNull Long id, @NotBlank String username,
    @NotBlank @Email String email, String address, @NotNull LocalDateTime createdAt) {
}
