package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.enums.OrderStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    Order findByIdAndUserIdAndOrderStatus(Long orderId, Long userId, OrderStatus orderStatus);

    List<Order> findAllByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    Order findByTrackingId(String trckingId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
