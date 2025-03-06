package com.example.delivery.config.aop;


import com.example.delivery.common.Status;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.Map;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class OrderAccessLoggingAspect {

    private final ObjectMapper objectMapper;

    @Around("@annotation(com.example.delivery.config.aop.annotation.Order)")
    public Object logOrderApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object result = joinPoint.proceed();

        //result에서 실행 결과 가져오기
        String responseBody = objectMapper.writeValueAsString(result);
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode bodyNode = rootNode.get("body");
        Long storeId = bodyNode.get("storeId").asLong();
        Long userId = bodyNode.get("userId").asLong();
        Long orderId = bodyNode.get("orderId").asLong();
        Status status = Status.valueOf(bodyNode.get("status").asText());


        log.info("주문 정보 : method = {}, storeId = {}, userId = {}, orderId = {}, status = {}", method.getName(), storeId, userId, orderId, status);

        return result;
    }
}
