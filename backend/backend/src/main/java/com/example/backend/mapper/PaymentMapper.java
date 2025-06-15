package com.example.backend.mapper;

import org.springframework.stereotype.Component;
import com.example.backend.dto.response.PaymentResponse;
import com.example.backend.model.Payment;

@Component
public class PaymentMapper {

  public PaymentResponse toPaymentResponse(Payment payment) {
    return new PaymentResponse(payment.getId(), payment.getAmount(), payment.getPaymentMethod(),
        payment.getStatus().toString(), payment.getPaymentTime(), payment.getCreatedAt(),
        payment.getUpdatedAt());
  }
}
