package com.ecommerce.shop.payload;

import lombok.Data;

@Data
public class ContactDto {

    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;

}
