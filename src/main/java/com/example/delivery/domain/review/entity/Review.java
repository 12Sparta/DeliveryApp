package com.example.delivery.domain.review.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.delivery.domain.store.entity.Store;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends Timestamped {

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

    @Setter
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private OwnerReview ownerReview;

    public Review(Store store, User user, Integer rating, String content) {
        this.store = store;
        this.user = user;
        this.rating = rating;
        this.content = content;
    }

}

