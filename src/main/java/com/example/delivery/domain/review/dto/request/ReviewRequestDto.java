package com.example.delivery.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "별점을 입력해주세요.")
    @Min(1)
    @Max(5)
    private Integer rating; // 별점 (1~5점)

    @Size(max = 600, message = "리뷰는 최대 600자까지 입력 가능합니다.")
    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content; // 리뷰 내용

}