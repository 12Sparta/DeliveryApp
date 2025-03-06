package com.example.delivery.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.delivery.common.Status;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.review.dto.request.ReviewRequestDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private OrderRepository orderRepository;

    private User user;
    private Store store;
    private Order order;
    private ReviewRequestDto requestDto;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        store = new Store();
        order = new Order();
        order.setStatus(Status.DELIVERY_COMPLETED);
        requestDto = new ReviewRequestDto(5, "좋은 리뷰입니다.");
        review = new Review(store, user, requestDto.getRating(), requestDto.getContent());
    }

    @Test
    void 리뷰_생성_정상_작동_확인() {
        // Given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // When & Then
        assertDoesNotThrow(() -> reviewService.save(1L, 1L, 1L, requestDto));
    }

    @Test
    void 리뷰_삭제_리뷰_없음() {
        // Given
        given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reviewService.deleteById(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("리뷰가 존재하지 않습니다.");
    }

    @Test
    void 리뷰_조회_상점_없음() {
        // Given
        given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reviewService.findReviews(1L, 0, 10, 1, 5, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("가게를 찾을 수 없습니다.");
    }

    @Test
    void 리뷰_삭제_본인_아님() {
        // Given
        User anotherUser = new User();
        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

        // When & Then
        assertThatThrownBy(() -> reviewService.deleteById(2L, 1L))  // 다른 사용자 ID로 삭제 시도
                .isInstanceOf(RuntimeException.class)
                .hasMessage("리뷰 작성자가 아닙니다.");
    }


    @Test
    void 리뷰_삭제_정상_작동() {
        // Given
        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

        // When
        reviewService.deleteById(1L, 1L);  // 사용자 ID와 리뷰 작성자 ID가 같을 때 삭제 시도

        // Then
        Mockito.verify(reviewRepository).delete(review);  // delete 메서드가 호출됐는지 검증
    }
}

