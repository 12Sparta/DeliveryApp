package com.example.delivery.domain.order.controller;

import com.example.delivery.config.aop.annotation.Order;
import com.example.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.example.delivery.domain.order.dto.request.OrderCancelRequestDto;
import com.example.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.request.OrderStateChangeRequestDto;
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

    //주문 생성
    @Order
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderCreateRequestDto requestDto
    ) {
        return new ResponseEntity<>(orderService.createOrder(requestDto), HttpStatus.CREATED);
    }

    //주문 수락
    @Order
    @PutMapping("/{id}/accept")
    public ResponseEntity<OrderResponseDto> acceptOrder(
            @PathVariable Long id,
            @RequestBody OrderAcceptRequestDto requestDto
    ) {
        return new ResponseEntity<>(orderService.acceptOrder(id, requestDto), HttpStatus.OK);
    }

    //주문 상태 변경
    @Order
    @PutMapping("/{id}/change-state")
    public ResponseEntity<OrderResponseDto> changeOrderState(
            @PathVariable Long id,
            @RequestBody OrderStateChangeRequestDto requestDto
    ) {
        return new ResponseEntity<>(orderService.changeOrderState(id, requestDto), HttpStatus.OK);
    }

    //주문 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @PathVariable Long id,
            @RequestBody OrderCancelRequestDto requestDto
    ) {
        orderService.cancelOrder(id, requestDto);
        Map<String, String> message = new HashMap<>();
        message.put("message", "주문 삭제 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
