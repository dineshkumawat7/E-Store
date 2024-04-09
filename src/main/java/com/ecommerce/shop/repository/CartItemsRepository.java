package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long orderId, Long userId);

    Optional<CartItems> findByProductIdAndUserId(Long productId, Long userId);

    Optional<CartItems> findByIdAndUserId(Long cartItemId, Long userId);

    Optional<CartItems> findByOrderIdAndUserId(Long orderId, Long userId);

    @Procedure(name = "Sp_GetCartItemByUserId")
    Optional<CartItems> getCartItemsByUserId(@Param("userIdParam") Long userId);

}
