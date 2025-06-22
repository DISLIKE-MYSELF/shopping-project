package com.example.backend.dto.response;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ErrorResponse(@NotNull Integer status, @NotBlank String error,
    @NotBlank String message, @NotBlank String path, List<String> details) {
}
