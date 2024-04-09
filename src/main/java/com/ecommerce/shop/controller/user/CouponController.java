package com.ecommerce.shop.controller.user;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ecommerce.shop.payload.CouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.user.coupon.UserCouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCouponService couponService;

    @GetMapping("/available")
    public ResponseEntity<?> getAllCoupons() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<CouponDto> couponDtos = this.couponService.getAllAvailableCoupon();
            response.put("status", "success");
            response.put("data", couponDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/expired")
    public ResponseEntity<?> getAllExpiredCoupons() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<CouponDto> couponDtos = this.couponService.getAllExpiredCoupon();
            response.put("status", "success");
            response.put("data", couponDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apply/{code}")
    public ResponseEntity<?> applyCoupon(Principal principal, @PathVariable("code") String couponCode) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Optional<User> optionalUser = this.userRepository.findByEmail(principal.getName());
            if (optionalUser.isPresent()) {
                if (this.couponService.isCouponValid(couponCode) == null) {
                    response.put("status", "error");
                    response.put("message", "Please apply valid coupon.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (this.couponService.isCouponExpired(couponCode)) {
                    response.put("status", "error");
                    response.put("message", "Coupon has expired.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                Long userId = optionalUser.get().getId();
                this.couponService.applyCoupon(userId, couponCode);
                response.put("status", "success");
                response.put("message", "Yay! Coupon applied successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("status", "error");
            response.put("message", "Something went wrong.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
