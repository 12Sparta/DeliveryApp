package com.example.delivery.common.scheduler;

import com.example.delivery.domain.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CartCheckScheduler {

    private final CartRepository cartRepository;

    @Scheduled(cron = "0 0 5 * * ?") // 매일 오전 5시에 실행
    public void cleanExpiredCarts() {

        // 만료시간 설정
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
        // 만료된 장바구니 삭제
        cartRepository.deleteByUpdatedAtBefore(expiredTime);
    }
}
