package com.example.delivery.common.filter;

import com.example.delivery.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginFilter implements Filter {
  // 인증 없이 접근 가능한 API 경로
  private static final String[] WHITE_LIST = {
      "/",
      "/users/signup",
      "/users/login",
  };

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestURI = httpRequest.getRequestURI();

    // 화이트리스트에 있는 경우 필터 통과
    if (isWhiteList(requestURI)) {
      chain.doFilter(request, response);
      return;
    }

    // Authorization 헤더에서 JWT 가져오기
    String authorization = httpRequest.getHeader("Authorization");
    // JWT가 없거나 형식이 틀리면 404 응답
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    // 토큰에서 "Bearer" 부분 제거 후 JWT 추출
    String token = authorization.substring(7);
    if (!JwtUtil.validateToken(token)) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    // JWT 유효하면 필터 계속 진행
    chain.doFilter(request, response);
  }

  // 화이트 리스트에 포함된 경로인지 확인
  private boolean isWhiteList(String requestURI) {
    for (String pattern : WHITE_LIST) {
      if (requestURI.startsWith(pattern)) {
        return true;
      }
    }
    return false;
  }
}