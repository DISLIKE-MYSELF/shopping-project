package com.example.backend.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import com.example.backend.dto.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex,
      WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    ErrorResponse errorResponse =
        new ErrorResponse("EntityNotFoundException", ex.getMessage(), path, null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex,
      WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    ErrorResponse errorResponse =
        new ErrorResponse("BusinessException", ex.getMessage(), path, null);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex,
      WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    ErrorResponse errorResponse =
        new ErrorResponse("UnauthorizedException", ex.getMessage(), path, null);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    ErrorResponse errorResponse =
        new ErrorResponse("UnauthorizedException", "请求体缺失或无法解析，请确保发送了有效的请求体！", path, null);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e,
      WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    List<String> details = new ArrayList<>();

    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      details.add(fieldName + ": " + errorMessage);
    });

    ErrorResponse errorResponse =
        new ErrorResponse("ValidationException", "Validation failed", path, details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
    String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

    ErrorResponse errorResponse = new ErrorResponse("UnknownError", ex.getMessage(), path, null);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
