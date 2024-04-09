package com.ecommerce.shop.entity;

import com.ecommerce.shop.payload.CouponDto;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 45, unique = true)
    private String code;
    @Column(nullable = false)
    private Integer discount;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date expirationDate;

    public CouponDto getCouponDto() {
        CouponDto couponDto = new CouponDto();
        couponDto.setId(id);
        couponDto.setName(name);
        couponDto.setCode(code);
        couponDto.setDiscount(discount);
        couponDto.setExpirationDate(expirationDate);
        return couponDto;
    }
}
