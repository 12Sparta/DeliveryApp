package com.example.delivery.domain.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.delivery.domain.store.entity.Store;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰 id

    @Range(min = 1, max = 5) //별점 검증
    @Column(nullable = false)
    private Integer rating; // 별점 (1~5점)

    @Column(nullable = false, length = 600)
    private String content; // 리뷰 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰를 작성한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 리뷰가 달린 가게

    @CreationTimestamp
    private LocalDateTime createdAt; // 생성시간 자동으로 설정

    public Review(User user, Store store, Integer rating, String content) {
        this.user = user;
        this.store = store;
        this.rating = rating;
        this.content = content;
    }

}

