package com.example.delivery.common.filter;

import com.example.delivery.common.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoginFilter implements Filter {

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

    // JWT 인증 처리
    String authorization = httpRequest.getHeader("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String token = authorization.substring(7);
    if (!JwtUtil.validateToken(token)) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    chain.doFilter(request, response);
  }


  private boolean isWhiteList(String requestURI) {
    for (String pattern : WHITE_LIST) {
      if (requestURI.startsWith(pattern)) {
        return true;
      }
    }
    return false;
  }
}