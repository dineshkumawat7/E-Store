package com.ecommerce.shop.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.UserDto;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.EmailService;
import com.ecommerce.shop.services.auth.AuthService;
import com.ecommerce.shop.services.user.order.OrderService;
import com.ecommerce.shop.utils.OTPGenerator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService;

    @PostMapping("/registration/save")
    public ResponseEntity<?> createNewUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                for (FieldError fieldError : fieldErrors) {
                    response.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // password match
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "*Password do not match.");
                response.put("confirmPassword", "*Password do not match.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // check if email already exists
            if (this.authService.hasUserWithEmail(userDto.getEmail())) {
                bindingResult.rejectValue("email", "error.email", "*Email already exist.");
                response.put("email", "*Email already exists.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            User user = this.authService.createUser(userDto);
            if (user != null) {
                this.orderService.createOrder(user);
            }
            response.clear();
            response.put("status", "success");
            response.put("message", "New user successfully registered.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            UserDto currentUser = this.authService.getCurrentUserDetails(username);
            if (currentUser != null) {
                response.put("status", "success");
                response.put("data", currentUser);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<UserDto> userDtos = this.authService.findAllUsers();
            if (userDtos != null) {
                response.put("status", "success");
                response.put("data", userDtos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "There is no any user available.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String recipient, HttpSession httpSession) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Optional<User> user = this.userRepository.findByEmail(recipient);
            if (user.isPresent()) {
                String otp = OTPGenerator.generateOTP();
                String subject = "Sending from E-Store";
                String message = "<div style='border:1px solid #e2e2e2; padding: 20px'>"
                        + "<h3>"
                        + "Your OTP for E-Store reset password is "
                        + "<b><a href='#'>"
                        + otp
                        + "</a></b>"
                        + " and is valid for 5 mins. Please DO NOT share this OTP with anyone to keep your account safe."
                        + "</n>"
                        + "<h2><a href='#'>"
                        + "E-Store Shoping"
                        + "</a></h2>"
                        + "</h3>"
                        + "</div>";

                boolean status = this.emailService.sendEmail(subject, message, recipient);
                if (status) {
                    httpSession.setAttribute("generateOtp", otp);
                    httpSession.setAttribute("validTime", LocalDateTime.now());
                    httpSession.setAttribute("recipientEmail", recipient);
                    response.put("status", "success");
                    response.put("message", "We have send OTP to your email.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("status", "error");
                    response.put("message", "Your email is incorrect.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.put("status", "error");
                response.put("message", "This email is not registered.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam("otp") String enteredOTP,
            @RequestParam("email") String enteredEmail, HttpSession httpSession) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String generatedOTP = (String) httpSession.getAttribute("generateOtp");
            LocalDateTime generatedTime = (LocalDateTime) httpSession.getAttribute("validTime");
            String setEmail = (String) httpSession.getAttribute("recipientEmail");
            if (enteredOTP != null && generatedOTP.equals(enteredOTP) && setEmail.equals(enteredEmail)) {
                if (checkTimeLimitForOTP(generatedTime)) {
                    response.put("status", "error");
                    response.put("message", "Your OTP is expired, Please resend OTP.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                } else {
                    response.put("status", "success");
                    response.put("message", "OTP verify success.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                response.put("status", "error");
                response.put("message", "Invalid OTP.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // method for check validation time for OTP
    private boolean checkTimeLimitForOTP(LocalDateTime generatedDateTime) {
        boolean flage = false;
        long expirationTime = 5;
        LocalDateTime currentDateTime = LocalDateTime.now();
        long passedTime = ChronoUnit.MINUTES.between(generatedDateTime, currentDateTime);
        if (passedTime >= expirationTime) {
            return true;
        }
        return flage;
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email,
            @RequestParam("password") String newPassword,
            HttpSession httpSession) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String setEmail = (String) httpSession.getAttribute("recipientEmail");
            User user = this.userRepository.findByEmail(setEmail).get();
            if (setEmail.equals(email)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                this.userRepository.save(user);
                response.put("status", "success");
                response.put("message", "Your password changed successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "error");
                response.put("message", "Session expired, Please try again.");
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
