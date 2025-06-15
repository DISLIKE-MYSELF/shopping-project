package com.example.backend.exception;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String entity, Long id) {
    super("未找到具有id:" + id + "的" + entity);
  }

  public EntityNotFoundException(String entity, String property) {
    super("未找到具有属性:" + property + "的" + entity);
  }
}
