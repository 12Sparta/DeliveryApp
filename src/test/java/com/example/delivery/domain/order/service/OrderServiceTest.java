package com.example.delivery.domain.order.service;

import com.example.delivery.common.Role;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.request.OrderCreateRequestDto;
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

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_생성_정상() {
        //given
        Long storeId = 1L;
        Long menuId = 1L;
        OrderCreateRequestDto dto = new OrderCreateRequestDto(menuId, storeId);
        User owner = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        User user = new User(2L, "test@tes.com", "test1234@@", "손님", Role.CUSTOMER, "test");
        Store store = new Store(owner, "teststore1", 1000, "this is teststore", LocalTime.of(8,0), LocalTime.of(23,59));
        Menu menu = new Menu("Test1", 5000L, store);

        given(userRepository.findByIdAndRoleIsOwner(2L, user.getRole())).willReturn(Optional.of(user)); // 유저 존재 확인
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store)); //가게 존재 확인
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu)); //메뉴 존재 확인

        //when
        assertDoesNotThrow(() -> orderService.createOrder(dto, owner.getId())); //예외 발생 시 실패

        //then
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class); //추적 객체 생성
        verify(orderRepository, times(1)).save(orderCaptor.capture()); //실행 횟수 확인

        Order capturedOrder = orderCaptor.getValue();
        assertEquals(dto.getMenuId(), capturedOrder.getMenu().getId()); //예상 확인
        assertEquals(dto.getStoreId(), capturedOrder.getStore().getId()); //예상 확인
    }

    @Test
    void 주문_최소금액_미만() {
        //given
        Long storeId = 1L;
        Long menuId = 1L;
        OrderCreateRequestDto dto = new OrderCreateRequestDto(menuId, storeId);
        User owner = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        User user = new User(2L, "test@tes.com", "test1234@@", "손님", Role.CUSTOMER, "test");
        Store store = new Store(owner, "teststore1", 1000, "this is teststore", LocalTime.of(8,0), LocalTime.of(23,59));
        Menu menu = new Menu("Test1", 5000L, store);

        given(userRepository.findByIdAndRoleIsOwner(2L, user.getRole())).willReturn(Optional.of(user)); // 유저 존재 확인
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store)); //가게 존재 확인
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu)); //메뉴 존재 확인

        //when

        //then
    }

    @Test
    void 주문_수락_정상() {
        //given

        //when

        //then
    }

    @Test
    void 주문_수락_자신의_가게_주문이_아닌_경우() {
        //given

        //when

        //then
    }

    @Test
    void 주문_상태변경_정상() {
        //given

        //when

        //then
    }

    @Test
    void 주문_상태변경_자신의_가게_주문이_아닌_경우() {
        //given

        //when

        //then
    }

    @Test
    void 주문_거절_정상() {
        //given

        //when

        //then
    }

    @Test
    void 주문_거절_자신의_가게_주문이_아닌_경우() {
        //given

        //when

        //then
    }


}
