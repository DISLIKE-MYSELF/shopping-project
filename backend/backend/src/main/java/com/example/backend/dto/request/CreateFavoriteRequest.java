package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFavoriteRequest(@NotBlank @Size(max = 20) String name) {
}
