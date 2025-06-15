package com.example.backend.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 商品名
  @Column(nullable = false, length = 100)
  private String name;

  // 商品价格
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  // 商品描述
  @Column(columnDefinition = "TEXT")
  private String description;

  // 商品类别
  @Column(nullable = false, length = 50)
  private String category;

  // 商品库存
  @Column(nullable = false)
  private Integer stock;

  // 商品图片
  @Column(length = 50)
  private String image;

  // 商品评分
  @Column(precision = 2, scale = 1)
  private BigDecimal rating;

  // 创建时间
  @Column(name = "created_at")
  private Timestamp createdAt;

  // 更新时间
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  public void prePersist() {
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
