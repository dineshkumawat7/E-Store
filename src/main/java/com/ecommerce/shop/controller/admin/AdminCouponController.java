package com.ecommerce.shop.controller.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.payload.CouponDto;
import com.ecommerce.shop.services.admin.coupon.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupon/add")
    public ResponseEntity<?> addNewCoupon(@Validated @RequestBody CouponDto couponDto, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> fieldErrors = new HashMap<>();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    fieldErrors.put(error.getField(), error.getDefaultMessage());
                }
                return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
            }

            boolean isDuplicateCode = this.couponService.isCouponCodeDuplicate(couponDto.getCode());
            if (isDuplicateCode) {
                response.clear();
                response.put("status", "error");
                response.put("message", "Duplicate coupon code not allowed.");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                this.couponService.addCoupon(couponDto);
                response.clear();
                response.put("status", "success");
                response.put("message", "New coupon added successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/coupon/get/{id}")
    public ResponseEntity<?> getCoupon(@PathVariable("id") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (this.couponService.existsById(id)) {
                CouponDto couponDto = this.couponService.getCouponDto(id);
                response.clear();
                response.put("status", "success");
                response.put("data", couponDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/coupon/all")
    public ResponseEntity<?> getAllCoupon() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<CouponDto> couponDtos = this.couponService.getAllCoupon();
            if (couponDtos != null) {
                response.put("status", "success");
                response.put("data", couponDtos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "success");
                response.put("data", couponDtos);
                response.put("data", "There is no data available.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/coupon/update/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable("id") Long id, @Validated @RequestBody CouponDto couponDto,
            BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (this.couponService.existsById(id)) {
                if (bindingResult.hasErrors()) {
                    Map<String, String> fieldErrors = new HashMap<>();
                    List<FieldError> errors = bindingResult.getFieldErrors();
                    for (FieldError error : errors) {
                        fieldErrors.put(error.getField(), error.getDefaultMessage());
                    }
                    return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
                }
                this.couponService.updateCoupon(id, couponDto);
                response.put("status", "success");
                response.put("message", "Coupon is updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/coupon/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("id") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (this.couponService.existsById(id)) {
                this.couponService.deleteCoupon(id);
                response.put("status", "success");
                response.put("message", "Coupon is deleted successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/coupons")
    public ResponseEntity<?> getPaginatedCoupon(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Page<CouponDto> coupons = this.couponService.getPaginatedCoupon(page, size);
            response.clear();
            response.put("status", "success");
            response.put("data", coupons);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
