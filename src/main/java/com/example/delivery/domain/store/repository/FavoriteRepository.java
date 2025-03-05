package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f WHERE f.user.id = :loginedId AND f.store.id = :storeId")
    Optional<Favorite> findByUserIdAndStoreId(Long loginedId, Long storeId);
}
