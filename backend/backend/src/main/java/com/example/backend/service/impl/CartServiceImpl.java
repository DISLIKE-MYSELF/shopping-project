package com.example.backend.service.impl;

import com.example.backend.model.Cart;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart addToCart(Cart cart) {
        User user = userRepository.findById(cart.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(cart.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.setUser(user);
        cart.setProduct(product);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public void clearCartByUserId(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
