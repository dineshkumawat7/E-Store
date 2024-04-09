package com.ecommerce.shop.services.admin.profile;

import com.ecommerce.shop.payload.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminProfileService {
    void updateProfile(Long userId, UserDto userDto, MultipartFile file);
}
