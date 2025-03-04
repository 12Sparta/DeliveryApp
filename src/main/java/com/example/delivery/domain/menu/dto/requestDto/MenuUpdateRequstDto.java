package com.example.delivery.domain.menu.dto.requestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuUpdateRequstDto {
  @NotBlank(message = "메뉴 이름은 필수")
  private String menuName;

  @NotNull(message = "가격은 필수")
  @Min(value = 0, message = "가격은 0원 이상")
  private Long price;
}
