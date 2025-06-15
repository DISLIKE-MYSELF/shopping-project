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
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与购物车关联
  @ManyToOne
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  // 与商品关联
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  // 商品购买数量
  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private Integer quantity;

  // 创建时间
  @Column(name = "created_at")
  private Timestamp createdAt;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = new Timestamp(System.currentTimeMillis());
    }
  }
}
