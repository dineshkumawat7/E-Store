package com.ecommerce.shop.payload;

import com.ecommerce.shop.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private LocalDateTime createdAt;
    private String description;
    private Integer amount;
    private String payment;
    private OrderStatus orderStatus;
    private Integer totalAmount;
    private Integer discount;
    private String trackingId;
    private String userName;
    private String couponName;
    private Long addressId;
    private OrderAddressDto orderAddressDto;
    private List<CartItemsDto> cartItems;

}
