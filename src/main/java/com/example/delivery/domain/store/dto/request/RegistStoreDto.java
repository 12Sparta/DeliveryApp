package com.example.delivery.domain.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class RegistStoreDto {

    private final String storeName;
    private final LocalTime openedAt;
    private final LocalTime closedAt;
    private final Integer orderMin;
    private final String about;
}
