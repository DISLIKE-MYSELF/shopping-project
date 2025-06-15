package com.example.backend.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.AddFavoriteItemRequest;
import com.example.backend.dto.response.FavoriteResponse;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.FavoriteMapper;
import com.example.backend.model.Favorite;
import com.example.backend.model.FavoriteItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.FavoriteItemRepository;
import com.example.backend.repository.FavoriteRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final FavoriteItemRepository favoriteItemRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final FavoriteMapper favoriteMapper;

  // 将收藏夹转为响应
  private FavoriteResponse getFavoriteResponse(Favorite favorite) {

    List<FavoriteItem> favoriteItems =
        favoriteItemRepository.findByFavoriteIdWithProduct(favorite.getId());

    return favoriteMapper.toFavoriteResponse(favorite, favoriteItems);
  }

  @Override
  @Transactional
  public FavoriteResponse createFavorite(String username, String favoriteName) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Favorite favorite = new Favorite();
    favorite.setUser(user);
    if (favoriteName != null && !favoriteName.isEmpty()) {
      favorite.setName(favoriteName);
    }
    return getFavoriteResponse(favoriteRepository.save(favorite));
  }

  @Override
  @Transactional
  public void deleteFavoriteById(String username, Long favoriteId) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 验证收藏夹存在性
    Favorite favorite = favoriteRepository.findById(favoriteId)
        .orElseThrow(() -> new EntityNotFoundException("Favorite", favoriteId));

    // 权限校验（当前收藏夹的用户ID与请求的用户ID匹配）
    if (!favorite.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    favoriteItemRepository.deleteAllByFavoriteId(favoriteId);
    favoriteRepository.deleteById(favoriteId);
  }

  @Override
  @Transactional
  public FavoriteResponse addToFavorite(String username, Long favoriteId,
      AddFavoriteItemRequest request) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 获取收藏夹
    Favorite favorite = favoriteRepository.findById(favoriteId)
        .orElseThrow(() -> new EntityNotFoundException("Favorite", favoriteId));

    // 权限校验（当前收藏夹的用户ID与请求的用户ID匹配）
    if (!favorite.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new EntityNotFoundException("Product", request.productId()));

    // 查找或创建收藏夹项
    Optional<FavoriteItem> existingItem =
        favoriteItemRepository.findByFavoriteIdAndProductId(favoriteId, request.productId());

    if (existingItem.isPresent()) {
      // Do nothing
    } else {
      // 创建新项
      FavoriteItem newItem = new FavoriteItem();
      newItem.setFavorite(favorite);
      newItem.setProduct(product);
      favoriteItemRepository.save(newItem);
    }

    // 更新收藏夹时间
    favorite.setUpdatedAt(LocalDateTime.now());

    // 返回更新后的收藏夹
    return getFavoriteResponse(favoriteRepository.save(favorite));
  }

  @Override
  public List<FavoriteResponse> getFavoritesByUserId(Long userId) {
    // 获取用户的所有收藏夹
    List<Favorite> favorites = favoriteRepository.findByUserId(userId);

    if (favorites.isEmpty()) {
      return Collections.emptyList();
    }

    // 获取所有收藏夹ID
    List<Long> favoriteIds = favorites.stream().map(Favorite::getId).toList();

    // 批量获取所有收藏夹项
    List<FavoriteItem> allFavoriteItems =
        favoriteItemRepository.findByFavoriteIdsWithProduct(favoriteIds);

    // 按收藏夹ID分组
    Map<Long, List<FavoriteItem>> favoriteItemsMap = allFavoriteItems.stream()
        .collect(Collectors.groupingBy(item -> item.getFavorite().getId()));

    // 构建响应
    return favorites.stream().map(favorite -> {
      List<FavoriteItem> items =
          favoriteItemsMap.getOrDefault(favorite.getId(), Collections.emptyList());
      return favoriteMapper.toFavoriteResponse(favorite, items);
    }).toList();
  }

  @Override
  public List<FavoriteResponse> getFavoritesByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return getFavoritesByUserId(user.getId());
  }

  @Override
  @Transactional
  public FavoriteResponse deleteFavoriteItem(String username, Long favoriteId,
      Long favoriteItemId) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    FavoriteItem favoriteItem = favoriteItemRepository.findById(favoriteItemId)
        .orElseThrow(() -> new EntityNotFoundException("FavoriteItem", favoriteItemId));

    Favorite favorite = favoriteItem.getFavorite();

    // 权限校验
    if (!favorite.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    if (!favorite.getId().equals(favoriteId)) {
      throw new BusinessException("收藏夹项与收藏夹不匹配");
    }

    favoriteItemRepository.delete(favoriteItem);

    // 更新收藏夹时间
    favorite.setUpdatedAt(LocalDateTime.now());

    return getFavoriteResponse(favoriteRepository.save(favorite));
  }

  @Override
  @Transactional
  public FavoriteResponse clearFavorite(String username, Long favoriteId) {
    Favorite favorite = favoriteRepository.findById(favoriteId)
        .orElseThrow(() -> new EntityNotFoundException("收藏夹", favoriteId));

    // 权限校验
    if (!favorite.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    favoriteItemRepository.deleteAllByFavoriteId(favoriteId);

    // 更新收藏夹时间
    favorite.setUpdatedAt(LocalDateTime.now());

    return getFavoriteResponse(favoriteRepository.save(favorite));
  }
}
