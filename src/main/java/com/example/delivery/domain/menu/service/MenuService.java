package com.example.delivery.domain.menu.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.dto.responseDto.MenuCreateResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuOrderResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuUpdateResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  //메뉴 생성
  public MenuCreateResponseDto createMenu(Long userId, Long storeId, String menuName, Long price) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

    // 사장님인지 확인
    if (findUser.getRole() != Role.OWNER) {
      throw new ApplicationException("메뉴 생성은 사장님만 할 수 있습니다.", HttpStatus.FORBIDDEN);
    }

    // 가게 존재 여부 확인
    Store findStore = storeRepository.findById(storeId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND));

    // 본인 가게인지 확인
    if (!findStore.getUser().getId().equals(userId)) {
      throw new ApplicationException("본인 가게 메뉴만 등록할수 있습니다.", HttpStatus.FORBIDDEN);
    }

    Menu menu = new Menu(menuName, price, findStore);
    Menu savedMenu = menuRepository.save(menu);

    return new MenuCreateResponseDto(savedMenu.getId(), savedMenu.getMenuName(), savedMenu.getPrice(), savedMenu.getCreatedAt());
  }

  //메뉴 수정
  @Transactional
  public MenuUpdateResponseDto updateMenu(Long userId, Long menuId, String menuName, Long price) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

    // 사장님인지 확인
    if (findUser.getRole() != Role.OWNER) {
      throw new ApplicationException("메뉴 수정은 사장님만 할 수 있습니다.", HttpStatus.FORBIDDEN);
    }

    // 수정할 메뉴가 존재하는지 확인
    Menu findMenu = menuRepository.findById(menuId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 메뉴입니다.", HttpStatus.NOT_FOUND));

    // 수정할 메뉴가 본인 가게 메뉴인지 확인
    if (!findMenu.getStore().getUser().getId().equals(userId)) {
      throw new ApplicationException("본인 가게 메뉴만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
    }

    findMenu.update(menuName, price);

    return new MenuUpdateResponseDto(
        findMenu.getId(),
        findMenu.getMenuName(),
        findMenu.getPrice(),
        findMenu.getUpdatedAt()
    );
  }

  // 가게별 메뉴 목록 조회
  public List<MenuFindResponseDto> getMenusByStore(Long storeId) {
    // 조회하려는 가게가 있는지 확인
    storeRepository.findById(storeId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 가게입니다.", HttpStatus.NOT_FOUND));

    List<Menu> menus = menuRepository.findMenusByStoreId(storeId);
    List<MenuFindResponseDto> responseDtos = new ArrayList<>();

    for (Menu menu : menus) {
      responseDtos.add(new MenuFindResponseDto(menu.getId(), menu.getMenuName(), menu.getPrice()));

    }
    return responseDtos;
  }

  // 주문 내역 메뉴 목록 조회
  public MenuOrderResponseDto getMenusByOrder(Long orderId) {
    // 조회하려는 주문이 있는지 확인
    orderRepository.findById(orderId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));

    // 주문에 해당하는 메뉴 찾기
    Menu findMenu = orderRepository.findMenuByOrderId(orderId)
        .orElseThrow(() -> new ApplicationException("주문에 해당하는 메뉴가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

    return new MenuOrderResponseDto(findMenu.getId(), findMenu.getMenuName(), findMenu.getPrice());

  }

  // 메뉴 삭제
  @Transactional
  public void deleteMenu(Long userId, Long menuId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

    // 삭제할 메뉴 있는지 확인
    Menu findMenu = menuRepository.findById(menuId)
        .orElseThrow(() -> new ApplicationException("존재하지 않는 메뉴입니다.", HttpStatus.NOT_FOUND));

    // 본인의 가게인지 확인
    if (!findMenu.getStore().getUser().getId().equals(userId)) {
      throw new ApplicationException("본인 가게의 메뉴만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
    }

    findMenu.delete();
  }
}