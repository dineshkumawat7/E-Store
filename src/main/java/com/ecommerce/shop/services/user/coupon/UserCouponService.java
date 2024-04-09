package com.ecommerce.shop.services.user.coupon;

import com.ecommerce.shop.entity.Coupon;
import com.ecommerce.shop.payload.CouponDto;

import java.util.List;

public interface UserCouponService {

    List<CouponDto> getAllAvailableCoupon();

    List<CouponDto> getAllExpiredCoupon();

    void applyCoupon(Long userId, String code);

    boolean isCouponExpired(String code);

    Coupon isCouponValid(String code);
}
