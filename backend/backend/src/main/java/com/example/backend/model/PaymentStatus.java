package com.example.backend.model;

public enum PaymentStatus {
  PENDING, // 待处理
  PAIED, // 已支付
  FAILED; // 支付失败

  public static PaymentStatus fromString(String status) {
    for (PaymentStatus value : PaymentStatus.values()) {
      if (value.name().equalsIgnoreCase(status)) {
        return value;
      }
    }
    return null;
  }

  public String toString() {
    return name().toLowerCase();
  }
}
