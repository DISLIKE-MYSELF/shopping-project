package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.AddFavoriteItemRequest;
import com.example.backend.dto.response.FavoriteResponse;

public interface FavoriteService {
  FavoriteResponse createFavorite(String username, String favoriteName);

  void deleteFavoriteById(String username, Long FavoriteId);

  List<FavoriteResponse> getFavoritesByUserId(Long userId);

  List<FavoriteResponse> getFavoritesByUsername(String username);

  FavoriteResponse addToFavorite(String username, Long FavoriteId, AddFavoriteItemRequest request);

  FavoriteResponse deleteFavoriteItem(String username, Long FavoriteId, Long FavoriteItemId);

  FavoriteResponse clearFavorite(String username, Long FavoriteId);
}
