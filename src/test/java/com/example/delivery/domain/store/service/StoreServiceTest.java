package com.example.delivery.domain.store.service;


import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.review.Repository.ReviewRepository;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ReviewRepository reviewRepository;
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
    void 가게_등록시_사용자가_점주로_등록되지_않은_경우() {
        // given
        Long loginedId = 1L;
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9,0), LocalTime.of(21,0), 15000, "testStoreAbout1");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.empty()); // 점주 계정이 아닐 경우

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, ()-> storeService.regist(dto, loginedId));
        assertEquals("You are not OWNER", exception.getMessage());
    }

    @Test
    void 가게_등록시_이미_등록된_가게가_3개를_초과하는_경우() {
        // given
        Long loginedId = 1L;
        List<Long> storeList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9,0), LocalTime.of(21,0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.of(user));   // 회원 등급 검증 통과
        given(storeRepository.findByOwnerId(loginedId)).willReturn(storeList); // 가게 수 3개 이상으로 검증 실패

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, ()-> storeService.regist(dto, loginedId));
        assertEquals("You can register up to 3 stores", exception.getMessage());
    }

    @Test
    void find() {
//        Optional<Store> optional = storeRepository.findByIdAndDeletedAtIsNull(storeId);
//        if (optional.isEmpty()) {
//            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
//        }
//        Store store = optional.get();
//
//        // 메뉴 조회
//        List<Menu> menuList = menuRepository.findByStoredId(storeId);
//
//        return new StoreResponseDto(
//                store.getUser().getName(),
//                store.getStoreName(),
//                store.getOpenedAt(),
//                store.getClosedAt(),
//                store.getOrderMin(),
//                menuList,
//                store.getAbout(),
//                reviewRepository.findReviewAvg(storeId)// 리뷰 평균 별점
//        );
    }

    @Test
    void 단건_조회_상점_Id_없는_경우 () {
        // given
        Long storeId = 1L;

        given(storeRepository.findByIdAndDeletedAtIsNull(storeId)).willReturn(Optional.empty());   // 상점 조회 실패

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, ()-> storeService.find(storeId));
        assertEquals("Wrong Id", exception.getMessage());
    }

    @Test
    void 단건_조회_테스트 () {
        // given
        Long storeId = 1L;
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(9,0), LocalTime.of(21,0));
        List<Menu> menuList = List.of(new Menu("Bread", 5000L, store), new Menu("Pizza", 10000L, store));
        double avgRating = 5.0;

        given(storeRepository.findByIdAndDeletedAtIsNull(storeId)).willReturn(Optional.of(store));  // 가게 조회
        given(menuRepository.findByStoredId(storeId)).willReturn(menuList);                         // 메뉴 조회
        given(reviewRepository.findReviewAvg(storeId)).willReturn(avgRating);                       // 평점 불러오기

        // when
        StoreResponseDto dto = storeService.find(storeId);

        // then
        assertNotNull(dto);
        assertEquals(store.getStoreName(), dto.getStoreName());
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