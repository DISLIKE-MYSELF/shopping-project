package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.backend.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByCartId(Long cartId);

  Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

  @Query("SELECT DISTINCT ci FROM CartItem ci " + "JOIN FETCH ci.product "
      + "WHERE ci.cart.id = :cartId")
  List<CartItem> findByCartIdWithProduct(@Param("cartId") Long cartId);

  @Query("SELECT DISTINCT ci FROM CartItem ci " + "JOIN FETCH ci.product "
      + "WHERE ci.cart.id IN :cartIds")
  List<CartItem> findByCartIdsWithProduct(@Param("cartIds") List<Long> cartIds);

  @Query("SELECT DISTINCT ci FROM CartItem ci " + "JOIN FETCH ci.product "
      + "WHERE ci.id IN :cartItemIds")
  List<CartItem> findByIdsWithProduct(@Param("cartItemIds") List<Long> cartItemIds);

  void deleteAllByCartId(Long cartId);
}
