package com.example.delivery.review.serviice;

import com.example.delivery.review.dto.RequestDto.ReviewRequestDto;
import com.example.delivery.review.dto.ResponseDto.ReviewResponseDto;
import com.example.delivery.review.entity.Review;
import com.example.delivery.review.repository.ReviewRepository;
import jakarta.persistence.OrderBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
               dto.getUsername(),
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

    public Page<ReviewResponseDto> findReviews(Long storeId, int page, int size, OrderBy orderBy, Sort.Direction direction) {
    }

    public void deleteById(Long reviewId) {
    }

}
