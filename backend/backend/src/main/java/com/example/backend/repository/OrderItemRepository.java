package com.example.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.backend.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  List<OrderItem> findByOrderId(Long orderId);

  Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);

  @Query("SELECT DISTINCT oi FROM OrderItem oi " + "JOIN FETCH oi.product "
      + "WHERE oi.order.id = :orderId")
  List<OrderItem> findByOrderIdWithProduct(@Param("orderId") Long orderId);

  @Query("SELECT DISTINCT oi FROM OrderItem oi " + "JOIN FETCH oi.product "
      + "WHERE oi.order.id IN :orderIds")
  List<OrderItem> findByOrderIdsWithProduct(@Param("orderIds") List<Long> orderIds);

  void deleteAllByOrderId(Long orderId);
}
