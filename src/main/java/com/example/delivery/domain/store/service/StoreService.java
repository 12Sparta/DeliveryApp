package com.example.delivery.domain.store.service;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.dto.request.UpdateStoreDto;
import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.ReviewRepository;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.dto.request.RegistStoreDto;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.repository.UserRepository;
import com.example.delivery.login.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        Optional<User> user = userRepository.findById(loginedId); // 추후 null 체크 일괄 처리
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not OWNER");
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

    public StoreResponseDto find(RegistStoreDto dto, Long storeId) {

        // 가게 조회
        Optional<Store> optional = storeRepository.findById(storeId);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong Id");
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
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction));

        Page<Store> pages;
        if (search.isEmpty()) {
            pages = storeRepository.findAll(pageable);
        } else {
            pages = storeRepository.findByStoreName(search, pageable);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong Id");
        }else if(!store.get().getId().equals(loginedId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Your Store");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong Id");
        }else if(!store.get().getId().equals(loginedId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Your Store");
        }

        store.get().delete();
    }
}
