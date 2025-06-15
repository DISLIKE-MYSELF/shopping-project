package com.example.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与订单建立关联
  @OneToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  // 订单金额
  @Column(nullable = false, precision = 20, scale = 2)
  private BigDecimal amount;

  // 支付方式
  @Column(name = "payment_method", length = 50)
  private String paymentMethod;

  // 支付状态
  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  PaymentStatus status;

  // 支付时间
  @Column(name = "payment_time")
  private LocalDateTime paymentTime;

  // 创建时间
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // 更新时间
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    if (status == null) {
      status = PaymentStatus.PENDING;
    }
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    if (updatedAt == null) {
      updatedAt = createdAt;
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
    if (this.status.equals(PaymentStatus.PAIED)) {
      this.paymentTime = updatedAt;
    }
  }
}
