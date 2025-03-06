package com.example.delivery.domain.order.entity;

import com.example.delivery.common.Status;
import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

import static com.example.delivery.common.Status.CHECKING;
import static com.example.delivery.common.Status.PENDING;

@Entity
@Getter
@Table(name = "orders")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "order_state")
    private Status status = PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //메뉴 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    //가게 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
    private Store store;

    //유저 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    //장바구니 ID
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    public Order(Menu menu, Store store, User user) {
        this.menu = menu;
        this.store = store;
        this.user = user;
    }

    public Order(Status status, Menu menu, Store store, User user, Cart cart) {
        this.status = status;
        this.menu = menu;
        this.store = store;
        this.user = user;
        this.cart = cart;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Status status, Menu menu, Store store, User user) {
        this.status = status;
        this.menu = menu;
        this.store = store;
        this.user = user;

    }

    //기본 생성자
    public Order() {

    }


}
