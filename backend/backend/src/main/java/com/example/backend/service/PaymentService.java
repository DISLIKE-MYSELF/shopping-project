package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.CreatePaymentRequest;
import com.example.backend.dto.request.UpdatePaymentRequest;
import com.example.backend.dto.response.PaymentResponse;

public interface PaymentService {
  List<PaymentResponse> getPaymentsByUsername(String username);

  PaymentResponse getPaymentById(String username, Long id);

  PaymentResponse getPaymentByOrderId(String username, Long orderId);

  PaymentResponse createPayment(String username, CreatePaymentRequest request);

  PaymentResponse updatePaymentStatus(String username, Long id, UpdatePaymentRequest request);

  void deletePaymentById(String username, Long id);
}
