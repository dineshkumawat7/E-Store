package com.ecommerce.shop.controller.user;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ecommerce.shop.entity.Subscribe;
import com.ecommerce.shop.services.user.subscribe.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsLetterController {
    @Autowired
    private SubscribeService subscribeService;
    @PostMapping("/newsLetter")
    public ResponseEntity<?> subscribeNewsLetter(@RequestParam("email") String email) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Subscribe subscribe = new Subscribe();
            subscribe.setEmail(email);
            Subscribe sub = this.subscribeService.subscribeNewsLetter(subscribe);
            response.put("status", "success");
            response.put("data", sub);
            response.put("message", "Thankyou for Subscribe our NewsLetter.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
