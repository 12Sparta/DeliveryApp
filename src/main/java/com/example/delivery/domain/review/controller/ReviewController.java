package com.example.delivery.domain.review.controller;

import com.example.delivery.domain.common.OrderBy;
import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestBody @Valid ReviewRequestDto dto //유효성 검사 적용
    ) {
        return ResponseEntity.ok(reviewService.save(userId, storeId, orderId, dto));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "1") int minRating,
            @RequestParam(defaultValue = "5") int maxRating,
            @RequestParam(defaultValue = "CREATED_AT") OrderBy orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {

        if (minRating < 1 || maxRating < 5) {
            throw new IllegalArgumentException("별점은 최소 1점, 최대 5점이어야 합니다.");
        }

        if (minRating > maxRating) {
            throw new IllegalArgumentException("최소 별점은 최대 별점보다 작아야합니다.");
        }

        Page<ReviewResponseDto> reviews = reviewService.findReviews(storeId, page, size, minRating, maxRating, orderBy, direction);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/stores/{storeId}/{reviewId}")
    public ResponseEntity<Void> Reply(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @ModelAttribute com.example.delivery.domain.review.dto.request.ReplyRequestDto dto){

        Long loginedId = 1L;

        reviewService.createReply(storeId, reviewId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{ownerReviewId}")
    public ResponseEntity<Void> updateReply(
            @RequestHeader("Authorization") String token,
            @PathVariable Long ownerReviewId,
            @ModelAttribute com.example.delivery.domain.review.dto.request.ReplyRequestDto dto){

        Long loginedId = 1L;

        reviewService.updateReply(ownerReviewId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{ownerReviewId}")
    public ResponseEntity<Void> deleteReply(
            @RequestHeader("Authorization") String token,
            @PathVariable Long ownerReviewId){

        Long loginedId = 1L;

        reviewService.deleteReply(ownerReviewId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @RequestParam Long userId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteById(userId, reviewId);
        return ResponseEntity.noContent().build();
    }
}


