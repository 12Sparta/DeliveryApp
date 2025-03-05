package com.example.delivery.domain.order.service;

import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.OrderAcceptRequestDto;
import com.example.delivery.domain.order.dto.OrderCancelRequestDto;
import com.example.delivery.domain.order.dto.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.OrderStateChangeRequestDto;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import static com.example.delivery.common.Status.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    //주문 생성
    public void createOrder(OrderCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 메뉴입니다.", HttpStatus.NOT_FOUND));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND));

        LocalTime now = LocalTime.now();

        //메뉴 상태 확인(최소 배달 금액은 넘는지)
        if (menu.getPrice() < store.getOrderMin()) {
            throw new ApplicationException("최소 주문 금액을 넘어야 합니다.", HttpStatus.BAD_REQUEST);
        }

        //가게 운영시간 확인
        if (now.isBefore(store.getOpenedAt())) {
            throw new ApplicationException("가게 운영 시간이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        if (now.isAfter(store.getClosedAt())) {
            throw new ApplicationException("가게 운영 시간이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        //주문 생성
        Order order = new Order(CHECKING, menu, store, user);

        orderRepository.save(order);
    }

    //주문 수락
    public void acceptOrder(Long orderId, OrderAcceptRequestDto requestDto) {
        //주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
        //사장
        User owner = order.getStore().getUser();
        //사용자
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        //주문의 사장 ID와 사용자 ID가 일치하는지 확인
        if (!owner.getId().equals(user.getId())) {
            throw new ApplicationException("본인 가게의 주문만 수락할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }

        //조리중으로 상태 변경
        order.setStatus(COOKING);
    }

    //주문 상태 변경
    public void changeOrderState(Long orderId, OrderStateChangeRequestDto requestDto) {
        //주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));

        //주문 사장 ID와 사용자 ID가 일치하는지 확인
        if (!order.getStore().getUser().getId().equals(requestDto.getUserID())) {
            throw new ApplicationException("본인 가게의 주문만 관리할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }

        //상태 변경(순차적으로 변경 - case문 사용)
        switch (order.getStatus()) {
            case COOKING:
                order.setStatus(DELIVERING);
                break;
            case DELIVERING:
                order.setStatus(DELIVERY_COMPLETED);
                break;
        }

    }

    //주문 취소/거절
    public void cancelOrder(Long id, OrderCancelRequestDto requestDto) {
        //주문 찾기
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));

        //사용자 DB에서 찾기
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()-> new ApplicationException("존재하지 않는 사용자입니다.",HttpStatus.NOT_FOUND));

        //사장인 경우
        //주문 가게의 사장과 사용자가 동일한지 확인
        if (!order.getStore().getUser().equals(user)) {
            throw new ApplicationException("본인 가게의 주문만 거절할 수 있습니다.",HttpStatus.UNAUTHORIZED);
        }
        orderRepository.delete(order);

        //손님인 경우
        //주문 손님과 사용자가 동일한지 확인

    }
}
