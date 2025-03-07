package com.example.delivery.domain.menu.repository;

import com.example.delivery.common.Role;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuRepositoryTest {

  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void 가게_ID로_삭제되지_않은_메뉴만_조회가능() {
    // Given
    User user = new User("park@email.com", "Password123!", "박성호", Role.OWNER, "대구");
    userRepository.save(user);

    Store store = new Store(user, "Store1", 10000, "Store1", LocalTime.of(9, 0), LocalTime.of(22, 0));
    storeRepository.save(store);
    Menu menu1 = new Menu("Burger", 5000L, store);
    Menu menu2 = new Menu("Pizza", 12000L, store);
    Menu menu3 = new Menu("Pasta", 9000L, store);
    menu3.delete();

    menuRepository.save(menu1);
    menuRepository.save(menu2);
    menuRepository.save(menu3);

    // When
    List<Menu> menus = menuRepository.findMenusByStoreId(store.getId());

    // Then
    assertNotNull(menus);
    assertThat(menus).hasSize(2);
    assertThat(menus).contains(menu1);
    assertThat(menus).contains(menu2);
    assertThat(menus).doesNotContain(menu3);
  }

  @Test
  void 메뉴를_가게ID로_조회할_수_있다() {
    // Given
      User user = new User("park@email.com", "Password123!", "박성호", Role.OWNER, "대구");
    userRepository.save(user);

    Store store = new Store(user, "Store2", 10000, " Store2", LocalTime.of(9, 0), LocalTime.of(22, 0));
    storeRepository.save(store);
    Menu menu1 = new Menu("Burger", 5000L, store);
    Menu menu2 = new Menu("Pizza", 12000L, store);

    menuRepository.save(menu1);
    menuRepository.save(menu2);
    // When
    List<MenuFindResponseDto> menus = menuRepository.findByStoredId(store.getId());

    // Then
    assertNotNull(menus);
    assertThat(menus).hasSize(2);
    assertThat(menus).extracting("menuName").contains("Burger", "Pizza");
  }
}
