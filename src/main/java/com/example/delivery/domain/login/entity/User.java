package com.example.delivery.domain.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.delivery.common.Role;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String email;
    private String password;
    private String address;

    public User(long id, String name, String email, String password, Role role, String address) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
    }
}
