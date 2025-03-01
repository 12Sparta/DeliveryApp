package com.example.delivery.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_state")
    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //메뉴 ID

    //가게 ID

    //유저 ID


    //메뉴,가게,유저 추가하기
    public Order(String state) {
        this.state = state;
    }

    //기본 생성자
    public Order() {

    }


}
