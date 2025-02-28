package com.example.delivery.domain.menu.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuCreateResponseDto {
  private Long id;
  private String menuName;
  private Long price;
  private LocalDateTime createdAt;

  public MenuCreateResponseDto(Long id, String menuName, Long price, LocalDateTime createdAt) {
    this.id = id;
    this.menuName = menuName;
    this.price = price;
    this.createdAt = createdAt;
  }
}