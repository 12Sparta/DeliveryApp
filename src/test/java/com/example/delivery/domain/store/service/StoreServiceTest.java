package com.example.delivery.domain.store.service;


import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.review.Repository.ReviewRepository;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.of(user));   // 회원 등급 검증 통과
        given(storeRepository.findByOwnerId(loginedId)).willReturn(List.of()); // 가게 수 검증 통과

        // when
        assertDoesNotThrow(() -> storeService.regist(dto, 1L));  // 예외 발생시 실패

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
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "testStoreAbout1");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.empty()); // 점주 계정이 아닐 경우

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.regist(dto, loginedId));
        assertEquals("You are not OWNER", exception.getMessage());
    }

    @Test
    void 가게_등록시_이미_등록된_가게가_3개를_초과하는_경우() {
        // given
        Long loginedId = 1L;
        List<Long> storeList = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        RegistStoreDto dto = new RegistStoreDto("testStore1", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "testStoreAbout1");
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");

        given(userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER)).willReturn(Optional.of(user));   // 회원 등급 검증 통과
        given(storeRepository.findByOwnerId(loginedId)).willReturn(storeList); // 가게 수 3개 이상으로 검증 실패

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.regist(dto, loginedId));
        assertEquals("You can register up to 3 stores", exception.getMessage());
    }

    @Test
    void 단건_조회_상점_Id_없는_경우() {
        // given
        Long storeId = 1L;

        given(storeRepository.findByIdAndDeletedAtIsNull(storeId)).willReturn(Optional.empty());   // 상점 조회 실패

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.find(storeId));
        assertEquals("Wrong Id", exception.getMessage());
    }

    @Test
    void 단건_조회_테스트() {
        // given
        Long storeId = 1L;
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(9, 0), LocalTime.of(21, 0));
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
    void 검색어_없는_경우_전체_조회() {
        // given
        int page = 1;
        int size = 10;
        String search = "";
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, "storeName"));
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store1 = new Store(user, "storeName1", 15000, "storeAbout1", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Store store2 = new Store(user, "storeName2", 15000, "storeAbout2", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Page<Store> pages = new PageImpl<>(List.of(store1, store2), pageable, 2);

        given(storeRepository.findByDeletedAtIsNull(pageable)).willReturn(pages);   // 반환값 설정

        // when
        Page<StoresResponseDto> result = storeService.findAll(search, page, size, direction);

        // then
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getStoreName(), "storeName1");
    }

    @Test
    void 검색어_있는_경우_전체_조회() {
        // given
        int page = 1;
        int size = 10;
        String search = "store";
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, "storeName"));
        User user = new User(1L, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store1 = new Store(user, "storeName1", 15000, "storeAbout1", LocalTime.of(9, 0), LocalTime.of(21, 0));
        Store store2 = new Store(user, "storeName2", 15000, "storeAbout2", LocalTime.of(8, 0), LocalTime.of(22, 0));
        Page<Store> pages = new PageImpl<>(List.of(store1, store2), pageable, 2);

        given(storeRepository.findByStoreNameAndDeletedAtIsNull(search, pageable)).willReturn(pages);   // 반환값 설정

        // when
        Page<StoresResponseDto> result = storeService.findAll(search, page, size, direction);

        // then
        assertNotNull(result);
        assertEquals(result.getContent().get(0).getStoreName(), "storeName1");
    }

    @Test
    void 가게_정보를_업데이트_하는_경우() {
        // given
        Long loginedId = 1L;
        Long storeId = 1L;
        UpdateStoreDto dto = new UpdateStoreDto("storeName123", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "newAbout");
        User user = new User(loginedId, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(10, 0), LocalTime.of(20, 0));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        assertDoesNotThrow(() -> storeService.update(loginedId, dto, storeId));

        // then
        assertEquals("storeName123", store.getStoreName());
    }

    @Test
    void 업데이트_하려는_가게가_존재하지_않는_경우() {
        // given
        Long loginedId = 1L;
        Long storeId = 123L;  // 존재하지 않는 ID
        UpdateStoreDto dto = new UpdateStoreDto("storeName123", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "newAbout");

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.update(loginedId, dto, storeId));
        assertEquals("Wrong Id", exception.getMessage());
    }

    @Test
    void 업데이트_하려는_가게가_본인_가게가_아닌_경우() {
        // given
        Long loginedId = 1L;
        Long storeId = 2L;
        Long ownerId = 99L;
        UpdateStoreDto dto = new UpdateStoreDto("새로운 가게 이름", LocalTime.of(9, 0), LocalTime.of(21, 0), 15000, "새로운 소개");
        User user = new User(ownerId, "other", "other@test.com", "otherPw", Role.OWNER, "otherAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(10, 0), LocalTime.of(20, 0));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.update(loginedId, dto, storeId));
        assertEquals("Not Your Store", exception.getMessage());
    }

    @Test
    void 가게_삭제_성공() {
        // given
        Long loginedId = 1L;
        Long storeId = 1L;
        User user = new User(loginedId, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(10, 0), LocalTime.of(20, 0));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        assertDoesNotThrow(() -> storeService.closeStore(loginedId, storeId));

        // then
        assertNotNull(store.getDeletedAt());
    }

    @Test
    void 삭제하려는_가게가_존재하지_않는_경우() {
        // given
        Long loginedId = 1L;
        Long storeId = 123L;

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.closeStore(loginedId, storeId));
        assertEquals("Wrong Id", exception.getMessage());
    }

    @Test
    void 삭제하려는_가게가_본인_가게가_아닌_경우() {
        // given
        Long loginedId = 1L;
        Long storeId = 2L;
        Long ownerId = 123L;
        User user = new User(ownerId, "testName", "test@test.com", "testpw", Role.OWNER, "testAddress");
        Store store = new Store(user, "storeName", 15000, "storeAbout", LocalTime.of(10, 0), LocalTime.of(20, 0));

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> storeService.closeStore(loginedId, storeId));
        assertEquals("Not Your Store", exception.getMessage());
    }
}