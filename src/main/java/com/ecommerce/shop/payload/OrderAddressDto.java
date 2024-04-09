package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class OrderAddressDto {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String street;
    private String city;
    private String pincode;
    private String state;
    private String country;
    private Long userId;
}
