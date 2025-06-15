package com.example.backend.model;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与用户表建立多对一关系
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 收货地址
  @Column(nullable = false, length = 255)
  private String address;

  // 订单状态
  @Column(length = 20)
  private String status;

  // 创建时间
  @Column(name = "created_at")
  private Timestamp createdAt;

  // 更新时间
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  public void prePersist() {
    if (status == null) {
      status = "pending";
    }
    if (createdAt == null) {
      createdAt = new Timestamp(System.currentTimeMillis());
    }
    if (updatedAt == null) {
      updatedAt = createdAt;
    }
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = new Timestamp(System.currentTimeMillis());
  }
}
