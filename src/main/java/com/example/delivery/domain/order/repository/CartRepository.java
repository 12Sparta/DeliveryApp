package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.id = :loginuserId")
    Optional<Cart> findByUserId(Long loginuserId);

    @Query("SELECT c FROM Cart c WHERE c.id = :cartId AND c.user.id = :loginuserId")
    Optional<Cart> findByIdAndUserId(Long cartId, long loginUserId);

    void deleteByUpdatedAtBefore(LocalDateTime expiredTime);
}
