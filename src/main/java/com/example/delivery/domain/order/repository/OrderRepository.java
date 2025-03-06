package com.example.delivery.domain.order.repository;

import com.example.delivery.common.Status;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query("SELECT o.menu FROM Order o WHERE o.id = :orderId")
  Optional<Menu> findMenuByOrderId(@Param("orderId") Long orderId);

  @Query("SELECT o FROM Order o WHERE o.cart.id = : cartId AND o.user.id = :loginUserId")
  List<Order> findByCartIdAndUserId(Long cartId, long loginUserId);

  @Modifying
  @Query("DELETE FROM Order o WHERE o.cart.id = :cartId AND o.user.id = :userId AND o.status = :status")
  void deleteByCartIdAndUserId(Long cartId, long userId, Status status);
}
