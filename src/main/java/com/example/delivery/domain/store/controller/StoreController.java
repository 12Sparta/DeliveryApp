package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.common.OrderBy;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
            @ModelAttribute RegistStoreDto dto) {

        Long loginedId = 1L;

        storeService.regist(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> find(
            //@RequestHeader("Authorization") String token,
            @ModelAttribute RegistStoreDto dto,
            @PathVariable Long storeId) {

        Long loginedId = 1L; // 이후에 어떻게 할지 다시 생각해야 함(장바구니 같은 기능 때문)

        return new ResponseEntity<>(storeService.find(dto, storeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StoresResponseDto>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            //@RequestParam(defaultValue = "RATING") OrderBy orderBy, 기본과제 완성 후 추가
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        return new ResponseEntity<>(storeService.findAll(search, page, size, direction), HttpStatus.OK);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(
            //@RequestHeader("Authorization") String token,
            @ModelAttribute UpdateStoreDto dto,
            @PathVariable Long storeId){

        Long loginedId = 1L;

        storeService.update(loginedId, dto, storeId);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> closeStore(
            //@RequestHeader("Authorization") String token,
            @PathVariable Long storeId){

        Long loginedId = 1L;

        storeService.closeStore(loginedId, storeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
