package com.example.delivery.domain.review.repository;

import com.example.delivery.domain.review.entity.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
}
