package com.example.backend.service;

import com.example.backend.dto.request.CreateProductRequest;
import com.example.backend.dto.request.UpdateProductRequest;
import com.example.backend.dto.response.ProductCardsResponse;
import com.example.backend.dto.response.ProductResponse;

public interface ProductService {
  ProductCardsResponse getAllProducts();

  ProductResponse getProductById(Long id);

  ProductResponse createProduct(CreateProductRequest request);

  ProductResponse updateProduct(Long id, UpdateProductRequest request);

  void deleteProduct(Long id);
}
