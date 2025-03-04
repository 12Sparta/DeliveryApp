package com.example.delivery.domain.review.controller;

import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.service.ReviewService;
import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

//    @GetMapping("/stores/{storeId}/reviews")
//    public ResponseEntity<Page<ReviewResponseDto>> getReviews(
//            @PathVariable Long storeId,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "1") int minRating,
//            @RequestParam(defaultValue = "5") int maxRating,
//            @RequestParam(defaultValue = "CreatedAt") OrderBy orderBy,
//            @RequestParam(defaultValue = "desc") Sort.Direction direction
//            ) {
//        Page<ReviewResponseDto> reviews = reviewService.findReviews(storeId, page, size, orderBy, direction);
//
//        return ResponseEntity.ok(reviews);
//    }
//
//    @DeleteMapping("/reviews/{reviewId}")
//    public ResponseEntity<Void> deleteReview(
//            @PathVariable Long reviewId
//    ) {
//        reviewService.deleteById(reviewId);
//
//        return ResponseEntity.noContent().build();
//    }

    @PostMapping("/stores/{storeId}/{reviewId}")
    public ResponseEntity<Void> Reply(
            //@RequestHeader("Authorization") String token,
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @ModelAttribute ReplyRequestDto dto){

        Long loginedId = 1L;

        reviewService.createReply(storeId, reviewId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/reviews/{ownerReviewId}")
    public ResponseEntity<Void> updateReply(
            //@RequestHeader("Authorization") String token,
            @PathVariable Long ownerReviewId,
            @ModelAttribute ReplyRequestDto dto){

        Long loginedId = 1L;

        reviewService.updateReply(ownerReviewId, loginedId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{ownerReviewId}")
    public ResponseEntity<Void> deleteReply(
            //@RequestHeader("Authorization") String token,
            @PathVariable Long ownerReviewId){

        Long loginedId = 1L;

        reviewService.deleteReply(ownerReviewId, loginedId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
