package com.example.delivery.domain.review.repository;

import com.example.delivery.domain.review.entity.Review;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByStoreId(Long storeId, Pageable pageable);

    Page<Review> findByStoreIdAndRatingBetween(Long storeId, Integer minRating, Integer maxRating, Pageable pageable);
}
