package com.example.delivery.domain.review.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    void 리뷰_생성_정상() {
        // given
        Long loginedUserId = 1L;
        Long storeId = 1L;
        ReviewRequestDto dto = new ReviewRequestDto(5, "짱맛있어요");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.CUSTOMER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0));

        given(userRepository.findById(loginedUserId)).willReturn(Optional.of(user)); // 유저 존재 여부 확인
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store)); // 가게 존재 여부 확인
        given(reviewRepository.existsByStoreIdAndUserId(storeId, loginedUserId)).willReturn(false); // 리뷰 중복 체크

        // when
        assertDoesNotThrow(() -> reviewService.createReview(loginedUserId, storeId, dto));  // 예외 발생 시 실패

        // then
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());  // 리뷰 저장 메서드 호출 여부 확인

        Review savedReview = reviewCaptor.getValue();
        assertEquals(dto.getRating(), savedReview.getRating());
        assertEquals(dto.getComment(), savedReview.getComment());
    }

    @Test
    void 리뷰_생성_이미_리뷰가_있는_경우() {
        // given
        Long loginedUserId = 1L;
        Long storeId = 1L;
        CreateReviewDto dto = new CreateReviewDto(5, "Great service!");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.CUSTOMER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0));

        given(userRepository.findById(loginedUserId)).willReturn(Optional.of(user)); // 유저 존재 여부 확인
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store)); // 가게 존재 여부 확인
        given(reviewRepository.existsByStoreIdAndUserId(storeId, loginedUserId)).willReturn(true); // 이미 리뷰가 존재

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> reviewService.createReview(loginedUserId, storeId, dto));
        assertEquals("You have already reviewed this store", exception.getMessage());
    }

    @Test
    void 리뷰_조회_상점_없는_경우() {
        // given
        Long storeId = 1L;

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());  // 상점이 존재하지 않음

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> reviewService.getReviews(storeId));
        assertEquals("Store not found", exception.getMessage());
    }

    @Test
    void 리뷰_조회_상점_있을_경우() {
        // given
        Long storeId = 1L;
        Store store = new Store(new User(1L, "owner", "owner@test.com", "ownerpw", Role.OWNER, "testAddress"), "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0));
        List<Review> reviews = List.of(new Review(store, new User(2L, "customer", "customer@test.com", "customerpw", Role.CUSTOMER, "customerAddress"), 5, "Great service!"));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));  // 상점 존재 여부 확인
        given(reviewRepository.findByStoreId(storeId)).willReturn(reviews);  // 리뷰 조회

        // when
        List<ReviewResponseDto> result = reviewService.getReviews(storeId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Great service!", result.get(0).getComment());
    }

    @Test
    void 리뷰_수정_정상() {
        // given
        Long loginedUserId = 2L;
        Long reviewId = 1L;
        Review review = new Review(new Store(new User(1L, "owner", "owner@test.com", "ownerpw", Role.OWNER, "testAddress"), "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0)),
                new User(loginedUserId, "customer", "customer@test.com", "customerpw", Role.CUSTOMER, "customerAddress"), 5, "Good service");
        review.setId(reviewId);
        String updatedComment = "Updated review!";

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        assertDoesNotThrow(() -> reviewService.updateReview(loginedUserId, reviewId, updatedComment));

        // then
        assertEquals(updatedComment, review.getComment());
    }

    @Test
    void 리뷰_수정_본인_리뷰가_아닌_경우() {
        // given
        Long loginedUserId = 3L;  // 본인 리뷰가 아님
        Long reviewId = 1L;
        Review review = new Review(new Store(new User(1L, "owner", "owner@test.com", "ownerpw", Role.OWNER, "testAddress"), "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0)),
                new User(2L, "customer", "customer@test.com", "customerpw", Role.CUSTOMER, "customerAddress"), 5, "Good service");
        review.setId(reviewId);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> reviewService.updateReview(loginedUserId, reviewId, "Updated review!"));
        assertEquals("You can only update your own review", exception.getMessage());
    }

    @Test
    void 리뷰_삭제_정상() {
        // given
        Long loginedUserId = 2L;
        Long reviewId = 1L;
        Review review = new Review(new Store(new User(1L, "owner", "owner@test.com", "ownerpw", Role.OWNER, "testAddress"), "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0)),
                new User(loginedUserId, "customer", "customer@test.com", "customerpw", Role.CUSTOMER, "customerAddress"), 5, "Good service");
        review.setId(reviewId);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        assertDoesNotThrow(() -> reviewService.deleteReview(loginedUserId, reviewId));

        // then
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void 리뷰_삭제_본인_리뷰가_아닌_경우() {
        // given
        Long loginedUserId = 3L;  // 본인 리뷰가 아님
        Long reviewId = 1L;
        Review review = new Review(new Store(new User(1L, "owner", "owner@test.com", "ownerpw", Role.OWNER, "testAddress"), "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0)),
                new User(2L, "customer", "customer@test.com", "customerpw", Role.CUSTOMER, "customerAddress"), 5, "Good service");
        review.setId(reviewId);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> reviewService.deleteReview(loginedUserId, reviewId));
        assertEquals("You can only delete your own review", exception.getMessage());
    }
}
