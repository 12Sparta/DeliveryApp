package com.example.delivery.domain.login.controller;

import com.example.delivery.common.utils.JwtUtil;
import com.example.delivery.domain.login.dto.requestDto.UserDeleteRequestDto;
import com.example.delivery.domain.login.dto.requestDto.UserLoginRequestDto;
import com.example.delivery.domain.login.dto.requestDto.UserSignUpRequestDto;
import com.example.delivery.domain.login.dto.responseDto.UserLoginResponseDto;
import com.example.delivery.domain.login.dto.responseDto.UserSignupResponseDto;
import com.example.delivery.domain.login.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  // 유저 회원 가입
  @PostMapping("/signup")
  public ResponseEntity<UserSignupResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
    UserSignupResponseDto response = userService.signUp(
        requestDto.getEmail(),
        requestDto.getPassword(),
        requestDto.getName(),
        requestDto.getRole(),
        requestDto.getAddress()
    );
    return ResponseEntity.ok(response);
  }

  // 유저 로그인
  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDto> login(
      @Valid @RequestBody UserLoginRequestDto requestDto) {

    // 유저 로그인 후 userId 반환
    Long userId = userService.login(requestDto.getEmail(), requestDto.getPassword());

    // 기존 토큰 제거
    JwtUtil.invalidateToken(userId);

    // 새로운 토큰 생성
    String newToken = JwtUtil.generateToken(userId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);

    // 유저에게 토큰 반환
    return ResponseEntity.ok()
        .headers(httpHeaders)  // 헤더 포함
        .body(new UserLoginResponseDto(newToken)); // 바디에 토큰 포함
  }
  //회원 탈퇴
  @DeleteMapping("/delete")
  public ResponseEntity<Void> delteUser(
      @RequestHeader(name = "Authorization") String authorization,
      @RequestBody UserDeleteRequestDto requestDto
  ){
    Long userId = JwtUtil.extractUserId(authorization);
    // 소프트 딜리트
    userService.deleteUser(userId, requestDto.getPassword());
    return new ResponseEntity<>(HttpStatus.OK);

  }


  }
