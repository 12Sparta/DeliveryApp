package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  // sotreId에 해당하는 Menu 목록 조회하는 쿼리(삭제된 메뉴는 포함 안함)
  @Query("SELECT m FROM Menu m JOIN FETCH m.store WHERE m.store.id = :storeId AND m.deletedAt IS NULL")
  List<Menu> findMenusByStoreId(@Param("storeId") Long storeId);



  List<Menu> findByStoredId(Long storeId);
}
