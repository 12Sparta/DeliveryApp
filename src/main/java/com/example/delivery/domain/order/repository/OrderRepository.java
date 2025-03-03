package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query(value = "SELECT m.* FROM orders o JOIN menus m ON o.menu_id = m.id WHERE o.id = :orderId", nativeQuery = true)
  Menu findMenuByOrderId(@Param("orderId") Long orderId);
}
