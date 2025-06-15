package com.example.backend.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.example.backend.dto.response.CartItemResponse;
import com.example.backend.dto.response.CartResponse;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;

@Component
public class CartMapper {

  private CartItemResponse toCartItemResponse(CartItem cartItem) {
    Product product = cartItem.getProduct();
    return new CartItemResponse(cartItem.getId(), product.getName(), product.getPrice(),
        cartItem.getQuantity(), product.getImage(), product.getStock(), cartItem.getCreatedAt());
  }

  public CartResponse toCartResponse(Cart cart, List<CartItem> cartItems) {
    List<CartItemResponse> itemResponses =
        cartItems.stream().map(this::toCartItemResponse).toList();

    return new CartResponse(cart.getId(), itemResponses, cart.getUpdatedAt());
  }
}
