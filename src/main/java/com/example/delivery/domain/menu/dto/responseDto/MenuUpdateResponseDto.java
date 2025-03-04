package com.example.delivery.domain.menu.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuUpdateResponseDto {
  private Long id;
  private String menuName;
  private Long price;
  private LocalDateTime updatedAt;

  public MenuUpdateResponseDto(Long id, String menuName, Long price, LocalDateTime updatedAt){
    this.id = id;
    this.menuName = menuName;
    this.price = price;
    this.updatedAt = updatedAt;
  }
}
