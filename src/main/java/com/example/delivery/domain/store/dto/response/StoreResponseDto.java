package com.example.delivery.domain.store.dto.response;

import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private String name;
    private String storeName;
    private LocalTime openedAt;
    private LocalTime closedAt;
    private int orderMin;
    private List<MenuFindResponseDto> menuList;
    private String about;
    private double rating;

    public StoreResponseDto(Store store, List<MenuFindResponseDto> menuList, double rating) {
        this.name = store.getUser().getName();
        this.storeName = store.getStoreName();
        this.openedAt = store.getOpenedAt();
        this.closedAt = store.getClosedAt();
        this.menuList = menuList;
        this.about = store.getAbout();
        this.orderMin = store.getOrderMin();
        this.rating = rating;
    }
}
