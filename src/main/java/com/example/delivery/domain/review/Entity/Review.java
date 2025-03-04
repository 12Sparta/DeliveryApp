package com.example.delivery.domain.review.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 리뷰 id

    private String username; // 리뷰 작성자

    private Integer rating; // 별점 (1~5점)

    private String content; // 리뷰 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰를 작성한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 리뷰가 달린 가게

    @CreationTimestamp
    private LocalDateTime createdAt; // 생성시간 자동으로 설정

    public Review(User user, Store store, String username, Integer rating, String content) {
        this.user = user;
        this.store = store;
        this.username = username;
        this.rating = rating;
        this.content = content;
    }

}

