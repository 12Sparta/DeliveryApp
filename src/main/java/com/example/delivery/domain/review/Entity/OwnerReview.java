package com.example.delivery.domain.review.Entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "owner_review")
public class OwnerReview extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String content;

    public OwnerReview(Store store, Review review, String content) {
        this.store = store;
        this.review = review;
        this.content = content;
    }
}
