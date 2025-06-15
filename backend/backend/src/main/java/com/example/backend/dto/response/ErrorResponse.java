package com.example.backend.dto.response;

import java.util.List;
import jakarta.validation.constraints.NotBlank;

public record ErrorResponse(@NotBlank String error, @NotBlank String message, @NotBlank String path,
    List<String> details) {
}
