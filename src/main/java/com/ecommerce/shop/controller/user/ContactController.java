package com.ecommerce.shop.controller.user;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.payload.ContactDto;
import com.ecommerce.shop.services.user.contact.ContactService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping("/add")
    public ResponseEntity<?> contactMessage(@ModelAttribute() ContactDto contactDto) {
        Map<String, String> response = new LinkedHashMap<>();
        try {
            System.out.println(contactDto);
            response.put("status", "success");
            response.put("message", "Your message successfully send, we will contact you soon.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occured, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
