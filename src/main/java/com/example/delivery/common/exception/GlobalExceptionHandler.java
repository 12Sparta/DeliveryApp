package com.example.delivery.common.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
  // ApplicationException 처리
  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<Map<String, Object>> handleApplicationException(ApplicationException ex) {
    return getErrorResponse(ex.getStatus(), ex.getMessage());
  }

  // 유효성 검사 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String firstErrorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElseThrow(() -> new IllegalStateException("검증 에러가 반드시 존재해야 합니다."));

    return getErrorResponse(HttpStatus.BAD_REQUEST, firstErrorMessage);
  }

  private ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
    Map<String, Object> errorResponse = new LinkedHashMap<>();
    errorResponse.put("status", status.name()); // 예 : NOT_FOUND
    errorResponse.put("code", status.value());  // 예 : 404
    errorResponse.put("message", message); // 예외 메세지
    errorResponse.put("timestamp", LocalDateTime.now());

    return new ResponseEntity<>(errorResponse, status);
  }

}
