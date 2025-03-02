package com.example.delivery.review.controller;

import com.example.delivery.review.dto.RequestDto.ReviewRequestDto;
import com.example.delivery.review.dto.ResponseDto.ReviewResponseDto;
import com.example.delivery.review.serviice.ReviewService;
import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long storeId,
            @RequestBody ReviewRequestDto dto
    ) {
        return ResponseEntity.ok(reviewService.save(dto.getUserId(), storeId, dto));
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            @RequestParam(defaultValue = "CreatedAt") OrderBy orderBy,
            @RequestParam(defaultValue = "desc") Sort.Direction direction
            ) {
        Page<ReviewResponseDto> reviews = reviewService.findReviews(storeId, page, size, orderBy, direction);

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteById(reviewId);

        return ResponseEntity.noContent().build();
    }
}
