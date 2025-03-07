package com.example.delivery.domain.menu.controller;

import com.example.delivery.common.utils.JwtUtil;
import com.example.delivery.domain.menu.dto.requestDto.MenuUpdateRequstDto;
import com.example.delivery.domain.menu.dto.requestDto.MenuCreateRequestDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuCreateResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuOrderResponseDto;
import com.example.delivery.domain.menu.dto.responseDto.MenuUpdateResponseDto;
import com.example.delivery.domain.menu.service.MenuService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
  private final MenuService menuService;

  // 메뉴 생성
  @PostMapping("/stores/{storeId}")
  public ResponseEntity<MenuCreateResponseDto> createMenu(
      @RequestHeader(name = "Authorization") String authorization,
      @PathVariable Long storeId,
      @Valid @RequestBody MenuCreateRequestDto requestDto) {

    Long userId = JwtUtil.extractUserId(authorization);
    MenuCreateResponseDto menuCreateResponseDto = menuService.createMenu(userId, storeId, requestDto.getMenuName(), requestDto.getPrice());
    return new ResponseEntity<>(menuCreateResponseDto, HttpStatus.CREATED);

  }

  // 메뉴 수정
  @PutMapping("/{menuId}")
  public ResponseEntity<MenuUpdateResponseDto> updateMenu(
      @RequestHeader(name = "Authorization") String authorization,
      @PathVariable Long menuId,
      @Valid @RequestBody MenuUpdateRequstDto requstDto) {
    Long userId = JwtUtil.extractUserId(authorization);

    MenuUpdateResponseDto menuUpdateResponseDto = menuService.updateMenu(userId, menuId, requstDto.getMenuName(), requstDto.getPrice());
    return new ResponseEntity<>(menuUpdateResponseDto, HttpStatus.OK);
  }

  // 가게별 메뉴 조회
  @GetMapping("/stores/{storeId}")
  public ResponseEntity<List<MenuFindResponseDto>> getMenusByStore(@PathVariable Long storeId) {
    List<MenuFindResponseDto> menuList = menuService.getMenusByStore(storeId);
    return new ResponseEntity<>(menuList, HttpStatus.OK);
  }

  // 주문 내역 메뉴 조회
  @GetMapping("/orders/{orderId}")
  public ResponseEntity<MenuOrderResponseDto> getMenusByOrder(@PathVariable Long orderId) {
    MenuOrderResponseDto menuOrderResponseDto = menuService.getMenusByOrder(orderId);
    return new ResponseEntity<>(menuOrderResponseDto, HttpStatus.OK);
  }

  // 메뉴 삭제
  @DeleteMapping("/{menuId}")
  public ResponseEntity<String> deleteMenu(
      @RequestHeader(name = "Authorization") String authorization,
      @PathVariable Long menuId) {
     Long userId = JwtUtil.extractUserId(authorization);

    menuService.deleteMenu(userId, menuId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
