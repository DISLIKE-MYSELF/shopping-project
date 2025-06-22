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
import com.example.backend.dto.request.AddCartItemRequest;
import com.example.backend.dto.request.UpdateCartItemRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.CartResponse;
import com.example.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CartController {

  private final CartService cartService;

  // 获取用户所有购物车
  @GetMapping("/my-carts")
  public ResponseEntity<ApiResponse<List<CartResponse>>> getAllCarts(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity
        .ok(ApiResponse.of(cartService.getCartsByUsername(userDetails.getUsername())));
  }

  // 增加购物车
  @PostMapping("/my-carts")
  public ResponseEntity<ApiResponse<CartResponse>> createCart(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(ApiResponse.of(cartService.createCart(userDetails.getUsername())));
  }

  // 添加购物车项
  @PostMapping("/{cartId}/items")
  public ResponseEntity<ApiResponse<CartResponse>> addToCart(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId,
      @RequestBody @Valid AddCartItemRequest request) {
    return ResponseEntity
        .ok(ApiResponse.of(cartService.addToCart(userDetails.getUsername(), cartId, request)));
  }

  // 更新购物车项
  @PostMapping("/{cartId}/items/{cartItemId}")
  public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId,
      @PathVariable Long cartItemId, @RequestBody @Valid UpdateCartItemRequest request) {
    return ResponseEntity.ok(ApiResponse
        .of(cartService.updateCartItem(userDetails.getUsername(), cartId, cartItemId, request)));
  }

  // 删除单个购物车项
  @DeleteMapping("/{cartId}/items/{cartItemId}")
  public ResponseEntity<ApiResponse<CartResponse>> deleteCartItem(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId,
      @PathVariable Long cartItemId) {
    return ResponseEntity.ok(
        ApiResponse.of(cartService.deleteCartItem(userDetails.getUsername(), cartId, cartItemId)));
  }

  // 清空购物车
  @DeleteMapping("/{cartId}/items")
  public ResponseEntity<ApiResponse<CartResponse>> clearCart(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId) {
    return ResponseEntity
        .ok(ApiResponse.of(cartService.clearCart(userDetails.getUsername(), cartId)));
  }

  // 删除购物车
  @DeleteMapping("/{cartId}")
  public ResponseEntity<?> deleteCart(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long cartId) {
    cartService.deleteCartById(userDetails.getUsername(), cartId);
    return ResponseEntity.noContent().build();
  }
}
