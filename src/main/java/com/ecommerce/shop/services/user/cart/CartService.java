package com.ecommerce.shop.services.user.cart;

import com.ecommerce.shop.payload.AddProductInCartDto;
import com.ecommerce.shop.payload.CartItemsDto;

import java.util.List;

public interface CartService {
    CartItemsDto addProductToCart(AddProductInCartDto addProductInCartDto) throws Exception;

    CartItemsDto getCartIdAndUserId(Long cartId, Long userId);

    CartItemsDto cartItemAlreadyExists(AddProductInCartDto addProductInCartDto);

    List<CartItemsDto> getCartItemsByUserId(Long userId);

    void removeItemFromCartById(AddProductInCartDto addProductInCartDto) throws Exception;

    void removeCartItem(Long cartItemId, Long userId);

    CartItemsDto updateCartItem(Long cartItemId, Long userId, Integer quantity);

    void increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    void decreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    long getTotalCartItems(Long userId);

}
