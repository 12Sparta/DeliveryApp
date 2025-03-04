package com.example.delivery.domain.review.controller;

import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.service.ReviewService;
import jakarta.validation.Valid;
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
            @RequestBody @Valid ReviewRequestDto dto //유효성 검사 적용
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
            @RequestParam(defaultValue = "CreatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") Sort.Direction direction
            ) {
        Sort sort = Sort.by(direction, sortBy);
        Page<ReviewResponseDto> reviews = reviewService.findReviews(storeId, page, size, sort);

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteById(user.getId(), reviewId);

        return ResponseEntity.noContent().build();
    }
}
