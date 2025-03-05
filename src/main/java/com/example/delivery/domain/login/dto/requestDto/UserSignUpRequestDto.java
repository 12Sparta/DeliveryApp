package com.example.delivery.domain.login.dto.requestDto;

import com.example.delivery.common.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {

  @NotBlank(message = "이메일은 필수입니다.")
  @Email
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$",
      message = "비밀번호는 8자리 이상만 가능하며, 최소 하나의 대문자, 숫자, 특수문자(!,@,#,$,%,^,&,*)를 포함해야 합니다."
  )
  private String password;

  @NotBlank(message = "이름은 필수입니다.")
  private String name;

  @NotBlank(message = "주소는 필수입니다.")
  private String address;

  @NotNull(message = "역할은 필수입니다")
  private Role role;


}