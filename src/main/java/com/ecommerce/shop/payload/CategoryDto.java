package com.ecommerce.shop.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    private Integer id;
    @NotEmpty(message = "*Please enter category name.")
    @NotBlank(message = "*Please enter category name.")
    private String name;
    @Size(min = 0, max = 500, message = "Word limits is 500 characters.")
    private String description;

}
