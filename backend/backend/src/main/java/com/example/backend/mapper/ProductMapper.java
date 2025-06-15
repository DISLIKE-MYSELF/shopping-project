package com.example.backend.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.example.backend.dto.response.ProductCardResponse;
import com.example.backend.dto.response.ProductCardsResponse;
import com.example.backend.dto.response.ProductResponse;
import com.example.backend.model.Product;

@Component
public class ProductMapper {
  public ProductCardResponse toProductCardResponse(Product product) {
    return new ProductCardResponse(product.getId(), product.getName(), product.getImage(),
        product.getPrice(), product.getStock());
  }

  public ProductCardsResponse toProductCardsResponse(List<Product> products) {
    List<ProductCardResponse> productCards =
        products.stream().map(this::toProductCardResponse).toList();
    return new ProductCardsResponse(productCards);
  }

  public ProductResponse toProductResponse(Product product) {
    return new ProductResponse(product.getId(), product.getName(), product.getImage(),
        product.getPrice(), product.getStock(), product.getDescription(), product.getCategory(),
        product.getRating(), product.getCreatedAt(), product.getUpdatedAt());
  }
}
