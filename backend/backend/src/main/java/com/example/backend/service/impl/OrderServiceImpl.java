package com.example.backend.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.CreateOrderRequest;
import com.example.backend.dto.request.UpdateOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.OrderMapper;
import com.example.backend.model.CartItem;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.OrderStatus;
import com.example.backend.model.User;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.OrderItemRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final OrderMapper orderMapper;

  // 将订单转为响应
  @Transactional
  private OrderResponse getOrderResponse(Order order) {

    List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithProduct(order.getId());

    return orderMapper.toOrderResponse(order, orderItems);
  }

  @Override
  @Transactional
  public OrderResponse createOrder(String username, CreateOrderRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    String address = request.address();
    if (address == null || address.isEmpty()) {
      if (user.getAddress() == null || user.getAddress().isEmpty()) {
        throw new BusinessException("请填写收货地址");
      } else {
        address = user.getAddress();
      }
    }

    // 创建订单
    Order order = new Order();
    order.setUser(user);
    order.setAddress(address);
    orderRepository.save(order);

    // 将购物车项转为订单项
    List<CartItem> cartItems = cartItemRepository.findByIdsWithProduct(request.cartItemIds());
    List<OrderItem> orderItems = orderMapper.mapCartItemsToOrderItems(order, cartItems);
    orderItemRepository.saveAll(orderItems);

    return getOrderResponse(orderRepository.save(order));
  }

  @Override
  @Transactional
  public void deleteOrderById(String username, Long orderId) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 验证订单存在性
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

    // 权限校验（当前订单的用户ID与请求的用户ID匹配）
    if (!order.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    orderItemRepository.deleteAllByOrderId(orderId);
    orderRepository.deleteById(orderId);
  }

  @Override
  public List<OrderResponse> getOrdersByUserId(Long userId) {
    // 获取用户的所有订单
    List<Order> orders = orderRepository.findByUserId(userId);

    if (orders.isEmpty()) {
      return Collections.emptyList();
    }

    // 获取所有订单ID
    List<Long> orderIds = orders.stream().map(Order::getId).toList();

    // 批量获取所有订单项
    List<OrderItem> allOrderItems = orderItemRepository.findByOrderIdsWithProduct(orderIds);

    // 按订单ID分组
    Map<Long, List<OrderItem>> orderItemsMap =
        allOrderItems.stream().collect(Collectors.groupingBy(item -> item.getOrder().getId()));

    // 构建响应
    return orders.stream().map(order -> {
      List<OrderItem> items = orderItemsMap.getOrDefault(order.getId(), Collections.emptyList());
      return orderMapper.toOrderResponse(order, items);
    }).toList();
  }

  @Override
  public List<OrderResponse> getOrdersByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return getOrdersByUserId(user.getId());
  }

  @Override
  @Transactional
  public OrderResponse updateOrderStatus(String username, Long orderId,
      UpdateOrderRequest request) {
    // 验证用户存在性
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User", username);

    // 验证订单存在性
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

    // 权限校验（当前订单的用户ID与请求的用户ID匹配）
    if (!order.getUser().getUsername().equals(username)) {
      throw new UnauthorizedException("无权操作");
    }

    OrderStatus status = OrderStatus.fromString(request.status());

    if (status == null) {
      throw new BusinessException("未知订单状态：" + request.status());
    }

    order.setStatus(status);
    return getOrderResponse(orderRepository.save(order));
  }
}
