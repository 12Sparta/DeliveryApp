package com.example.delivery.domain.login.dto.responseDto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
  private String token;


  public UserLoginResponseDto(String token){

    this.token = token;
  }
}
