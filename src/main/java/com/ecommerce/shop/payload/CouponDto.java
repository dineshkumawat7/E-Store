package com.ecommerce.shop.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CouponDto {
    private Long id;
    @NotEmpty(message = "*Please enter coupon name.")
    @NotBlank(message = "*Please enter coupon name.")
    private String name;

    @NotEmpty(message = "*Please enter coupon code.")
    @NotBlank(message = "*Please enter coupon code.")
    private String code;

    @Min(value = 0, message = "Discount(%) must between 0 to 100.")
    @Max(value = 100, message = "Discount(%) must between 0 to 100.")
    @NotNull(message = "*Please enter discount(%).")
    private Integer discount;
    @NotNull(message = "*Please enter expire date.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date expirationDate;

}
