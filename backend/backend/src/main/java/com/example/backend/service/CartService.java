package com.example.backend.service;

import com.example.backend.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCartByUserId(Long userId);
    Cart addToCart(Cart cart);
    void deleteCartItem(Long id);
    void clearCartByUserId(Long userId);
}
