package com.example.backend.dto.response;

import java.sql.Timestamp;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FavoriteResponse(@NotNull Long id, @NotBlank String name,
    @NotNull List<FavoriteItemResponse> favoriteItems, @NotNull Timestamp updatedAt) {
}
