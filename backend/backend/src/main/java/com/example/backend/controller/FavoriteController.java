package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.AddFavoriteItemRequest;
import com.example.backend.dto.request.CreateFavoriteRequest;
import com.example.backend.dto.response.FavoriteResponse;
import com.example.backend.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@AllArgsConstructor
public class FavoriteController {

  private final FavoriteService favoriteService;

  // 获取用户所有收藏夹
  @GetMapping("/my-favorites")
  public ResponseEntity<List<FavoriteResponse>> getAllFavorites(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(favoriteService.getFavoritesByUsername(userDetails.getUsername()));
  }

  // 增加收藏夹
  @PostMapping("/my-favorites")
  public ResponseEntity<FavoriteResponse> createFavorite(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody(required = false) @Valid CreateFavoriteRequest request) {
    String name = (request != null && request.name() != null) ? request.name() : "";
    return ResponseEntity.ok(favoriteService.createFavorite(userDetails.getUsername(), name));
  }

  // 添加收藏夹项
  @PostMapping("/{favoriteId}/items")
  public ResponseEntity<FavoriteResponse> addToFavorite(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long favoriteId,
      @RequestBody @Valid AddFavoriteItemRequest request) {
    return ResponseEntity
        .ok(favoriteService.addToFavorite(userDetails.getUsername(), favoriteId, request));
  }

  // 删除单个收藏夹项
  @DeleteMapping("/{favoriteId}/items/{favoriteItemId}")
  public ResponseEntity<FavoriteResponse> deleteFavoriteItem(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long favoriteId,
      @PathVariable Long favoriteItemId) {
    return ResponseEntity.ok(
        favoriteService.deleteFavoriteItem(userDetails.getUsername(), favoriteId, favoriteItemId));
  }

  // 清空收藏夹
  @DeleteMapping("/{favoriteId}/items")
  public ResponseEntity<FavoriteResponse> clearFavorite(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long favoriteId) {
    return ResponseEntity.ok(favoriteService.clearFavorite(userDetails.getUsername(), favoriteId));
  }

  // 删除收藏夹
  @DeleteMapping("/{favoriteId}")
  public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long favoriteId) {
    favoriteService.deleteFavoriteById(userDetails.getUsername(), favoriteId);
    return ResponseEntity.noContent().build();
  }
}
