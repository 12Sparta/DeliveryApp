package com.example.delivery.domain.login.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.config.PasswordEncoder;
import com.example.delivery.domain.login.dto.responseDto.UserSignupResponseDto;
import com.example.delivery.domain.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.delivery.domain.login.entity.User;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  //회원가입
  @Transactional
  public UserSignupResponseDto signUp(String email, String password, String username, Role role, String address) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new ApplicationException("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
    }

    User user = new User(email, passwordEncoder.encode(password), username, role, address);
    userRepository.save(user);
    return new UserSignupResponseDto(user);
  }


  //로그인
  public Long login(String email, String password) {
    User findUser = userRepository.findByEmail(email)
        .orElseThrow(() -> new ApplicationException("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND));

    if (findUser.getDeletedAt() != null) {
      throw new ApplicationException("회원 탈퇴한 유저입니다.", HttpStatus.NOT_FOUND);
    }

    if (!passwordEncoder.matches(password, findUser.getPassword())) {
      throw new ApplicationException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }

    return findUser.getId();
  }

  // 유저 탈퇴
  @Transactional
  public void deleteUser(Long userId, String password) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

    // 비밀번호 검증
    if (findUser.getDeletedAt() != null) {
      throw new ApplicationException("이미 탈퇴한 사용자입니다.", HttpStatus.BAD_REQUEST);
    }

    if (!passwordEncoder.matches(password, findUser.getPassword())) {
      throw new ApplicationException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
    findUser.deleteUser();
  }
}