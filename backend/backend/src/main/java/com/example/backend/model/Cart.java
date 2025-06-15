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
@Table(name = "carts")
@Data
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与用户关联
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 更新时间
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  public void prePersist() {
    if (updatedAt == null) {
      updatedAt = new Timestamp(System.currentTimeMillis());
    }
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = new Timestamp(System.currentTimeMillis());
  }
}
