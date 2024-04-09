package com.ecommerce.shop.services.user.order;

import java.util.List;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.OrderAddressDto;
import com.ecommerce.shop.payload.OrderDto;

public interface OrderService {
    void createOrder(User user);

    OrderAddressDto checkout(Long userId, OrderAddressDto orderAddressDto);

    List<OrderAddressDto> getAllOrderAddressByUser(Long userId);

    OrderAddressDto getOrderAddressByUser(Long orderAddressId);

    void deleteOrderAddressByIdAndUserId(Long orderAddressId, Long userId);

    void selectOrderAddress(Long orderAddressId, Long userId);

    void updateOrderAddressByIdAndUser(Long orderAddressId, Long userId, OrderAddressDto orderAddressDto);

    OrderDto placeOrder(Long userId, Long orderId);

    OrderDto getCurrentOrderByUser(Long userId);

    List<OrderDto> getAllPlacedOrders(Long userId);

    void addPaymentInOrder(Long userId, String paymentMode);

    OrderDto getOrderByTrackingId(String trackingId);

}
