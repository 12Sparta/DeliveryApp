package com.example.delivery.domain.store.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "store")
public class Store extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onwer_id", nullable = false)
    private User user;

    private String storeName;
    private int orderMin;
    private String about;
    private LocalDateTime openedAt;

    public Store(User user, String storeName, int orderMin, String about, LocalDateTime openedAt, LocalDateTime closedAt) {
        this.user = user;
        this.storeName = storeName;
        this.orderMin = orderMin;
        this.about = about;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

    private LocalDateTime closedAt;
}
