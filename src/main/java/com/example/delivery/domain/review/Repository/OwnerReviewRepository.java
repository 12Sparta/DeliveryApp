package com.example.delivery.domain.review.Repository;

import com.example.delivery.domain.review.Entity.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
}
