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
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.order.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private UserRepository userRepository;
    @Mock private MenuRepository menuRepository;
    @InjectMocks private OrderService orderService;

    @Test
    void 주문_생성_정상() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 5000L, store);
        OrderCreateRequestDto dto = new OrderCreateRequestDto(menu.getId(), store.getId());

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

        // when
        OrderResponseDto response = assertDoesNotThrow(() -> orderService.createOrder(dto, user.getId()));

        // then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertEquals(dto.getMenuId(), savedOrder.getMenu().getId());
        assertEquals(dto.getStoreId(), savedOrder.getStore().getId());
        assertEquals(user.getId(), savedOrder.getUser().getId());
    }

    @Test
    void 주문_최소금액_미만() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 500L, store);
        OrderCreateRequestDto dto = new OrderCreateRequestDto(menu.getId(), store.getId());

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));
        given(menuRepository.findById(menu.getId())).willReturn(Optional.of(menu));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> orderService.createOrder(dto, user.getId()));
        assertEquals("최소 주문 금액을 넘어야 합니다.", exception.getMessage());
    }

    @Test
    void 주문_수락_정상() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 5000L, store);
        Order order = new Order(menu, store, user);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        given(userRepository.findById(owner.getId())).willReturn(Optional.of(owner));

        // when
        OrderResponseDto response = orderService.acceptOrder(order.getId(), owner.getId());

        // then
        assertEquals(Status.COOKING, response.getStatus());
    }

    @Test
    void 주문_수락_자신의_가게_주문이_아닌_경우() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 5000L, store);
        Order order = new Order(menu, store, user);
        User wrongOwner = new User(3L, "wrongOwner", "wrong@test.com", "password", Role.OWNER, "wrongAddress");

        given(userRepository.findById(wrongOwner.getId())).willReturn(Optional.of(wrongOwner));
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> orderService.acceptOrder(order.getId(), wrongOwner.getId()));
        assertEquals("본인 가게의 주문만 수락할 수 있습니다.", exception.getMessage());
    }

    @Test
    void 주문_상태변경_정상() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 5000L, store);
        Order order = new Order(menu,store,user);

        order.setStatus(Status.COOKING);
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));
        // when
        OrderResponseDto response = orderService.changeOrderState(order.getId(), owner.getId());

        // then
        assertEquals(Status.DELIVERING, response.getStatus());
    }

    @Test
    void 주문_거절_정상() {
        // given
        User owner = new User(1L, "testOwner", "owner@test.com", "password", Role.OWNER, "testAddress");
        User user = new User(2L, "customer", "customer@test.com", "password", Role.CUSTOMER, "testAddress");
        Store store = new Store(1L, owner, "testStore", 1000, "This is a test store", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Menu menu = new Menu(1L, "TestMenu", 5000L, store);
        Order order = new Order(menu, store, user);

        given(userRepository.findById(owner.getId())).willReturn(Optional.of(owner));
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));


        // when
        assertDoesNotThrow(() -> orderService.cancelOrder(order.getId(), owner.getId()));

        // then
        verify(orderRepository, times(1)).delete(order);
    }
}
