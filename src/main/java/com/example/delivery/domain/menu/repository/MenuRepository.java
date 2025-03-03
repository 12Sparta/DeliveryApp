package com.example.delivery.domain.menu.repository;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.store.entity.Store;

import com.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND m.deletedAt IS NULL")
  List<Menu> findMenusByStoreId(@Param("storeId") Long storeId);


}
