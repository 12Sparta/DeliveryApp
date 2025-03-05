package com.example.delivery.domain.login.dto.responseDto;

import com.example.delivery.common.Role;
import com.example.delivery.domain.login.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSignupResponseDto {
  private final Long id;
  private final String email;
  private final String name;
  private final String address;
  private final Role role;
  private final LocalDateTime createdAt;

  public UserSignupResponseDto(User user){
    this.id = user.getId();
    this.email = user.getEmail();
    this.name = user.getName();
    this.address = user.getAddress();
    this.role = user.getRole();
    this.createdAt = user.getCreatedAt();
  }


}
