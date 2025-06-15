package com.example.backend.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.example.backend.dto.response.OrderItemResponse;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.model.CartItem;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.Product;

@Component
public class OrderMapper {

  private OrderItem toOrderItem(Order order, CartItem cartItem) {
    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(cartItem.getProduct());
    orderItem.setQuantity(cartItem.getQuantity());

    return orderItem;
  }

  public List<OrderItem> mapCartItemsToOrderItems(Order order, List<CartItem> cartItems) {
    return cartItems.stream().map(cartItem -> toOrderItem(order, cartItem)).toList();
  }

  private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
    Product product = orderItem.getProduct();
    return new OrderItemResponse(orderItem.getId(), product.getName(), product.getPrice(),
        orderItem.getQuantity(), product.getImage(), orderItem.getCreatedAt());
  }

  public OrderResponse toOrderResponse(Order order, List<OrderItem> orderItems) {
    List<OrderItemResponse> itemResponses =
        orderItems.stream().map(this::toOrderItemResponse).toList();

    return new OrderResponse(order.getId(), order.getStatus().toString(), order.getAddress(),
        itemResponses, order.getCreatedAt(), order.getUpdatedAt());
  }
}
