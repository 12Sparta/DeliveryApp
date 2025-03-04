package com.example.delivery.domain.store.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalTime openedAt;
    private LocalTime closedAt;
    private LocalDateTime deletedAt = null;

    public Store(User user, String storeName, int orderMin, String about, LocalTime openedAt, LocalTime closedAt) {
        this.user = user;
        this.storeName = storeName;
        this.orderMin = orderMin;
        this.about = about;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
    }

    public void save(String storeName, LocalTime openedAt, LocalTime closedAt, int orderMin, String about){
        this.storeName = storeName;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.orderMin = orderMin;
        this.about = about;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }
}
