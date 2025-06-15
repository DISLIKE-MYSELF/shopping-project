package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.CreateOrderRequest;
import com.example.backend.dto.request.UpdateOrderRequest;
import com.example.backend.dto.response.OrderResponse;
import com.example.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // 获取用户所有订单
  @GetMapping("/my-orders")
  public ResponseEntity<List<OrderResponse>> getAllOrders(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(orderService.getOrdersByUsername(userDetails.getUsername()));
  }

  // 创建订单
  @PostMapping("/my-orders")
  public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid CreateOrderRequest request) {
    return ResponseEntity.ok(orderService.createOrder(userDetails.getUsername(), request));
  }


  // 更新订单状态
  @PostMapping("/{orderId}/status")
  public ResponseEntity<OrderResponse> updateOrderItem(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId,
      @RequestBody @Valid UpdateOrderRequest request) {
    return ResponseEntity
        .ok(orderService.updateOrderStatus(userDetails.getUsername(), orderId, request));
  }

  // 删除订单
  @DeleteMapping("/{orderId}")
  public ResponseEntity<?> deleteOrder(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long orderId) {
    orderService.deleteOrderById(userDetails.getUsername(), orderId);
    return ResponseEntity.noContent().build();
  }
}
