package com.example.backend.service.impl;

import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public Payment createPayment(Payment payment) {
        Order order = orderRepository.findById(payment.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        payment.setOrder(order);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updateStatus(Long id, String status) {
        return paymentRepository.findById(id)
                .map(p -> {
                    p.setStatus(status);
                    return paymentRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
