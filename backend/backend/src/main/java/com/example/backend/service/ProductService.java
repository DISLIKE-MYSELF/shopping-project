package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.CreateProductRequest;
import com.example.backend.dto.request.UpdateProductRequest;
import com.example.backend.dto.response.ProductCardResponse;
import com.example.backend.dto.response.ProductResponse;

public interface ProductService {
  List<ProductCardResponse> getAllProducts();

  ProductResponse getProductById(Long id);

  ProductResponse createProduct(CreateProductRequest request);

  ProductResponse updateProduct(Long id, UpdateProductRequest request);

  void deleteProduct(Long id);
}
