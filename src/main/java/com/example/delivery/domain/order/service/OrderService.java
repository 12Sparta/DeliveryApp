package com.example.delivery.domain.order.service;

import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.OrderAcceptRequestDto;
import com.example.delivery.domain.order.dto.OrderCancelRequestDto;
import com.example.delivery.domain.order.dto.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.OrderStateChangeRequestDto;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    //유저 레포지토리 추가

    //주문 생성
    public void createOrder(OrderCreateRequestDto requestDto) {
        //사용자 상태(사장 / 손님) 확인

        //메뉴 상태 확인(1개인지 && 최소 배달 금액은 넘는지)

        //주문 생성  생성자 맞게 넣기
        Order order = new Order();

        orderRepository.save(order);
    }

    //주문 수락
    public void acceptOrder(Long orderId, OrderAcceptRequestDto requestDto){
        //주문 찾기
        Order order = orderRepository.findById(orderId).orElseThrow();

        //주문의 사장 ID와 사용자 ID가 일치하는지 확인 && 사용자 상태가 사장인지 확인
        if(order.getStore().getId() == )

        //주문 수락으로 상태 변경
    }

    //주문 상태 변경
    public void changeOrderState(Long orderId, OrderStateChangeRequestDto requestDto){
        //주문 찾기
        Order order = orderRepository.findById(orderId).orElseThrow();

        //주문 사장 ID와 사용자 ID가 일치하는지 확인

        //상태 변경(순차적으로 변경)

    }

    //주문 취소/거절
    public void cancelOrder(Long id, OrderCancelRequestDto requestDto){
        //주문 찾기
        Order order = orderRepository.findById(id).orElseThrow();

        //사장인 경우
        //주문서 가게 ID와 사용자 ID가 동일한지 확인

        //주문 거절
        orderRepository.delete(order);

        //손님인 경우
        //주문 user ID와 사용자 ID가 동일한지 확인

        //주문 상태 확인

        //주문 거절
    }
}
