package com.example.delivery.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // 주문 상태 (배달 완료, 배달 중 등)

    public boolean isComplete() {
        return this.status == OrderStatus.COMPLETED;
    }
}
