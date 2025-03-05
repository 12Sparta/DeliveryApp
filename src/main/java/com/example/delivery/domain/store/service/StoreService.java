package com.example.delivery.domain.store.service;

import com.example.delivery.common.Role;
import com.example.delivery.common.exception.ApplicationException;
import com.example.delivery.domain.login.repository.UserRepository;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.review.Repository.ReviewRepository;
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

    @Transactional
    public void regist(RegistStoreDto dto, Long loginedId) {

        Optional<User> user = userRepository.findByIdAndRoleIsOwner(loginedId, Role.OWNER); // 추후 null 체크 일괄 처리, 여기 순서만 어떻게 하면 user 한번만 조회하면 될 듯
        if (user.isEmpty()) {
            throw new ApplicationException("You are not OWNER", HttpStatus.FORBIDDEN);
        }
        if(storeRepository.findByOwnerId(loginedId).size() > 2){
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

    public StoreResponseDto find(Long storeId) {

        // 가게 조회
        Optional<Store> optional = storeRepository.findByIdAndDeletedAtIsNull(storeId);
        if (optional.isEmpty()) {
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        }
        Store store = optional.get();

        // 메뉴 조회
        List<Menu> menuList = menuRepository.findByStoredId(storeId);

        return new StoreResponseDto(
                store.getUser().getName(),
                store.getStoreName(),
                store.getOpenedAt(),
                store.getClosedAt(),
                store.getOrderMin(),
                menuList,
                store.getAbout(),
                reviewRepository.findReviewAvg(storeId)// 리뷰 평균 별점
        );
    }

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
                reviewRepository.findReviewAvg(store.getId()) // 개선할 필요 있음
        ));
    }

    @Transactional
    public void update(Long loginedId, UpdateStoreDto dto, Long storeId) {

        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()){
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        }else if(!store.get().getUser().getId().equals(loginedId)){
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

    @Transactional
    public void closeStore(Long loginedId, Long storeId) {

        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()){
            throw new ApplicationException("Wrong Id", HttpStatus.NOT_FOUND);
        }else if(!store.get().getUser().getId().equals(loginedId)){
            throw new ApplicationException("Not Your Store", HttpStatus.FORBIDDEN);
        }

        store.get().delete();
    }
}
