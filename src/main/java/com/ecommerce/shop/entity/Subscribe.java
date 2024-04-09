package com.ecommerce.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "subscribe")
@Data
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
}
