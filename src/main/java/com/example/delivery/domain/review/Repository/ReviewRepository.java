package com.example.delivery.domain.review.Repository;

import com.example.delivery.domain.review.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
