package com.example.delivery.domain.store.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RegistStoreDto {

    private final String storeName;
    private final LocalDate openedAt;
    private final LocalDate closedAt;
    private final int orderMin;
    private final String about;
}
