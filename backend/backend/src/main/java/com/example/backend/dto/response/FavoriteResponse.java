package com.example.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FavoriteResponse(@NotNull Long id, @NotBlank String name,
    @NotNull List<FavoriteItemResponse> favoriteItems, @NotNull LocalDateTime updatedAt) {
}
