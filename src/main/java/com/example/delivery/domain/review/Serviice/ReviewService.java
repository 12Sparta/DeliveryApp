package com.example.delivery.domain.review.Serviice;

import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.review.Dto.RequestDto.ReviewRequestDto;
import com.example.delivery.domain.review.Dto.ResponseDto.ReviewResponseDto;
import com.example.delivery.domain.review.Entity.Review;
import com.example.delivery.domain.review.Repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
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

//    public Page<ReviewResponseDto> findReviews(Long storeId, int page, int size, OrderBy orderBy, Sort.Direction direction) {
//    }
//
//    public void deleteById(Long reviewId) {
//    }

}
