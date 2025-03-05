package com.example.delivery.domain.menu.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.dto.responseDto.MenuCreateResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuUpdateResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
  @Mock
  private MenuRepository menuRepository;
  @Mock
  private StoreRepository storeRepository;


  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private MenuService menuService;

  private User owner;
  private Store store;
  private Menu menu;

  @Test
  void 메뉴를_정상적으로_등록() {
    //given
    owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
    store = new Store(owner, "Test Store", 10000, "about", LocalTime.of(9, 0), LocalTime.of(22, 0));
    ReflectionTestUtils.setField(owner, "id", 1L);
    ReflectionTestUtils.setField(store, "id", 1L);
    Long userId = 1L;
    Long storeId = 1L;
    given(userRepository.findById(userId)).willReturn(Optional.of(owner));
    given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
    given(menuRepository.save(any(Menu.class))).willAnswer(invocation -> {
      Menu savedMenu = invocation.getArgument(0);
      ReflectionTestUtils.setField(savedMenu, "id", 1L);
      return savedMenu;
    });

   //when
    MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(userId, storeId, "치킨", 5000L);

    // then
    assertNotNull(menuCreateResponseDto);
    assertEquals("치킨",menuCreateResponseDto.getMenuName());
    assertEquals(5000L,menuCreateResponseDto.getPrice());
  }


  @Test
  void 사장이_아닌_사용자가_메뉴_등록_시_에러발생(){
    // given
    User customer = new User("customer", "customer@email.com", "password", Role.CUSTOMER, "서울");
    Long userId = 1L;
    Long storeId = 1L;

    given(userRepository.findById(userId)).willReturn(Optional.of(customer));

    // when
    ApplicationException exception = assertThrows(ApplicationException.class, () ->
        menuService.createMenu(userId, storeId, "피자", 12000L)
    );
    assertEquals("메뉴 생성은 사장님만 할 수 있습니다.", exception.getMessage());
  }

  @Test
  void updateMenu() {
    // given

    User owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
    Store store = new Store(owner, "Test Store", 10000, "Test Description", LocalTime.of(9, 0), LocalTime.of(22, 0));
    Menu menu = new Menu("Test Menu", 1000L, store);
    ReflectionTestUtils.setField(owner, "id", 1L);
    Long userId = 1L;
    Long menuId = 1L;
    given(userRepository.findById(userId)).willReturn(Optional.of(owner));
    given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

    //when
    MenuUpdateResponseDto response = menuService.updateMenu(userId, menuId, "Updated Menu", 2000L);

    //then
    assertNotNull(response);
    assertEquals("Updated Menu", response.getMenuName());
  }

  @Test
  void getMenusByStore() {
  }

  @Test
  void getMenusByOrder() {
  }

  @Test
  void deleteMenu() {
  }
}