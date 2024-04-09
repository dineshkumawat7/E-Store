package com.ecommerce.shop.services.user.wishlist;

import java.util.List;

import com.ecommerce.shop.payload.AddProductInWishlistDto;
import com.ecommerce.shop.payload.WishlistDto;

public interface WishlistService {
    WishlistDto addProductToWishlist(AddProductInWishlistDto addProductInWishlistDto);

    boolean existsProductAlready(Long userId, Long productId);

    List<WishlistDto> getWishlistByUserId(Long userId);

    void removeWishlistItem(Long userId, Long productId);
}
