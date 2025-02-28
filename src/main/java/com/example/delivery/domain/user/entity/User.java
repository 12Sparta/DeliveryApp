package com.example.delivery.domain.user.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import com.example.delivery.domain.user.common.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "store")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    private Enum<Role> role;
    private String address;
}
