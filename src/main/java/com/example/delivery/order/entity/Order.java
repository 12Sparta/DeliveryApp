package com.example.delivery.order.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "orders")
public class Order {
    @Id
    Long id;

}
