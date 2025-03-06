package com.example.delivery.domain.order.controller;

import com.example.delivery.common.utils.JwtUtil;
import com.example.delivery.config.aop.annotation.Order;
import com.example.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.response.OrderResponseDto;
import com.example.delivery.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    //주문 수락
    @Order
    @PutMapping("/{id}/accept")
    public ResponseEntity<OrderResponseDto> acceptOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        Long loginId = JwtUtil.extractUserId(token);
        return new ResponseEntity<>(orderService.acceptOrder(id, loginId), HttpStatus.OK);
    }

    //주문 상태 변경
    @Order
    @PutMapping("/{id}/change-state")
    public ResponseEntity<OrderResponseDto> changeOrderState(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        Long loginId = JwtUtil.extractUserId(token);
        return new ResponseEntity<>(orderService.changeOrderState(id, loginId), HttpStatus.OK);
    }

    //주문 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        Long loginId = JwtUtil.extractUserId(token);
        orderService.cancelOrder(id, loginId);
        Map<String, String> message = new HashMap<>();
        message.put("message", "주문 삭제 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //장바구니에 상품 추가
    @Order
    @PostMapping
    public ResponseEntity<OrderResponseDto> createCartOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderCreateRequestDto requestDto
    ) {
        Long loginId = JwtUtil.extractUserId(token);
        return new ResponseEntity<>(orderService.addCart(requestDto, loginId), HttpStatus.CREATED);
    }

    //장바구니의 상품들 구매
    @PostMapping("/buy/{cartId}")
    public ResponseEntity<Void> buyCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long cartId) {

        Long loginId = JwtUtil.extractUserId(token);
        orderService.buyCart(cartId, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 장바구니 비우기
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long cartId) {

        Long loginId = JwtUtil.extractUserId(token);
        orderService.deleteCart(cartId, loginId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
