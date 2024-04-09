package com.ecommerce.shop.controller.user;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.repository.ProductRepository;

@RestController
public class SearchController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/search/{keywords}")
    public ResponseEntity<?> searchProductByName(@PathVariable("keywords") String keywords) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<Product> products = this.productRepository.findByNameContaining(keywords);
            response.put("status", "success");
            response.put("data", products);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
