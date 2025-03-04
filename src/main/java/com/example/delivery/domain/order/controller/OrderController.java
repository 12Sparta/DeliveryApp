package com.example.delivery.domain.order.controller;

import com.example.delivery.domain.order.dto.OrderAcceptRequestDto;
import com.example.delivery.domain.order.dto.OrderCancelRequestDto;
import com.example.delivery.domain.order.dto.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.OrderStateChangeRequestDto;
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
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(
            @RequestBody OrderCreateRequestDto requestDto
            ) {
        orderService.createOrder(requestDto);
        Map<String, String> message = new HashMap<>();
        message.put("message", "주문 생성 완료");
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    //주문 수락
    @PutMapping("{/id}")
    public ResponseEntity<Map<String, String>> acceptOrder(
            @PathVariable Long id,
            @RequestBody OrderAcceptRequestDto requestDto
    ) {
        orderService.acceptOrder(id, requestDto);
        Map<String, String> message = new HashMap<>();
        message.put("message", "주문 수락 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //주문 상태 변경
    @PutMapping("{/id}")
    public ResponseEntity<Map<String, String>> changeOrderState(
            @PathVariable Long id,
            @RequestBody OrderStateChangeRequestDto requestDto
    ) {
        orderService.changeOrderState(id, requestDto);
        Map<String, String> message = new HashMap<>();
        message.put("message", "주문 상태 변경 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //주문 취소
    @DeleteMapping("{/id}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @PathVariable Long id,
            @RequestBody OrderCancelRequestDto requestDto
    ) {
        orderService.cancelOrder(id,requestDto);
        Map<String, String>message = new HashMap<>();
        message.put("message","주문 삭제 완료");
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
