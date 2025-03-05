package com.example.delivery.domain.review.Serviice;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.review.Dto.RequestDto.ReplyRequestDto;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.review.Dto.RequestDto.ReviewRequestDto;
import com.example.delivery.domain.review.Dto.ResponseDto.ReviewResponseDto;
import com.example.delivery.domain.review.Entity.OwnerReview;
import com.example.delivery.domain.review.Entity.Review;
import com.example.delivery.domain.review.Repository.OwnerReviewRepository;
import com.example.delivery.domain.review.Repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
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
    private final OwnerReviewRepository ownerReviewRepository;

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

    @Transactional
    public void createReply(Long storeId, Long reviewId, Long loginedId, ReplyRequestDto dto) {
        // 리뷰, 가게 id 확인
        Optional<Store> store = storeRepository.findByIdAndDeletedAtIsNull(storeId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(store.isEmpty() || review.isEmpty()){
            throw new ApplicationException("Store or review not found", HttpStatus.NOT_FOUND);
        }
        // 리뷰 작성 자격 확인
        Optional<User> user = userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER);
        if(user.isEmpty() || !store.get().getUser().equals(user.get())){
            throw new ApplicationException("Not your store", HttpStatus.FORBIDDEN);
        }

        ownerReviewRepository.save(new OwnerReview(store.get(), review.get(), dto.getContent()));
    }

    @Transactional
    public void updateReply(Long ownerReviewId, Long loginedId, ReplyRequestDto dto) {

        // 리뷰, 가게 id 확인
        Optional<OwnerReview> review = ownerReviewRepository.findById(ownerReviewId);
        if(review.isEmpty()){
            throw new ApplicationException("Review not found", HttpStatus.NOT_FOUND);
        }
        // 리뷰 수정 자격 확인
        Optional<User> user = userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER);
        if(user.isEmpty() || !review.get().getStore().getUser().equals(user.get())){    // 여기 수정해야 할 것 같음
            throw new ApplicationException("Not your store", HttpStatus.FORBIDDEN);
        }

        review.get().update(dto.getContent());
    }

    @Transactional
    public void deleteReply(Long ownerReviewId, Long loginedId, ReplyRequestDto dto) {
        // 리뷰, 가게 id 확인
        Optional<OwnerReview> review = ownerReviewRepository.findById(ownerReviewId);
        if(review.isEmpty()){
            throw new ApplicationException("Review not found", HttpStatus.NOT_FOUND);
        }
        // 리뷰 수정 자격 확인
        Optional<User> user = userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER);
        if(user.isEmpty() || !review.get().getStore().getUser().equals(user.get())){    // 여기 수정해야 할 것 같음
            throw new ApplicationException("Not your store", HttpStatus.FORBIDDEN);
        }

        ownerReviewRepository.delete(review.get());
    }
}
