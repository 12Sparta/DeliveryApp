package com.example.delivery.domain.store.service;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepositiry;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.request.RegistStoreDto;
import com.example.delivery.domain.store.response.StoreResponseDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepositiry menuRepositiry;


    public void regist(RegistStoreDto dto, Long loginedId) {

        Optional<User> user = userRepository.findById(loginedId); // 추후 null 체크 일괄 처리
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not OWNER");
        }

        Store store = new Store(
                user.get(),
                dto.getStoreName(),
                dto.getOrderMin(),
                dto.getAbout(),
                dto.getOpenedAt().atStartOfDay(),
                dto.getClosedAt().atStartOfDay()
        );

        storeRepository.save(store);
    }

    public StoreResponseDto find(RegistStoreDto dto, Long storeId) {
        Optional<Store> optional = storeRepository.findById(storeId);
        if(optional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong Id");
        }
        Store store = optional.get();

        List<Menu> menuList = menuRepositiry.findByStoredId(storeId);

        return new StoreResponseDto(
                store.getUser().getName(),
                store.getStoreName(),
                store.getOpenedAt(),
                store.getClosedAt(),
                store.getOrderMin(),
                store.getAbout(),
                menuList,

        );
    }
}
