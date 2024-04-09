package com.ecommerce.shop.controller.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminProfileController {

    // @GetMapping("/profile/{id}")
    // public ResponseEntity<?> getAdminProfile(@PathVariable("id") Long id) {
    // Map<String, Object> response = new LinkedHashMap<>();
    // try {

    // } catch (Exception exception) {
    // response.clear();
    // response.put("status", "error");
    // response.put("message", "Some error occurred, Please try again.");
    // return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }
}
