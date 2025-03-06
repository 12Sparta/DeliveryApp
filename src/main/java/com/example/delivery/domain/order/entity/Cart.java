package com.example.delivery.domain.order.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "carts")
public class Cart extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Cart(User user, Store store) {
        this.user = user;
        this.store = store;
    }
}
