package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

}
