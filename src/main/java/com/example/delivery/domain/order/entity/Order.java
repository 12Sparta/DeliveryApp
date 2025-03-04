package com.example.delivery.domain.order.entity;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.login.entity.User;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    //가게 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
    private Store store;

    //유저 ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    //메뉴,가게,유저 추가하기
    public Order(String state, Menu menu, Store store, User user) {
        this.state = state;
        this.menu = menu;
        this.store = store;
        this.user = user;
    }

    //기본 생성자
    public Order() {

    }


}
