package com.example.delivery.domain.login.repository;

import com.example.delivery.common.Role;
import com.example.delivery.domain.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  @Query("SELECT u FROM User u WHERE u.id = :loginedId AND u.role = :role")
  Optional<User> findByIdAndRoleIsOwner(Long loginedId, Role role);




  Optional<User> findByEmail(String email);
}
