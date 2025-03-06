package com.example.delivery.domain.review.service;

import com.example.delivery.common.Status;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.common.OrderBy;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.review.dto.request.ReplyRequestDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.common.Role;
import com.example.delivery.domain.review.entity.OwnerReview;
import com.example.delivery.domain.review.repository.OwnerReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final OwnerReviewRepository ownerReviewRepository;

    @Transactional
    public ReviewResponseDto save(Long userId, Long storeId, Long orderId, ReviewRequestDto dto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게를 찾을 수 없습니다.")
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("주문을 찾을 수 없습니다.")
        );

        if (!order.getStatus().equals(Status.DELIVERY_COMPLETED)) {
            throw new ApplicationException("배달이 완료된 후 리뷰를 작성할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }


        Review review = new Review(
                user,
                store,
                dto.getRating(),
                dto.getContent()
        );

        reviewRepository.save(review);

        return new ReviewResponseDto(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findReviews(
            Long storeId, int page, int size, int minRating, int maxRating,
            OrderBy orderBy, Sort.Direction direction) {

        if (minRating < 1 || maxRating > 5) {
            throw new ApplicationException("별점은 1~5 사이입니다.", HttpStatus.BAD_REQUEST);
        }

        if (minRating > maxRating) {
            throw new ApplicationException("최소 별점은 최대 별점보다 작아야 합니다.", HttpStatus.BAD_REQUEST);
        }

        Sort sort = (orderBy == OrderBy.RATING)
                ? Sort.by(direction, "rating")
                : Sort.by(direction, "createdAt");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews = reviewRepository.findByStoreIdAndRatingBetween(storeId, minRating, maxRating, pageable);

        return reviews.map(review -> new ReviewResponseDto(review));

    }

    @Transactional
    public void createReply(Long storeId, Long reviewId, Long loginedId, ReplyRequestDto dto) {
        // 리뷰, 가게 id 확인
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId)
                .orElseThrow(() -> new ApplicationException("Store not found", HttpStatus.NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException("Review not found", HttpStatus.NOT_FOUND));

        // 리뷰 작성 자격 확인
        if(!store.getUser().getId().equals(loginedId)){
            throw new ApplicationException("Not your store", HttpStatus.FORBIDDEN);
        }

        ownerReviewRepository.save(new OwnerReview(store, review, dto.getContent()));
    }

    @Transactional
    public void deleteById(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ApplicationException("리뷰가 존재하지 않습니다.", HttpStatus.BAD_REQUEST)
        );

        if (!review.getUser().getId().equals(userId)) {
            throw new ApplicationException("리뷰 작성자가 아닙니다.", HttpStatus.FORBIDDEN);
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public void updateReply(Long ownerReviewId, Long loginedId, ReplyRequestDto dto) {

        OwnerReview review = check(ownerReviewId, loginedId);

        review.update(dto.getContent());
    }

    @Transactional
    public void deleteReply(Long ownerReviewId, Long loginedId) {

        ownerReviewRepository.delete(check(ownerReviewId, loginedId));
    }

    public OwnerReview check(Long ownerReviewId, Long loginedId){
        // 리뷰 id 확인
        OwnerReview review = ownerReviewRepository.findById(ownerReviewId)
                .orElseThrow(() -> new ApplicationException("Review not found", HttpStatus.NOT_FOUND));

        // 리뷰 수정 자격 확인
        if(!review.getStore().getUser().getId().equals(loginedId)){
            throw new ApplicationException("Not your store", HttpStatus.FORBIDDEN);
        }

        return review;
    }
}