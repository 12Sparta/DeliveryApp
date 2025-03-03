package com.example.delivery.domain.menu.dto.responseDto;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;

@Getter
public class MenuOrderResponseDto {
  private Long id;
  private String menuName;
  private Long price;

  public MenuOrderResponseDto(Long id, String menuName, Long price){
    this.id = id;
    this.menuName = menuName;
    this.price = price;
  }
}
