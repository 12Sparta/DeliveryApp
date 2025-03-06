package com.example.delivery.domain.order.entity;

import com.example.delivery.common.Status;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.example.delivery.common.Status.CHECKING;

@Entity
@Getter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "order_state")
    private Status status;

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

    public Order(Menu menu, Store store, User user) {
        this.status = CHECKING;
        this.menu = menu;
        this.store = store;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    //기본 생성자
    public Order() {

    }


}
