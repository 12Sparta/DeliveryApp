package com.example.delivery.domain.store.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class RegistStoreDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s]{1,30}$")
    private final String storeName;

    @NotNull
    private final LocalTime openedAt;

    @NotNull
    private final LocalTime closedAt;

    @Min(0)
    @NotNull
    private final Integer orderMin;

    @Size(max = 500)
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!\\\"#$%&'()*+,-./:;<=>?@[\\\\]^_`{|}~\\\\s]*$")
    private final String about;
}
