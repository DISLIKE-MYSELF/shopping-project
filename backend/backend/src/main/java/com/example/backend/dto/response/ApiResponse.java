package com.example.backend.dto.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
  private T data;

  public static <T> ApiResponse<T> of(T data) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setData(data);
    return response;
  }
}
