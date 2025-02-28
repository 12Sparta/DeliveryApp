package com.example.delivery.domain.menu.dto.responseDto;

import lombok.Getter;

@Getter
public class MenuFindResponseDto {
  private Long id;
  private String menuName;
  private Long price;

  public  MenuFindResponseDto(Long id, String menuName, Long price){
    this.id = id;
    this.menuName = menuName;
    this.price = price;
  }
}
