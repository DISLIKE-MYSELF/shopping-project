package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.backend.model.FavoriteItem;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
  List<FavoriteItem> findByFavoriteId(Long favoriteId);

  Optional<FavoriteItem> findByFavoriteIdAndProductId(Long favoriteId, Long productId);

  @Query("SELECT fi FROM FavoriteItem fi " + "JOIN FETCH fi.product "
      + "WHERE fi.favorite.id = :favoriteId")
  List<FavoriteItem> findByFavoriteIdWithProduct(@Param("favoriteId") Long favoriteId);

  @Query("SELECT fi FROM FavoriteItem fi " + "JOIN FETCH fi.product "
      + "WHERE fi.favorite.id IN :favoriteIds")
  List<FavoriteItem> findByFavoriteIdsWithProduct(@Param("favoriteIds") List<Long> favoriteIds);

  void deleteAllByFavoriteId(Long favoriteId);
}
