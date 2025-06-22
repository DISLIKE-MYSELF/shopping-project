package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.CreateProductRequest;
import com.example.backend.dto.request.UpdateProductRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.ProductCardResponse;
import com.example.backend.dto.response.ProductResponse;
import com.example.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductCardResponse>>> getAllProducts() {
    return ResponseEntity.ok(ApiResponse.of(productService.getAllProducts()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.of(productService.getProductById(id)));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
      @RequestBody @Valid CreateProductRequest request) {
    return ResponseEntity.ok(ApiResponse.of(productService.createProduct(request)));
  }

  @PostMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id,
      @RequestBody @Valid UpdateProductRequest request) {
    return ResponseEntity.ok(ApiResponse.of(productService.updateProduct(id, request)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}

