package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Role;
import com.ecommerce.shop.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByRoles(Role role);

    boolean existsByEmail(String email);

    @SuppressWarnings("null")
    boolean existsById(Long userId);

}
