package com.example.delivery.domain.menu.repository;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.store.entity.Store;

import com.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByStore(Store store);


}
