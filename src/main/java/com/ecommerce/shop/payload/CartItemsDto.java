package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class CartItemsDto {
    private Long id;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;
    private Long productId;
    private Long orderId;
    private String productName;
    private String productImage;
    private Long userId;

}
