package com.ecommerce.shop.services.user.wishlist;

import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.entity.Wishlist;
import com.ecommerce.shop.payload.AddProductInWishlistDto;
import com.ecommerce.shop.payload.WishlistDto;
import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    @SuppressWarnings("null")
    @Override
    public WishlistDto addProductToWishlist(AddProductInWishlistDto addProductInWishlistDto) {
        Optional<User> optionalUser = this.userRepository.findById(addProductInWishlistDto.getUserId());
        Optional<Product> optionalProduct = this.productRepository.findById(addProductInWishlistDto.getProductId());
        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(optionalUser.get());
            wishlist.setProduct(optionalProduct.get());
            return this.wishlistRepository.save(wishlist).getWishlistDto();
        }
        return null;
    }

    @Override
    public boolean existsProductAlready(Long userId, Long productId) {
        return this.wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<WishlistDto> getWishlistByUserId(Long userId) {
        List<Wishlist> wishlists = this.wishlistRepository.findByUserId(userId);
        List<WishlistDto> wishlistDtos = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            WishlistDto wishlistDto = new WishlistDto();
            wishlistDto.setId(wishlist.getId());
            wishlistDto.setPrice(wishlist.getProduct().getPrice());
            wishlistDto.setProductId(wishlist.getProduct().getId());
            wishlistDto.setProductName(wishlist.getProduct().getName());
            wishlistDto.setProductImage(wishlist.getProduct().getImageUrl());
            wishlistDto.setUserId(wishlist.getUser().getId());
            wishlistDtos.add(wishlistDto);
        }
        return wishlistDtos;
    }

    @SuppressWarnings("null")
    @Override
    public void removeWishlistItem(Long userId, Long productId) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        Optional<Product> optionalProduct = this.productRepository.findById(productId);
        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            Optional<Wishlist> optionalWishlistItem = this.wishlistRepository.findByUserIdAndProductId(
                    userId, productId);
            if (optionalWishlistItem.isPresent()) {
                this.wishlistRepository.deleteById(optionalWishlistItem.get().getId());
            }
        }
    }

}
