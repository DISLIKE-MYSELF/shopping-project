package com.example.backend.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.example.backend.dto.response.FavoriteItemResponse;
import com.example.backend.dto.response.FavoriteResponse;
import com.example.backend.model.Favorite;
import com.example.backend.model.FavoriteItem;
import com.example.backend.model.Product;

@Component
public class FavoriteMapper {

  private FavoriteItemResponse toFavoriteItemResponse(FavoriteItem favoriteItem) {
    Product product = favoriteItem.getProduct();
    return new FavoriteItemResponse(favoriteItem.getId(), product.getId(), product.getName(), product.getPrice(),
        product.getImage(), product.getStock(), favoriteItem.getCreatedAt());
  }

  public FavoriteResponse toFavoriteResponse(Favorite favorite, List<FavoriteItem> favoriteItems) {
    List<FavoriteItemResponse> itemResponses = favoriteItems.stream().map(this::toFavoriteItemResponse).toList();

    return new FavoriteResponse(favorite.getId(), favorite.getName(), itemResponses,
        favorite.getUpdatedAt());
  }
}
