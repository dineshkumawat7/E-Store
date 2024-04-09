package com.ecommerce.shop.services.admin.profile;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.UserDto;
import com.ecommerce.shop.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {
    private AuthService authService;

    @Override
    public void updateProfile(Long userId, UserDto userDto, MultipartFile file) {
        // Optional<User> optionalUser = this.authService.getUserById(userId);
        // if (optionalUser.isPresent()) {
        // User user = optionalUser.get();
        // user.setFirstname(userDto.getFirstname());
        // user.setLastname(userDto.getLastname());
        // user.setEmail(userDto.getEmail());
        // user.setPhone(userDto.getPhone());
        // // user.setRole();
        // user.setPassword(userDto.getPassword());
        // if (!file.isEmpty()) {
        // try {
        // // user.setImage(file.getOriginalFilename());
        // File saveFile = new ClassPathResource("static/img").getFile();
        // Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
        // file.getOriginalFilename());
        // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // } catch (IOException exception) {
        // System.out.println(exception);
        // }
        // } else {
        // System.out.println("file not found.");
        // }
        // }
    }
}
