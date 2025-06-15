package com.example.backend.model;

import java.time.LocalDateTime;
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
@Table(name = "emails")
@Data
public class Email {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 与用户建立关联
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // 邮件主题
  @Column(nullable = false, length = 255)
  private String subject;

  // 邮件内容
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  // 发送时间
  @Column(name = "sent_at")
  private LocalDateTime sentAt;

  @PrePersist
  public void prePersist() {
    if (sentAt == null) {
      sentAt = LocalDateTime.now();
    }
  }
}
