package com.example.delivery.domain.store.controller;

import com.example.delivery.common.utils.JwtUtil;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.service.StoreService;
import jakarta.validation.Valid;
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
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody RegistStoreDto dto) {

        Long loginedId = JwtUtil.extractUserId(token);
//        Long loginedId = 1L;

        storeService.regist(dto, loginedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> find(
            @PathVariable Long storeId) {

        return new ResponseEntity<>(storeService.find(storeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StoresResponseDto>> findAll(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        return new ResponseEntity<>(storeService.findAll(search, page, size, direction), HttpStatus.OK);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateStoreDto dto,
            @PathVariable Long storeId) {

        Long loginedId = JwtUtil.extractUserId(token);

        storeService.update(loginedId, dto, storeId);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> closeStore(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId) {

        Long loginedId = JwtUtil.extractUserId(token);

        storeService.closeStore(loginedId, storeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{storeId}")
    public ResponseEntity<Void> favorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Long storeId) {

        Long loginedId = JwtUtil.extractUserId(token);

        storeService.favorite(loginedId, storeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
