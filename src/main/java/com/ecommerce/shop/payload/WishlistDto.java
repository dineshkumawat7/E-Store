package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class WishlistDto {
    private Long id;
    private Integer price;
    private Long productId;
    private String productName;
    private String productImage;
    private Long userId;
}
