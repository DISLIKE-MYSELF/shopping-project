package com.example.backend.model;

public enum OrderStatus {
  PENDING, // 待处理
  SHIPPED, // 已发货
  DELIVERED, // 已送达
  CANCELLED, // 已取消
  COMPELETED; // 已退货

  public static OrderStatus fromString(String status) {
    for (OrderStatus value : OrderStatus.values()) {
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
