package com.example.delivery.domain.store.service;


import com.example.delivery.domain.store.Role;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    void 가게_등록시_정상적으로_등록_되는지_여부() {
        // given
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9,0), LocalTime.of(21,0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(1L, Role.OWNER)).willReturn(Optional.of(user));   // 회원 등급 검증 통과
        given(storeRepository.findByOwnerId(1L)).willReturn(List.of()); // 가게 수 검증 통과

        // when
        assertDoesNotThrow(() -> storeService.regist(dto, 1L));

        // then
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void 가게_등록시_사용자가_존재하지_않는_경우() {
        // given
        // when
        // then
    }

    @Test
    void 가게_등록시_이미_등록된_가게가_3개를_초과하는_경우() {
        // given
        // when
        // then
    }

    @Test
    void find() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void closeStore() {
    }
}