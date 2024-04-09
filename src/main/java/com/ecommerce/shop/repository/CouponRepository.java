package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsById(long id);

    boolean existsByCode(String code);

    Coupon findByCode(String code);

    List<Coupon> findByExpirationDateAfter(Date currentDate);

    List<Coupon> findByExpirationDateBefore(Date currentDate);
}
