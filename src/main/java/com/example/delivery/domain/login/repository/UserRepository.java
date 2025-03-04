package com.example.delivery.domain.login.repository;

import com.example.delivery.domain.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
