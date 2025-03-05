package com.example.delivery.config.aop;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class OrderAccessLoggingAspect {

    ObjectMapper objectMapper;

    @Around("@annotation(com.example.delivery.config.aop.annotation.Order)")
    public Object logOrderApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object result = joinPoint.proceed();

        //result에서 실행 결과 가져오기
        String responseBody = objectMapper.writeValueAsString(result);

        if(method.getName().equals("createOrder"))
        {
            //log.info("주문 생성 - , storeId={}, Timestamp={}",storeId,System.currentTimeMillis());
        }
        else
        {
            //log.info("주문 변경 - orderId={}, storeId={}, Timestamp={}", orderId, storeId, System.currentTimeMillis());
        }

        return result;
    }
}
