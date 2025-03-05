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

  // application.properties에서 시크릿 키를 불러옴
  private static String JWT_KEY;

  // 유저별 최신 JWT 저장
  private static final ConcurrentHashMap<Long, String> userTokens = new ConcurrentHashMap<>();

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

    String newToken = Jwts.builder()
        .setSubject(userId.toString()) // userId 저장
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiration))
        .signWith(getKey())
        .compact();

    // 최신 토큰 저장
    userTokens.put(userId, newToken);
    return newToken;
  }

  // JWT 검증 (최신 토큰인지 확인)
  public static boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);

      // 최신 토큰인지 확인
      Long userId = extractUserId(token);
      String latestToken = userTokens.get(userId);

      return latestToken != null && latestToken.equals(token);
    } catch (JwtException | IllegalArgumentException ex) {
      return false;
    }
  }

  // JWT 만료시간 검증 (true 반환 시 만료된 토큰)
  public static boolean validateExpired(String authorization) {
    try {
      String token = authorization.substring(7);
      Long expirationTime = Jwts.parserBuilder()
          .setSigningKey(getKey())
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .getTime();

      return expirationTime < System.currentTimeMillis();
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

  // 토큰 무효화
  public static void invalidateToken(Long userId) {
    userTokens.remove(userId); // 해당 유저의 토큰 제거
  }
}
