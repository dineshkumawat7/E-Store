package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class AddProductInWishlistDto {
    private Long userId;
    private Long productId;
}
