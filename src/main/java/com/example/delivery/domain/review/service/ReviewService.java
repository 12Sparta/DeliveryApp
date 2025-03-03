package com.example.delivery.domain.review.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.login.entity.User;
import com.example.delivery.domain.review.dto.RequestDto.ReviewRequestDto;
import com.example.delivery.domain.review.dto.ResponseDto.ReviewResponseDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ReviewResponseDto save(Long userId, Long storeId, ReviewRequestDto dto) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("사용자를 찾을 수 없습니다.")
        );

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new RuntimeException("가게를 찾을 수 없습니다.")
        );

        Review review = new Review(
                user,
                store,
                user.getUsername(),
                dto.getRating(),
                dto.getContent()
        );

        reviewRepository.save(review);

        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getUsername(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findReviews(Long storeId, int page, int size, Sort sort) {
        Page<Review> reviews = reviewRepository.findByStoreId(
                storeId,
                PageRequest.of(page -1, size, sort)
        );

        return reviews.stream()
                .map(r -> new ReviewResponseDto(
                        r.getId(),
                        r.getUser().getId(),
                        r.getUsername(),
                        r.getRating(),
                        r.getContent(),
                        r.getCreatedAt()
                )).toList();
    }

    @Transactional
    public void deleteById(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalStateException("리뷰가 존재하지 않습니다.")
        );

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("리뷰 작성자가 아닙니다.");
        }

        reviewRepository.delete(review);
    }

}