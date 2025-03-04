package com.example.delivery.domain.review.Dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long userId; // 작성자 id

    private String username; // 작성자 이름

    private Integer rating; // 별점 (1~5점)

    private String content; // 리뷰 내용

}
