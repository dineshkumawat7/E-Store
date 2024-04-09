package com.ecommerce.shop.payload;

import com.ecommerce.shop.entity.OrderAddress;
import lombok.Data;

@Data
public class PlaceOrderDto {
    private Long userId;
    private OrderAddress address;


}
