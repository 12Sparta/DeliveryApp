package com.example.delivery.domain.login.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private final Long id;
    private final String email;
    private final String password;

    public UserLoginResponseDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
