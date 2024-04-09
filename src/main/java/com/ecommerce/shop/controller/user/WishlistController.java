package com.ecommerce.shop.controller.user;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.AddProductInWishlistDto;
import com.ecommerce.shop.payload.WishlistDto;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.user.wishlist.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addItemInWishlist(@RequestBody AddProductInWishlistDto addProductInWishlistDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (this.wishlistService.existsProductAlready(addProductInWishlistDto.getUserId(),
                    addProductInWishlistDto.getProductId())) {
                response.clear();
                response.put("status", "error");
                response.put("message", "Product already added in your wishlist.");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                WishlistDto wishlistDto = this.wishlistService.addProductToWishlist(addProductInWishlistDto);
                response.clear();
                response.put("status", "success");
                response.put("message", "Product added in wishlist successfully.");
                response.put("data", wishlistDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllItemsInWishlist(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                List<WishlistDto> wishlistDtos = this.wishlistService.getWishlistByUserId(user.getId());
                if (wishlistDtos != null) {
                    response.clear();
                    response.put("status", "success");
                    response.put("data", wishlistDtos);
                    response.put("totalWishlistItems", wishlistDtos.size());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.clear();
                    response.put("status", "success");
                    response.put("message", "There is no any items available in your wishlist.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                response.clear();
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/removeWishlistItem")
    public ResponseEntity<?> removeWishlistItem(@RequestParam("productId") Long productId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                this.wishlistService.removeWishlistItem(user.getId(), productId);
                response.clear();
                response.put("status", "success");
                response.put("message", "Wishlist item removed successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
