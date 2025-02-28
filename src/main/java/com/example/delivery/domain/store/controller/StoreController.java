package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.store.request.RegistStoreDto;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public ResponseEntity<Void> regist(
            //@RequestHeader("Authorization") String token,
            @ModelAttribute RegistStoreDto dto){

        Long loginedId = 1L;

        storeService.regist(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
