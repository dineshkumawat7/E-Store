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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.AddProductInCartDto;
import com.ecommerce.shop.payload.CartItemsDto;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.user.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addProductInCart(@RequestBody AddProductInCartDto addProductInCartDto) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            CartItemsDto cartItemsDto = this.cartService.cartItemAlreadyExists(addProductInCartDto);
            if (cartItemsDto != null) {
                response.put("status", "error");
                response.put("message", "Product already added in your cart.");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                this.cartService.addProductToCart(addProductInCartDto);
                response.put("status", "success");
                response.put("message", "Product added in cart successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCartByUserId(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                List<CartItemsDto> cartItemsDtos = this.cartService.getCartItemsByUserId(user.getId());
                response.put("status", "success");
                response.put("data", cartItemsDtos);
                response.put("totalCartItems", cartItemsDtos.size());
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
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/get")
    public ResponseEntity<?> getCartProduct(@RequestParam("cartId") Long cartId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (!(user == null)) {
                CartItemsDto cartItemsDto = this.cartService.getCartIdAndUserId(cartId, user.getId());
                response.put("status", "success");
                response.put("data", cartItemsDto);
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
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam("cartId") Long cartId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (!(user == null)) {
                this.cartService.removeCartItem(cartId, user.getId());
                response.put("status", "success");
                response.put("message", "Cart item removed successfully.");
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
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/update")
    public ResponseEntity<?> updateCartItem(@RequestParam("cartId") Long cartId,
            @RequestParam("quantity") Integer quantity, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (!(user == null)) {
                CartItemsDto cartItemsDto = this.cartService.updateCartItem(cartId, user.getId(), quantity);
                response.put("status", "success");
                response.put("data", cartItemsDto);
                response.put("message", "Cart item updated successfully.");
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
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/increaseProductQuantity")
    public ResponseEntity<?> increaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            this.cartService.increaseProductQuantity(addProductInCartDto);
            response.put("status", "success");
            response.put("message", "Product quantity increase successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decreaseProductQuantity")
    public ResponseEntity<?> decreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            this.cartService.decreaseProductQuantity(addProductInCartDto);
            response.put("status", "success");
            response.put("message", "Product quantity decrease successfully.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
