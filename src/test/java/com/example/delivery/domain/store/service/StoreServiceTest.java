package com.example.delivery.domain.store.service;


import com.example.delivery.domain.store.Role;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        Long loginedId = 1L;
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9,0), LocalTime.of(21,0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.of(user));   // 회원 등급 검증 통과
        given(storeRepository.findByOwnerId(loginedId)).willReturn(List.of()); // 가게 수 검증 통과

        // when
        assertDoesNotThrow(() -> storeService.regist(dto,1L));  // 예외 발생시 실패

        // then
        ArgumentCaptor<Store> storeCaptor = ArgumentCaptor.forClass(Store.class);   // 추적할 저장용 객체 생성
        verify(storeRepository, times(1)).save(storeCaptor.capture());  // 해당 메서드가 1번 실행되는지 확인 후 저장된 객체 캡쳐

        Store savedStore = storeCaptor.getValue();

        assertEquals(dto.getStoreName(), savedStore.getStoreName());    // 저장된 객체와 정보 비교
    }

    @Test
    void 가게_등록시_사용자가_존재하지_않거나_점주로_등록되지_않은_경우() {
        // given
        Long loginedId = 1L;
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9,0), LocalTime.of(21,0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.of(user));
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