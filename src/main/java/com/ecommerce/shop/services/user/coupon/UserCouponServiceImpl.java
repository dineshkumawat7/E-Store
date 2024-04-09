package com.ecommerce.shop.services.user.coupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecommerce.shop.payload.CouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.shop.entity.Coupon;
import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.repository.CouponRepository;
import com.ecommerce.shop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<CouponDto> getAllAvailableCoupon() {
        Date currentDate = new Date();
        List<Coupon> coupons = this.couponRepository.findByExpirationDateAfter(currentDate);
        List<CouponDto> couponDtos = new ArrayList<>();
        for (Coupon coupon : coupons) {
            CouponDto couponDto = new CouponDto();
            couponDto.setId(coupon.getId());
            couponDto.setName(coupon.getName());
            couponDto.setCode(coupon.getCode());
            couponDto.setDiscount(coupon.getDiscount());
            couponDto.setExpirationDate(coupon.getExpirationDate());
            couponDtos.add(couponDto);
        }
        return couponDtos;
    }

    @Override
    public List<CouponDto> getAllExpiredCoupon(){
        Date currentDate = new Date();
        List<Coupon> coupons = this.couponRepository.findByExpirationDateBefore(currentDate);
        List<CouponDto> couponDtos = new ArrayList<>();
        for (Coupon coupon : coupons) {
            CouponDto couponDto = new CouponDto();
            couponDto.setId(coupon.getId());
            couponDto.setName(coupon.getName());
            couponDto.setCode(coupon.getCode());
            couponDto.setDiscount(coupon.getDiscount());
            couponDto.setExpirationDate(coupon.getExpirationDate());
            couponDtos.add(couponDto);
        }
        return couponDtos;
    }
    @Override
    public void applyCoupon(Long userId, String code) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        Coupon coupon = this.couponRepository.findByCode(code);

        Integer discountAmount = ((coupon.getDiscount() / 100) *
                activeOrder.getTotalAmount());
        Integer netAmount = activeOrder.getTotalAmount() - discountAmount;
        activeOrder.setAmount(netAmount);
        activeOrder.setDiscount(discountAmount);
        activeOrder.setCoupon(coupon);
        this.orderRepository.save(activeOrder);
    }

    @Override
    public boolean isCouponExpired(String code) {
        Coupon coupon = this.couponRepository.findByCode(code);
        Date currentDate = new Date();
        Date expiredDate = coupon.getExpirationDate();
        if (expiredDate != null && currentDate.after(expiredDate)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Coupon isCouponValid(String code) {
        Coupon coupon = this.couponRepository.findByCode(code);
        if (coupon == null) {
            return null;
        } else {
            return coupon;
        }
    }

}
