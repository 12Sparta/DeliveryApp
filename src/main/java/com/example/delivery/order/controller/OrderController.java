package com.example.delivery.order.controller;

import com.example.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    //주문 생성

    //주문 수락

    //주문 상태 변경

    //주문 취소
}
