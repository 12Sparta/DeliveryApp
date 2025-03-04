package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.dto.response.StoresResponseDto;
import com.example.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE CONCAT('%', :storeName, '%') ")
    Page<Store> findByStoreName(String storeName, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.user.id = :loginedId AND s.deletedAt IS NULL")
    List<Store> findByOwnerId(Long loginedId);

    Optional<Store> findByIdAndDeletedAtIsNull(Long storeId);

    Page<Store> findAllAndDeletedAtIsNull(Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE CONCAT('%', :storeName, '%') AND s.deletedAt IS NULL")
    Page<Store> findByStoreNameAndDeletedAtIsNull(String search, Pageable pageable);
}
