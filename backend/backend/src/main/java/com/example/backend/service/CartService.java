package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.AddCartItemRequest;
import com.example.backend.dto.request.UpdateCartItemRequest;
import com.example.backend.dto.response.CartResponse;

public interface CartService {
  CartResponse createCart(String username);

  void deleteCartById(String username, Long cartId);

  List<CartResponse> getCartsByUserId(Long userId);

  List<CartResponse> getCartsByUsername(String username);

  CartResponse addToCart(String username, Long cartId, AddCartItemRequest request);

  CartResponse updateCartItem(String username, Long cartId, Long cartItemId,
      UpdateCartItemRequest request);

  CartResponse deleteCartItem(String username, Long cartId, Long cartItemId);

  CartResponse clearCart(String username, Long cartId);
}
