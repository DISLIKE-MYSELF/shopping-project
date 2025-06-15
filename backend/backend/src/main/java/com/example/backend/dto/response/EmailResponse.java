package com.example.backend.dto.response;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailResponse(@NotNull Long id, @NotBlank String subject, @NotBlank String content,
    @NotNull LocalDateTime sentTime) {
}
