package com.example.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  List<Favorite> findByUserId(Long userId);
}
