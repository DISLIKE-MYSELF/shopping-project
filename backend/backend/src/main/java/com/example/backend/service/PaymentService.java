package com.example.backend.service;

import com.example.backend.model.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByOrderId(Long orderId);
    Payment createPayment(Payment payment);
    Payment updateStatus(Long id, String status);
}
