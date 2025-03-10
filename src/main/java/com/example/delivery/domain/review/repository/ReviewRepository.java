package com.example.delivery.domain.review.repository;

import com.example.delivery.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByStoreId(Long storeId, Pageable pageable);

    Page<Review> findByStoreIdAndRatingBetween(Long storeId, Integer minRating, Integer maxRating, Pageable pageable);

    boolean existsByStoreIdAndUserId(Long storeId, Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id = :storeId")
    Optional<Double> findReviewAvg(Long storeId);
}
