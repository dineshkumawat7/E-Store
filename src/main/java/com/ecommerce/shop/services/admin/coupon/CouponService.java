package com.ecommerce.shop.services.admin.coupon;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.shop.entity.Coupon;
import com.ecommerce.shop.payload.CouponDto;

public interface CouponService {

    void addCoupon(CouponDto couponDto);

    CouponDto getCouponDto(Long id);

    boolean existsById(Long id);

    List<CouponDto> getAllCoupon();

    Page<CouponDto> getPaginatedCoupon(Integer page, Integer size);

    void updateCoupon(Long id, CouponDto couponDto);

    void deleteCoupon(Long id);

    Coupon findByCode(String code);

    boolean isCouponCodeDuplicate(String code);
}
