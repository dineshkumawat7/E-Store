package com.ecommerce.shop.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ProductDto {

    private Long id;

    private String code;

    @NotEmpty(message = "*Please enter product name.")
    @NotBlank(message = "*Please enter product name.")
    private String name;

    @NotNull(message = "*Please enter product price.")
    @Positive(message = "*Please enter valid price.")
    private Integer price;

    @NotEmpty(message = "*Please enter product color.")
    @NotBlank(message = "*Please enter product color.")
    private String color;

    @NotEmpty(message = "*field required.")
    @NotBlank(message = "*field required.")
    private String manufacturer;

    @NotEmpty(message = "*field required.")
    @NotBlank(message = "*field required.")
    private String description;

    private String imageUrl;

    @NotNull(message = "*Please choose a product image.")
    private MultipartFile image;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private boolean featured;

    @NotNull(message = "*Please select a category.")
    private Integer categoryId;

    private CategoryDto category;

}
