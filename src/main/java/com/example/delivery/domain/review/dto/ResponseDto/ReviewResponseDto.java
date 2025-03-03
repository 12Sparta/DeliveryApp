package com.example.delivery.domain.review.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private final Long id; // 리뷰 id
    private final Long userId; // 리뷰 작성자
    private final String username; // 작성자 이름
    private final Integer rating; // 별점
    private final String content; // 리뷰 내용
    private final LocalDateTime createdAt; // 생성일

}
