package com.example.delivery.domain.menu.service;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;

  //메뉴 생성
  public MenuCreateResponseDto createMenu(Long storeId, String menuName, Long price) {
    Store findStore = storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

    Menu menu = new Menu(menuName, price, findStore);
    Menu savedMenu = menuRepository.save(menu);

    return new MenuCreateResponseDto(savedMenu.getId(), savedMenu.getMenuName(), savedMenu.getPrice(), savedMenu.getCreatedAt());
  }

  //메뉴 수정
  @Transactional
  public MenuUpdateResponseDto updateMenu(Long menuId, String menuName, Long price) {
    Menu findMenu = menuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

    findMenu.update(menuName, price);

    return new MenuUpdateResponseDto(
        findMenu.getId(),
        findMenu.getMenuName(),
        findMenu.getPrice(),
        findMenu.getUpdatedAt());

  }

  // 가게별 메뉴 목록 조회
  public List<MenuFindResponseDto> getMenusByStore(Long storeId) {
    // 조회하려는 가게가 있는지 확인
     storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

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
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    // 주문에 해당하는 메뉴 찾기
    Menu findMenu = orderRepository.findMenuByOrderId(orderId);
    if (findMenu == null){
      throw new IllegalArgumentException("주문에 해당하는 메뉴가 존재하지 않습니다.");
    }

    return new MenuOrderResponseDto(findMenu.getId(), findMenu.getMenuName(), findMenu.getPrice());

  }
  // 메뉴 삭제
  @Transactional
  public void deleteMenu(Long menuId) {
    Menu findMenu = menuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    findMenu.delete();
  }
}