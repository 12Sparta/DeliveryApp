package com.example.delivery.domain.order.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.Status;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.domain.order.dto.response.OrderResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.delivery.common.Status.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    //주문 생성
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, long loginUserId) {
        User user = userRepository.findById(loginUserId)
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
        Order order = new Order(menu, store, user);

        orderRepository.save(order);

        return new OrderResponseDto(order.getStore().getId(), order.getUser().getId(), order.getId(), order.getStatus());
    }

    //주문 수락
    public OrderResponseDto acceptOrder(Long orderId, Long loginUserId) {
        //주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
        //사장
        User owner = order.getStore().getUser();
        //사용자
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        //주문의 사장 ID와 사용자 ID가 일치하는지 확인
        if (!owner.getId().equals(user.getId())) {
            throw new ApplicationException("본인 가게의 주문만 수락할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }

        //조리중으로 상태 변경
        order.setStatus(COOKING);

        return new OrderResponseDto(order.getStore().getId(), order.getUser().getId(), order.getId(), order.getStatus());
    }

    //주문 상태 변경
    public OrderResponseDto changeOrderState(Long orderId, Long loginUserId) {
        //주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));

        //주문 사장 ID와 사용자 ID가 일치하는지 확인
        if (!order.getStore().getUser().getId().equals(loginUserId)) {
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
        return new OrderResponseDto(order.getStore().getId(), order.getUser().getId(), order.getId(), order.getStatus());
    }

    //주문 취소/거절
    public void cancelOrder(Long orderId, Long loginUserId) {
        //주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));

        //사용자 DB에서 찾기
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        switch (user.getRole()){
            case OWNER :
                //사장인 경우
                //주문 가게의 사장과 사용자가 동일한지 확인
                if (!order.getStore().getUser().equals(user)) {
                    throw new ApplicationException("본인 가게의 주문만 거절할 수 있습니다.", HttpStatus.UNAUTHORIZED);
                }
                orderRepository.delete(order);
                break;

            case CUSTOMER :
                //손님인 경우
                //주문 손님과 사용자가 동일한지 확인
                if (!order.getUser().equals(user)) {
                    throw new ApplicationException("본인의 주문만 취소할 수 있습니다.", HttpStatus.UNAUTHORIZED);
                }
                orderRepository.delete(order);
        }
    }

    //장바구니에 상품 추가
    @Transactional
    public OrderResponseDto addCart(OrderCreateRequestDto requestDto, long loginUserId) {
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApplicationException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 메뉴입니다.", HttpStatus.NOT_FOUND));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new ApplicationException("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND));

        //장바구니 확인 후 없을 경우
        Cart cart = cartRepository.findByUserId(loginUserId)
                .orElse(cartRepository.save(new Cart(user, store)));


        //다른 가게의 상품을 추가하는 경우 장바구니 새로고침
        if(!cart.getStore().getId().equals(requestDto.getStoreId())){
            cartRepository.delete(cart);
            cart = cartRepository.save(new Cart(user, store));
        }

        LocalTime now = LocalTime.now();

        //가게 운영시간 확인
        if (now.isBefore(store.getOpenedAt())) {
            throw new ApplicationException("가게 운영 시간이 아닙니다.", HttpStatus.BAD_REQUEST);
        }
        if (now.isAfter(store.getClosedAt())) {
            throw new ApplicationException("가게 운영 시간이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        //주문 생성
        Order order = new Order(PENDING, menu, store, user, cart);

        orderRepository.save(order);

        return new OrderResponseDto(order.getStore().getId(), order.getUser().getId(), order.getId(), order.getStatus());
    }

    //장바구니의 상품들 구매
    @Transactional
    public void buyCart(Long cartId, long loginUserId){

        // 본인의 장바구니가 존재하는지 확인
        Cart cart = cartRepository.findByIdAndUserId(cartId, loginUserId)
                .orElseThrow(() -> new ApplicationException("본인의 장바구니가 아닙니다", HttpStatus.BAD_REQUEST));

        // 장바구니 불러오기
        List<Order> orderList = orderRepository.findByCartIdAndUserId(cartId, loginUserId);

        // 구매 후 가게측 확인 대기 상태로 변경
        orderList.forEach(order -> order.setStatus(CHECKING));

        // 구매한 장바구니 비우기
        cartRepository.delete(cart);
    }

    // 장바구니 비우기
    @Transactional
    public void deleteCart(Long cartId, long loginUserId){

        // 본인의 장바구니가 존재하는지 확인
        Cart cart = cartRepository.findByIdAndUserId(cartId, loginUserId)
                .orElseThrow(() -> new ApplicationException("본인의 장바구니가 아닙니다", HttpStatus.BAD_REQUEST));

        // 장바구니 삭제
        orderRepository.deleteByCartIdAndUserId(cartId, loginUserId, PENDING);
        cartRepository.delete(cart);
    }
}
