package com.ecommerce.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.shop.entity.OrderAddress;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long> {

    List<OrderAddress> findByUserId(long userId);

    OrderAddress findByIdAndUserId(long orderAddressId, long userId);
}
