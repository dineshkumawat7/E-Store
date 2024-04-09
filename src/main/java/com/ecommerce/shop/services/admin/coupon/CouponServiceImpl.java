package com.ecommerce.shop.services.admin.coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.shop.entity.Coupon;
import com.ecommerce.shop.payload.CouponDto;
import com.ecommerce.shop.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void addCoupon(CouponDto couponDto) {
        Coupon coupon = new Coupon();
        coupon.setName(couponDto.getName().trim());
        coupon.setCode(couponDto.getCode().trim());
        coupon.setDiscount(couponDto.getDiscount());
        coupon.setExpirationDate(couponDto.getExpirationDate());
        this.couponRepository.save(coupon);
    }

    @SuppressWarnings("null")
    @Override
    public CouponDto getCouponDto(Long id) {
        Optional<Coupon> optionalCoupon = this.couponRepository.findById(id);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            CouponDto couponDto = new CouponDto();
            couponDto.setId(coupon.getId());
            couponDto.setName(coupon.getName());
            couponDto.setCode(coupon.getCode());
            couponDto.setDiscount(coupon.getDiscount());
            couponDto.setExpirationDate(coupon.getExpirationDate());
            return couponDto;
        }
        return null;
    }

    @SuppressWarnings("null")
    @Override
    public boolean existsById(Long id) {
        return couponRepository.existsById(id);
    }

    @Override
    public List<CouponDto> getAllCoupon() {
        List<Coupon> coupons = this.couponRepository.findAll();
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

    @SuppressWarnings("null")
    @Override
    public void updateCoupon(Long id, CouponDto couponDto) {
        Optional<Coupon> optionalCoupon = this.couponRepository.findById(id);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            coupon.setName(couponDto.getName());
            coupon.setCode(couponDto.getCode());
            coupon.setDiscount(couponDto.getDiscount());
            coupon.setExpirationDate(couponDto.getExpirationDate());
            this.couponRepository.save(coupon);
        }
    }

    @SuppressWarnings("null")
    @Override
    public void deleteCoupon(Long id) {
        Optional<Coupon> optionalCoupon = this.couponRepository.findById(id);
        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            this.couponRepository.delete(coupon);
        }
    }

    @Override
    public Page<CouponDto> getPaginatedCoupon(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Coupon> coupons = this.couponRepository.findAll(pageable);
        return coupons.map(coupon -> convertToCouponDto(coupon));
    }

    private CouponDto convertToCouponDto(Coupon coupon) {
        CouponDto couponDto = new CouponDto();
        couponDto.setId(coupon.getId());
        couponDto.setName(coupon.getName());
        couponDto.setCode(coupon.getCode());
        couponDto.setDiscount(coupon.getDiscount());
        couponDto.setExpirationDate(coupon.getExpirationDate());
        return couponDto;
    }

    @Override
    public Coupon findByCode(String code) {
        return this.couponRepository.findByCode(code);
    }

    @Override
    public boolean isCouponCodeDuplicate(String code) {
        return couponRepository.existsByCode(code);
    }

}
