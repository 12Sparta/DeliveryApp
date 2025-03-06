package com.example.delivery.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateRequestDto {
    private Long menuId;
    private Long storeId;
}
