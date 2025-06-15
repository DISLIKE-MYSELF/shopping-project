package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.CreateOrderRequest;
import com.example.backend.dto.request.UpdateOrderRequest;
import com.example.backend.dto.response.OrderResponse;

public interface OrderService {
  OrderResponse createOrder(String username, CreateOrderRequest request);

  void deleteOrderById(String username, Long OrderId);

  List<OrderResponse> getOrdersByUserId(Long userId);

  List<OrderResponse> getOrdersByUsername(String username);

  OrderResponse updateOrderStatus(String username, Long orderId, UpdateOrderRequest request);
}
