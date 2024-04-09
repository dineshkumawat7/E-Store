package com.ecommerce.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping(path = { "/", "/home" })
    public String home() {
        return "index";
    }

    @GetMapping("/registration")
    public String redirectRegistrationPage() {
        return "registration";
    }

    @GetMapping("/login")
    public String redirectLoginPage() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot_password";
    }


}
