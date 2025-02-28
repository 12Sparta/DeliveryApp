package com.example.delivery.domain.store.entity;

import com.example.delivery.domain.common.entity.Timestamped;
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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onwer_id", nullable = false)
    private User user;

    private String storeName;
    private int orderMin;
    private String about;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
}
