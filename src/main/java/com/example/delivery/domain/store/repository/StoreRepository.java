package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE CONCAT('%', :storeName, '%') ")
    Page<Store> findByStoreName(String storeName, Pageable pageable);
}
