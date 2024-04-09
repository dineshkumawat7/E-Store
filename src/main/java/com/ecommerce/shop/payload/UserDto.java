package com.ecommerce.shop.payload;

import java.time.LocalDateTime;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "*Please enter your first name.")
    @NotEmpty(message = "*Please enter your first name.")
    private String firstname;

    private String lastname;

    @NotBlank(message = "*Please enter your email.")
    @Email
    @NotEmpty(message = "*Please enter your email.")
    private String email;

    @NotBlank(message = "*Please enter your phone no.")
    @NotEmpty(message = "*Please enter your phone no.")
    private String phone;

    @NotBlank(message = "*Please enter your password.")
    @NotEmpty(message = "*Please enter your password.")
    private String password;

    @NotBlank(message = "*Please enter confirm password.")
    @Transient
    @NotEmpty(message = "*Please enter confirm password.")
    private String confirmPassword;

    private String imageUrl;

    private LocalDateTime createdAt;

    @Transient
    @AssertTrue(message = "*Please click agree button.")
    private boolean agree;

}
