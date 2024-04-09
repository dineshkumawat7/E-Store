package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Wishlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);

    List<Wishlist> findByUserId(Long userId);
}
