package com.example.delivery.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoresResponseDto {
    private String storeName;
    private LocalTime openedAt;
    private LocalTime closedAt;
    private int orderMin;
    private double rating;
}
