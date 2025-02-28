package com.example.delivery.domain.store.response;

import com.example.delivery.domain.menu.entity.Menu;
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
    private List<Menu> menuList;
    private double rating;
}
