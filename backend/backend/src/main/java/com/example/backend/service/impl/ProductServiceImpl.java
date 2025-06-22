package com.example.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.CreateProductRequest;
import com.example.backend.dto.request.UpdateProductRequest;
import com.example.backend.dto.response.ProductCardResponse;
import com.example.backend.dto.response.ProductResponse;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public List<ProductCardResponse> getAllProducts() {
    return productMapper.toProductCardsResponse(productRepository.findAll());
  }

  @Override
  public ProductResponse getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product", id));

    return productMapper.toProductResponse(product);
  }

  @Override
  @Transactional
  public ProductResponse createProduct(CreateProductRequest request) {
    Product product = new Product();
    product.setName(request.name());
    product.setImage(request.image());
    product.setPrice(request.price());
    product.setDescription(request.description());
    product.setCategory(request.category());
    product.setStock(request.stock());
    product.setRating(request.rating());
    return productMapper.toProductResponse(productRepository.save(product));
  }

  @Override
  @Transactional
  public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product", id));

    // 更新字段，仅当字段不为空时
    if (request.name() != null && !request.name().isEmpty()) {
      product.setName(request.name());
    }
    if (request.image() != null && !request.image().isEmpty()) {
      product.setImage(request.image());
    }
    if (request.price() != null) {
      product.setPrice(request.price());
    }
    if (request.stock() != null) {
      product.setStock(request.stock());
    }
    if (request.description() != null && !request.description().isEmpty()) {
      product.setDescription(request.description());
    }
    if (request.category() != null && !request.category().isEmpty()) {
      product.setCategory(request.category());
    }
    if (request.rating() != null) {
      product.setRating(request.rating());
    }

    return productMapper.toProductResponse(productRepository.saveAndFlush(product));
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}
