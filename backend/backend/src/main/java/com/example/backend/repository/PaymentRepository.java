package com.example.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  List<Payment> findByOrderId(Long orderId);
}
