package com.example.delivery.domain.menu.service;

import com.example.delivery.common.Role;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.dto.responseDto.MenuCreateResponseDto;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
  @Mock
  private MenuRepository menuRepository;
  @Mock
  private StoreRepository storeRepository;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private MenuService menuService;

  @Test
  void 메뉴를_정상적으로_등록() {
    //given
   User user = new User("user", "owner@email.com", "password", Role.OWNER, "서울");
   Store store = new Store(user, "store", 10000, " Store", null, null);
   Long userId = 1L;
   Long storeId = 1L;
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
   //when
    MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(userId, storeId, "치킨", 5000L);

    // then
    assertNotNull(menuCreateResponseDto);
    assertEquals("치킨",menuCreateResponseDto.getMenuName());
    assertEquals(5000L,menuCreateResponseDto.getPrice());
  }

  @Test
  void updateMenu() {
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