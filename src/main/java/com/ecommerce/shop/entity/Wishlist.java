package com.ecommerce.shop.entity;

import com.ecommerce.shop.payload.WishlistDto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "wishlists")
@Data
@Getter
@Setter
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public WishlistDto getWishlistDto() {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setId(id);
        wishlistDto.setPrice(product.getPrice());
        wishlistDto.setProductId(product.getId());
        wishlistDto.setProductName(product.getName());
        wishlistDto.setProductImage(product.getImageUrl());
        wishlistDto.setUserId(user.getId());
        return wishlistDto;
    }
}
