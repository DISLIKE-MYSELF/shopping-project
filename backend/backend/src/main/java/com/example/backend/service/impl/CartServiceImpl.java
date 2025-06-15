package com.example.backend.service.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.AddCartItemRequest;
import com.example.backend.dto.request.UpdateCartItemRequest;
import com.example.backend.dto.response.CartResponse;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.CartMapper;
import com.example.backend.model.Cart;
import com.example.backend.model.CartItem;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CartService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final CartMapper cartMapper;

  // 将购物车转为响应
  private CartResponse getCartResponse(Cart cart) {

    List<CartItem> cartItems = cartItemRepository.findByCartIdWithProduct(cart.getId());

    return cartMapper.toCartResponse(cart, cartItems);
  }

  @Override
  @Transactional
  public CartResponse createCart(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    Cart cart = new Cart();
    cart.setUser(user);
    return getCartResponse(cartRepository.save(cart));
  }

  @Override
  @Transactional
  public void deleteCartById(String username, Long cartId) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 验证购物车存在性
    Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new EntityNotFoundException("Cart", cartId));

    // 权限校验（当前购物车的用户ID与请求的用户ID匹配）
    if (!cart.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    cartItemRepository.deleteAllByCartId(cartId);
    cartRepository.deleteById(cartId);
  }

  @Override
  @Transactional
  public CartResponse addToCart(String username, Long cartId, AddCartItemRequest request) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 获取购物车
    Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new EntityNotFoundException("Cart", cartId));

    // 权限校验（当前购物车的用户ID与请求的用户ID匹配）
    if (!cart.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    // 获取商品
    Product product = productRepository.findById(request.productId())
        .orElseThrow(() -> new EntityNotFoundException("Product", request.productId()));

    // 库存校验
    if (product.getStock() < request.quantity()) {
      throw new BusinessException(product.getName() + " 库存不足");
    }

    // 查找或创建购物车项
    Optional<CartItem> existingItem =
        cartItemRepository.findByCartIdAndProductId(cartId, request.productId());

    if (existingItem.isPresent()) {
      // 更新现有项
      CartItem item = existingItem.get();
      item.setQuantity(request.quantity());
      cartItemRepository.save(item);
    } else {
      // 创建新项
      CartItem newItem = new CartItem();
      newItem.setCart(cart);
      newItem.setProduct(product);
      newItem.setQuantity(request.quantity());
      newItem.setCreatedAt(new Timestamp(System.currentTimeMillis()));
      cartItemRepository.save(newItem);
    }

    // 更新购物车时间
    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    // 返回更新后的购物车
    return getCartResponse(cartRepository.save(cart));
  }

  @Override
  public List<CartResponse> getCartsByUserId(Long userId) {
    // 获取用户的所有购物车
    List<Cart> carts = cartRepository.findByUserId(userId);

    if (carts.isEmpty()) {
      return Collections.emptyList();
    }

    // 获取所有购物车ID
    List<Long> cartIds = carts.stream().map(Cart::getId).toList();

    // 批量获取所有购物车项
    List<CartItem> allCartItems = cartItemRepository.findByCartIdsWithProduct(cartIds);

    // 按购物车ID分组
    Map<Long, List<CartItem>> cartItemsMap =
        allCartItems.stream().collect(Collectors.groupingBy(item -> item.getCart().getId()));

    // 构建响应
    return carts.stream().map(cart -> {
      List<CartItem> items = cartItemsMap.getOrDefault(cart.getId(), Collections.emptyList());
      return cartMapper.toCartResponse(cart, items);
    }).toList();
  }

  @Override
  public List<CartResponse> getCartsByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return getCartsByUserId(user.getId());
  }

  @Override
  @Transactional
  public CartResponse deleteCartItem(String username, Long cartId, Long cartItemId) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new EntityNotFoundException("CartItem", cartItemId));

    Cart cart = cartItem.getCart();

    // 权限校验
    if (!cart.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    if (!cart.getId().equals(cartId)) {
      throw new BusinessException("购物车项与购物车不匹配");
    }

    cartItemRepository.delete(cartItem);

    // 更新购物车时间
    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    return getCartResponse(cartRepository.save(cart));
  }

  @Override
  @Transactional
  public CartResponse updateCartItem(String username, Long cartId, Long cartItemId,
      UpdateCartItemRequest request) {
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new EntityNotFoundException("CartItem", cartItemId));

    Cart cart = cartItem.getCart();

    // 权限校验
    if (!cart.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    if (!cart.getId().equals(cartId)) {
      throw new BusinessException("购物车项与购物车不匹配");
    }

    Product product = cartItem.getProduct();

    // 库存校验
    if (product.getStock() < request.quantity()) {
      throw new BusinessException(product.getName() + " 库存不足");
    }

    cartItem.setQuantity(request.quantity());

    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    return getCartResponse(cartRepository.save(cart));
  }

  @Override
  @Transactional
  public CartResponse clearCart(String username, Long cartId) {
    Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new EntityNotFoundException("购物车", cartId));

    // 权限校验
    if (!cart.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    cartItemRepository.deleteAllByCartId(cartId);

    // 更新购物车时间
    cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    return getCartResponse(cartRepository.save(cart));
  }
}
