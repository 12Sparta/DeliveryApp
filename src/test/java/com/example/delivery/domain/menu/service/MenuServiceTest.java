//package com.example.delivery.domain.menu.service;
//
//import com.example.delivery.common.Role;
//import com.example.delivery.common.Status;
//import com.example.delivery.common.exception.ApplicationException;
//import com.example.delivery.domain.login.entity.User;
//import com.example.delivery.domain.login.repository.UserRepository;
//import com.example.delivery.domain.menu.dto.responseDto.MenuCreateResponseDto;
//import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
//import com.example.delivery.domain.menu.dto.responseDto.MenuOrderResponseDto;
//import com.example.delivery.domain.menu.dto.responseDto.MenuUpdateResponseDto;
//import com.example.delivery.domain.menu.entity.Menu;
//import com.example.delivery.domain.order.entity.Order;
//import com.example.delivery.domain.menu.repository.MenuRepository;
//import com.example.delivery.domain.order.repository.OrderRepository;
//import com.example.delivery.domain.store.entity.Store;
//import com.example.delivery.domain.store.repository.StoreRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//
//import java.lang.reflect.Field;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//  @Mock
//  private MenuRepository menuRepository;
//  @Mock
//  private StoreRepository storeRepository;
//
//  @Mock
//  private OrderRepository orderRepository;
//
//  @Mock
//  private UserRepository userRepository;
//
//  @InjectMocks
//  private MenuService menuService;
//
//  private User owner;
//  private Store store;
//  private Menu menu;
//
//
//  @Test
//  void 메뉴를_정상적으로_등록() {
//    //given
//    owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
//    store = new Store(owner, "Test Store", 10000, "about", LocalTime.of(9, 0), LocalTime.of(22, 0));
//    ReflectionTestUtils.setField(owner, "id", 1L);
//    ReflectionTestUtils.setField(store, "id", 1L);
//    given(userRepository.findById(1L)).willReturn(Optional.of(owner));
//    given(storeRepository.findById(1L)).willReturn(Optional.of(store));
//    given(menuRepository.save(any(Menu.class))).willAnswer(invocation -> {
//      Menu savedMenu = invocation.getArgument(0);
//      ReflectionTestUtils.setField(savedMenu, "id", 1L);
//      return savedMenu;
//    });
//
//   //when
//    MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(1L, 1L, "치킨", 5000L);
//
//    // then
//    assertNotNull(menuCreateResponseDto);
//    assertEquals("치킨",menuCreateResponseDto.getMenuName());
//    assertEquals(5000L,menuCreateResponseDto.getPrice());
//  }
//
//
//  @Test
//  void 사장이_아닌_사용자가_메뉴_등록_시_에러발생(){
//    // given
//    User customer = new User("customer", "customer@email.com", "password", Role.CUSTOMER, "서울");
//    Long userId = 1L;
//    Long storeId = 1L;
//
//    given(userRepository.findById(userId)).willReturn(Optional.of(customer));
//
//    // when
//    ApplicationException exception = assertThrows(ApplicationException.class, () ->
//        menuService.createMenu(userId, storeId, "피자", 12000L)
//    );
//    assertEquals("메뉴 생성은 사장님만 할 수 있습니다.", exception.getMessage());
//  }
//
//  @Test
//  void 메뉴를_정상적으로_수정가능() {
//    // given
//    User user = new User("owner@email.com", "Password123!", "박성호", Role.OWNER, "서울");
//    Store store = new Store(user, "Test Store", 10000, "Test Description", LocalTime.of(9, 0), LocalTime.of(22, 0));
//    Menu menu = new Menu("Menu1", 1000L, store);
//    ReflectionTestUtils.setField(user, "id", 1L);
//    ReflectionTestUtils.setField(store, "id", 1L);
//    ReflectionTestUtils.setField(menu, "id", 1L);
//    Long userId = 1L;
//    Long menuId = 1L;
//    given(userRepository.findById(userId)).willReturn(Optional.of(user));
//    given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));
//
//    //when
//    MenuUpdateResponseDto response = menuService.updateMenu(userId, menuId, "Menu2", 2000L);
//
//    //then
//    assertNotNull(response);
//    assertEquals("Menu2", response.getMenuName());
//    assertEquals(2000L, response.getPrice());
//  }
//
//  @Test
//  void 가게별_메뉴_목록을_조회() {
//    // given
//    User owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
//    Store store1 = new Store(owner, "Store1", 10000, "Test1", LocalTime.of(9, 0), LocalTime.of(22, 0));
//    Store store2 = new Store(owner, "Store2",15000,"Test2",LocalTime.of(9, 0), LocalTime.of(22, 0));
//    Menu menu1 = new Menu("Menu1", 1000L, store1);
//    Menu menu2 = new Menu("Menu2", 1000L, store2);
//    ReflectionTestUtils.setField(owner, "id", 1L);
//    ReflectionTestUtils.setField(store1, "id", 1L);
//    ReflectionTestUtils.setField(store2, "id", 2L);
//    ReflectionTestUtils.setField(menu1, "id", 1L);
//    ReflectionTestUtils.setField(menu2, "id", 2L);
//    given(storeRepository.findById(1L)).willReturn(Optional.of(store1));
//    given(storeRepository.findById(2L)).willReturn(Optional.of(store2));
//    given(menuRepository.findMenusByStoreId(1L)).willReturn(List.of(menu1));
//    given(menuRepository.findMenusByStoreId(2L)).willReturn(List.of(menu2));
//
//    // when
//    List<MenuFindResponseDto> menusStore1 = menuService.getMenusByStore(1L);
//    List<MenuFindResponseDto> menusStore2 = menuService.getMenusByStore(2L);
//
//    // then
//    assertEquals(1, menusStore1.size());
//    assertEquals("Menu1", menusStore1.get(0).getMenuName());
//
//    assertEquals(1, menusStore2.size());
//    assertEquals("Menu2", menusStore2.get(0).getMenuName());
//  }
//
//
//  @Test
//  void 주문별_메뉴_목록을_조회() {
//    // given
//    User owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
//    Store store = new Store(owner, "Store", 10000, "Test Description", LocalTime.of(9, 0), LocalTime.of(22, 0));
//    Menu menu1 = new Menu("치킨", 1000L, store);
//    Menu menu2 = new Menu("육회", 2000l,store);
//    Order order1 = new Order(Status.DELIVERY_COMPLETED,menu1,store,owner);
//    Order order2 = new Order(Status.DELIVERY_COMPLETED,menu2,store,owner);
//    ReflectionTestUtils.setField(owner, "id", 1L);
//    ReflectionTestUtils.setField(store, "id", 1L);
//    ReflectionTestUtils.setField(menu1, "id", 1L);
//    ReflectionTestUtils.setField(menu2, "id", 2L);
//    ReflectionTestUtils.setField(order1, "id", 1L);
//    ReflectionTestUtils.setField(order2, "id", 2L);
//    given(orderRepository.findById(1L)).willReturn(Optional.of(order1));
//    given(orderRepository.findById(2L)).willReturn(Optional.of(order2));
//    given(orderRepository.findMenuByOrderId(1L)).willReturn(Optional.of(menu1));
//    given(orderRepository.findMenuByOrderId(2L)).willReturn(Optional.of(menu2));
//
//    //when
//    MenuOrderResponseDto menuResponse1 = menuService.getMenusByOrder(1L);
//    MenuOrderResponseDto menuResponse2 = menuService.getMenusByOrder(2L);
//
//    // then
//    assertEquals("치킨", menuResponse1.getMenuName());
//    assertEquals("육회", menuResponse2.getMenuName());
//
//  }
//
//  @Test
//  void 메뉴를_삭제한다() {
//    owner = new User("owner", "owner@email.com", "password", Role.OWNER, "서울");
//    store = new Store(owner, "Store", 10000, "Test1", LocalTime.of(9, 0), LocalTime.of(22, 0));
//    menu = new Menu("치킨", 1000L, store);
//    ReflectionTestUtils.setField(owner, "id", 1L);
//    ReflectionTestUtils.setField(store, "id", 1L);
//    ReflectionTestUtils.setField(menu, "id", 1L);
//    given(userRepository.findById(1L)).willReturn(Optional.of(owner));
//    given(menuRepository.findById(1L)).willReturn(Optional.of(menu));
//
//    // when
//    menuService.deleteMenu(1L,1L);
//
//    //then
//    assertNotNull(menu.getDeletedAt());
//
//
//  }
//}