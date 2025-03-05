package com.example.delivery.domain.common.entity;

public enum OrderStatus {
    PENDING, // 주문 접수 중
    COOKING, // 조리 중
    DELIVERING, // 배달 중
    COMPLETED // 배달 완료
}
