package com.ecommerce.shop.services.auth;

import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.entity.Role;
import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.payload.UserDto;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.RoleRepository;
import com.ecommerce.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstname(userDto.getFirstname().trim());
        user.setLastname(userDto.getLastname().trim());
        user.setEmail(userDto.getEmail().trim());
        user.setPhone(userDto.getPhone().trim());
        user.setPassword(passwordEncoder.encode(userDto.getPassword().trim()));
        user.setCreatedAt(LocalDateTime.now());
        Role role = this.roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkUserRoleExists();
        }
        user.setRoles(Arrays.asList(role));
        this.userRepository.save(user);
        return user;
    }

    private Role checkUserRoleExists() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return this.roleRepository.save(role);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @SuppressWarnings("null")
    @Override
    public void uploadProfileImage(Long userId, MultipartFile file) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!file.isEmpty()) {
                try {
                    // user.setImage(file.getOriginalFilename());
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException exception) {
                    System.out.println("ERROR : " + exception);
                }
                this.userRepository.save(user);
            } else {
                System.out.println("file not found.");
            }
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setImageUrl(user.getImageUrl());
        return userDto;
    }

    public UserDto getCurrentUserDetails(String username) {
        Optional<User> optionalUser = this.userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstname(user.getFirstname());
            userDto.setLastname(user.getLastname());
            userDto.setEmail(user.getEmail());
            userDto.setPhone(user.getPhone());
            userDto.setImageUrl(user.getImageUrl());
            userDto.setCreatedAt(user.getCreatedAt());
            return userDto;
        } else {
            return null;
        }
    }

}
