package com.ecommerce.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private Integer price;
    @Column
    private String color;
    @Column
    private String manufacturer;
    @Column
    private String description;
    @Column
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    private boolean featured;

    @ManyToOne
    @JsonIgnore
    @JoinTable(name = "PRODUCT_CATEGORY", joinColumns = {
            @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID") })
    private Category category;

}
