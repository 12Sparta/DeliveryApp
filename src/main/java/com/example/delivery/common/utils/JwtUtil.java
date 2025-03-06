package com.example.delivery.common.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {


  private static String JWT_KEY;
  // application.properties에서 JWT 시크릿 키를 불러옴
  public JwtUtil(Environment env) {
    JWT_KEY = env.getProperty("jwt.secret");
  }

  // SecretKey 생성 메서드
  private static SecretKey getKey() {
    return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
  }

  // JWT 생성
  public static String generateToken(Long userId) {
    Instant now = Instant.now();
    Instant expiration = now.plus(1, ChronoUnit.HOURS);

    return Jwts.builder()
        .setSubject(userId.toString()) // userId 저장
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiration))
        .signWith(getKey())
        .compact();


  }

  // JWT 유효성 검증
  public static boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException ex) {
      return false;
    }
  }

  // JWT 만료 여부 확인
  public static boolean validateExpired(String authorization) {
    try {
      String token = authorization.substring(7);
      return Jwts.parserBuilder()
          .setSigningKey(getKey())
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return true;
    }
  }

  // JWT userId 추출
  public static Long extractUserId(String authorization) {
    try {
      String token = authorization.substring(7);

      return Long.parseLong(
          Jwts.parserBuilder()
              .setSigningKey(getKey())
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject()
      );
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("토큰이 만료되었습니다.");
    } catch (JwtException | IllegalArgumentException e) {
      throw new RuntimeException("유효하지 않은 토큰입니다.");
    }
  }


}

