package com.example.delivery.domain.login.dto.requestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserDeleteRequestDto {
  @NotBlank
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
      message = "비밀번호는 8자리 이상만 가능하며, 최소 하나의 대문자, 숫자, 특수문자(!,@,#,$,%,^,&,*)를 포함해야 합니다."
  )
  private String password;
}

