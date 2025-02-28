package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.store.request.RegistStoreDto;
import com.example.delivery.domain.store.response.StoreResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Void> regist(
            //@RequestHeader("Authorization") String token,
            @ModelAttribute RegistStoreDto dto){

        Long loginedId = 1L;

        storeService.regist(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> find(
            //@RequestHeader("Authorization") String token,
            @ModelAttribute RegistStoreDto dto,
            @PathVariable Long storeId){

        Long loginedId = 1L; // 이후에 어떻게 할지 다시 생각해야 함(장바구니 같은 기능 때문)

        storeService.find(dto, storeId);

        return new ResponseEntity<>(HttpStatus.OK, StoreResponseDto)
    }
}
