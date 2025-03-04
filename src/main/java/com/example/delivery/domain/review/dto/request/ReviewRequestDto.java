package com.example.delivery.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "별점을 입력해주세요.")
    @Min(1)
    @Max(5)
    private Integer rating; // 별점 (1~5점)

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content; // 리뷰 내용

}
