package com.example.backend.service;

import com.example.backend.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(Long userId);
    Order createOrder(Order order);
    Optional<Order> getOrderById(Long id);
    Order updateStatus(Long id, String status);
    void deleteOrder(Long id);
}
