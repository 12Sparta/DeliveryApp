package com.example.delivery.domain.review.dto.response;

import com.example.delivery.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private final Long id; // 리뷰 id
    private final String reviewerName; // 작성자 이름
    private final Integer rating; // 별점
    private final String content; // 리뷰 내용
    private final LocalDateTime createdAt; // 생성일

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.reviewerName = review.getUser().getName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}