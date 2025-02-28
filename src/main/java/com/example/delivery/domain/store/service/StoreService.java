package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.store.request.RegistStoreDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    public void regist(RegistStoreDto dto, Long loginedId) {

        User user = userRepository.findById(loginedId).get(); // 추후 null 체크 일괄 처리

        Store store = new Store(
                user,
                dto.getStoreName(),
                dto.getOrderMin(),
                dto.getAbout(),
                dto.getOpenedAt().atStartOfDay(),
                dto.getClosedAt().atStartOfDay()
        );

        storeRepository.save(store);
    }
}
