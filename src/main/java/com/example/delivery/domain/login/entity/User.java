package com.example.delivery.domain.login.entity;

import com.example.delivery.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.delivery.common.Role;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 100)
    private String address;

    // 회원 탈퇴 시간 저장 (NULL이면 탈퇴하지 않은 상태)
    @Column
    private LocalDateTime deletedAt = null;

    public User(String email, String password, String name, Role role, String address) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.address = address;
    }



    public void deleteUser() {
        this.password = "";
        this.name = "";
        this.address = "";
        this.deletedAt = LocalDateTime.now();
    }
}
