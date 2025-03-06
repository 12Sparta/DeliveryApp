package com.example.delivery.domain.order.dto.response;

import com.example.delivery.common.Status;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long storeId;
    private Long userId;
    private Long orderId;
    private Status status;

    public OrderResponseDto(Long storeId, Long userId, Long orderId, Status status) {
        this.storeId = storeId;
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
    }
}
