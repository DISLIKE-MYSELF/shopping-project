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
import com.example.backend.dto.request.CreatePaymentRequest;
import com.example.backend.dto.request.UpdatePaymentRequest;
import com.example.backend.dto.response.PaymentResponse;
import com.example.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  // 获取用户所有支付记录
  @GetMapping("/my-payments")
  public ResponseEntity<List<PaymentResponse>> getAllPayments(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(paymentService.getPaymentsByUsername(userDetails.getUsername()));
  }

  // 获取指定支付记录
  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponse> getPaymentById(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long paymentId) {
    return ResponseEntity.ok(paymentService.getPaymentById(userDetails.getUsername(), paymentId));
  }

  // 获取某订单的支付记录
  @GetMapping("/orders/{orderId}")
  public ResponseEntity<PaymentResponse> getPaymentByOrderId(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
    return ResponseEntity
        .ok(paymentService.getPaymentByOrderId(userDetails.getUsername(), orderId));
  }

  // 创建支付记录
  @PostMapping
  public ResponseEntity<PaymentResponse> createPayment(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid CreatePaymentRequest request) {
    return ResponseEntity.ok(paymentService.createPayment(userDetails.getUsername(), request));
  }

  // 更新支付状态
  @PostMapping("/{paymentId}/status")
  public ResponseEntity<PaymentResponse> updatePaymentStatus(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long paymentId,
      @RequestBody @Valid UpdatePaymentRequest request) {
    return ResponseEntity
        .ok(paymentService.updatePaymentStatus(userDetails.getUsername(), paymentId, request));
  }

  // 删除支付记录
  @DeleteMapping("/{paymentId}")
  public ResponseEntity<?> deletePayment(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long paymentId) {
    paymentService.deletePaymentById(userDetails.getUsername(), paymentId);
    return ResponseEntity.noContent().build();
  }
}
