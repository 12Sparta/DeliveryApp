package com.example.delivery.domain.review.repository;

import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 리뷰() {
        //given

        //reviewRepository.save(review);
        //when

        //then
    }

}