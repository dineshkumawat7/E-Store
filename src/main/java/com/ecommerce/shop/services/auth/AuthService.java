package com.ecommerce.shop.services.auth;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.UserDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    User createUser(UserDto userDto);

    boolean hasUserWithEmail(String email);

    List<UserDto> findAllUsers();

    void uploadProfileImage(Long userId, MultipartFile file);

    UserDto getCurrentUserDetails(String username);

}
