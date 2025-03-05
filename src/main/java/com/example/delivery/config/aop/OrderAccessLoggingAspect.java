package com.example.delivery.config.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class OrderAccessLoggingAspect {

    private final HttpServletRequest request;

    @Around("@annotation(com.example.delivery.config.aop.annotation.Order)")
    public Object logOrderApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        Long orderId = (Long) request.getAttribute("orderId");
        Long storeId = (Long) request.getAttribute("storeId");
        log.info("주문 변경 - orderId={}, storeId={}, Timestamp={}", orderId, storeId, System.currentTimeMillis());

        return result;
    }
}
