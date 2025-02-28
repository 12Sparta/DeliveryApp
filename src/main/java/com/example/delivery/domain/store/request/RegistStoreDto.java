package com.example.delivery.domain.store.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RegistStoreDto {

    private String storeName;
    private LocalDate openedAt;
    private LocalDate closedAt;
    private int orderMin;
    private String about;
}
