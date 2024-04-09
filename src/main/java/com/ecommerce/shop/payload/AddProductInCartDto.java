package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class AddProductInCartDto {
    private Long userId;
    private Long productId;
    private Long orderId;
}
