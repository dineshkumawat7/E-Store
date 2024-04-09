package com.ecommerce.shop.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ecommerce.shop.payload.OrderAddressDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_order_address")
@Data
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fullname;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String pincode;
    @Column
    private String state;
    @Column
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public OrderAddressDto getOrderAddressDto() {
        OrderAddressDto orderAddressDto = new OrderAddressDto();
        orderAddressDto.setId(id);
        orderAddressDto.setFullname(fullname);
        orderAddressDto.setEmail(email);
        orderAddressDto.setPhone(phone);
        orderAddressDto.setCity(city);
        orderAddressDto.setPincode(pincode);
        orderAddressDto.setStreet(street);
        orderAddressDto.setState(state);
        orderAddressDto.setCountry(country);
        orderAddressDto.setUserId(user.getId());
        return orderAddressDto;
    }
}
