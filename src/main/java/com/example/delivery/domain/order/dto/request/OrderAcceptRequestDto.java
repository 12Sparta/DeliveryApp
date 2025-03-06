package com.example.delivery.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderAcceptRequestDto {
    private Long userId;
    private Long storeId;
}
