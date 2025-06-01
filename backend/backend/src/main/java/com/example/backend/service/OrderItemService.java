package com.example.backend.service;

import com.example.backend.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getItemsByOrderId(Long orderId);
    OrderItem createOrderItem(OrderItem item);
    void deleteOrderItem(Long id);
}
