package com.example.delivery.domain.login.repository;

import com.example.delivery.domain.login.entity.User;
import com.example.delivery.domain.store.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  @Query("SELECT u FROM User u WHERE u.id = :loginedId AND u.role = :role")
  Optional<User> findByIdAndRoleIsOwner(Long loginedId, Role role);


  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
