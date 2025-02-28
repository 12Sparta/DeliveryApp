package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Menu findMenuById(Long orderId);
}
