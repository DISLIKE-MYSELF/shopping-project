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
@Table(name = "favorite_items")
@Data
public class FavoriteItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与收藏夹关联
  @ManyToOne
  @JoinColumn(name = "favorite_id", nullable = false)
  private Favorite favorite;

  // 与商品关联
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  // 收藏时间
  @Column(name = "created_at")
  private Timestamp createdAt;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = new Timestamp(System.currentTimeMillis());
    }
  }
}
