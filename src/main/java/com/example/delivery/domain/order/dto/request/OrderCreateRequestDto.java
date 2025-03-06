package com.example.delivery.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderCreateRequestDto {
    private Long menuId;
    private Long storeId;
}
