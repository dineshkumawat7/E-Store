package com.ecommerce.shop.controller.user;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.image.CloudinaryImageStoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class profileController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryImageStoreService cloudinaryImageStoreService;

    @PostMapping("/update")
    public ResponseEntity<?> updateUserDetails(
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "firstname", required = true) String firstname,
            @RequestParam(value = "lastname", required = true) String lastname,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "phone", required = true) String phone,
            Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User currentUser = this.userRepository.findByEmail(username).orElseThrow();
            currentUser.setFirstname(firstname);
            currentUser.setLastname(lastname);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);

            if (file != null) {
                String fileName = file.getOriginalFilename();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                // if (!(fileExtension.equals("jpeg")) && !(fileExtension.equals("jpg"))
                // && !(fileExtension.equals("png"))) {
                // response.put("status", "error");
                // response.put("message", "Only PNG, JPEG, or JPG files are allowed.");
                // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                // }
                // String randomID = UUID.randomUUID().toString();
                // String randomFileName =
                // randomID.concat(fileName.substring(fileName.lastIndexOf(".")));

                this.cloudinaryImageStoreService.uploadProfileImage(file, currentUser);
                response.put("status", "success");
                response.put("message", "User details updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String image = currentUser.getImageUrl();
                currentUser.setImageUrl(image);
                this.userRepository.save(currentUser);
                response.put("status", "success");
                response.put("message", "User details updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User currentUser = this.userRepository.findByEmail(username).orElseThrow();
            boolean update = this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword());
            if (update) {
                currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
                this.userRepository.save(currentUser);
                response.put("status", "success");
                response.put("message", "Your password is updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "*Your old password is incorrect.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
