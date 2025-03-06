package com.example.delivery.domain.store.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.dto.responseDto.MenuFindResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Favorite;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.store.repository.FavoriteRepository;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    // 가게 등록
    @Transactional
    public void regist(RegistStoreDto dto, Long loginedId) {

        Optional<User> user = userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER); // 추후 null 체크 일괄 처리, 여기 순서만 어떻게 하면 user 한번만 조회하면 될 듯
        // 사용자 계정 확인
        if (user.isEmpty()) {
            throw new ApplicationException("You are not OWNER", HttpStatus.FORBIDDEN);
        }
        // 등록한 가게 체크
        if (storeRepository.findByOwnerId(loginedId).size() > 2) {
            throw new ApplicationException("You can register up to 3 stores", HttpStatus.BAD_REQUEST);
        }

        Store store = new Store(
                user.get(),
                dto.getStoreName(),
                dto.getOrderMin(),
                dto.getAbout(),
                dto.getOpenedAt(),
                dto.getClosedAt()
        );

        storeRepository.save(store);
    }

    // 가게 단건 조회
    public StoreResponseDto find(Long storeId) {

        // 가게 조회
        Optional<Store> optional = storeRepository.findByIdAndDeletedAtIsNull(storeId);
        if (optional.isEmpty()) {
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        }
        Store store = optional.get();

        // 메뉴 조회
        List<MenuFindResponseDto> menuList = menuRepository.findByStoredId(storeId);

        return new StoreResponseDto(
                store,
                menuList,
                reviewRepository.findReviewAvg(storeId).orElse(0.0) // 리뷰 평균 별점
        );
    }

    // 가게 다건 조회
    public Page<StoresResponseDto> findAll(String search, int page, int size, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, "storeName"));

        Page<Store> pages;
        if (!search.isEmpty()) {
            pages = storeRepository.findByStoreNameAndDeletedAtIsNull(search, pageable);
        } else {
            pages = storeRepository.findByDeletedAtIsNull(pageable);
        }

        return pages.map(store -> new StoresResponseDto(
                store.getStoreName(),
                store.getOpenedAt(),
                store.getClosedAt(),
                store.getOrderMin(),
                reviewRepository.findReviewAvg(store.getId()).orElse(0.0) // 개선할 필요 있음
        ));
    }

    // 가게 정보 수정
    @Transactional
    public void update(Long loginedId, UpdateStoreDto dto, Long storeId) {

        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isEmpty()) {
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        } else if (!store.get().getUser().getId().equals(loginedId)) {
            throw new ApplicationException("Not Your Store", HttpStatus.FORBIDDEN);
        }

        store.get().save(
                dto.getStoreName(),
                dto.getOpenedAt(),
                dto.getClosedAt(),
                dto.getOrderMin(),
                dto.getAbout()
        );
    }

    // 가게 삭제
    @Transactional
    public void closeStore(Long loginedId, Long storeId) {

        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isEmpty()) {
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        } else if (!store.get().getUser().getId().equals(loginedId)) {
            throw new ApplicationException("Not Your Store", HttpStatus.FORBIDDEN);
        }

        store.get().delete();
    }

    // 찜 등록
    @Transactional
    public void favorite(Long loginedId, Long storeId) {
        Optional<User> user = userRepository.findById(loginedId);
        Optional<Store> store = storeRepository.findByIdAndDeletedAtIsNull(storeId);

        // 가게 존재 확인
        if (store.isEmpty()) {
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        }
        // 즐겨찾기 등록 여부 확인
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndStoreId(loginedId, storeId);
        // 이미 즐겨찾기에 등록된 경우 취소 처리, 등록 안된 경우 즐겨찾기 등록
        if (favorite.isEmpty()) {
            favoriteRepository.save(new Favorite(user.get(), store.get()));
        } else {
            favoriteRepository.delete(favorite.get());
        }
    }


}
