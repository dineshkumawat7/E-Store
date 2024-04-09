package com.ecommerce.shop.controller.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping("totalProductCount")
    public ResponseEntity<?> allProductsCount() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Long totalProductCount = this.productRepository.count();
            response.put("data", totalProductCount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("totalUserCount")
    public ResponseEntity<?> allUserCount() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Long totalUserCount = this.userRepository.count();
            response.put("data", totalUserCount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("totalSubscribeCount")
    public ResponseEntity<?> allSubscribeCount() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Long totalSubscribeCount = this.subscribeRepository.count();
            response.put("data", totalSubscribeCount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("totalCompleteOrderCount")
    public ResponseEntity<?> allCompleteOrderCount() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<Order> orders = this.orderRepository.findByOrderStatus(OrderStatus.Delivered);
            response.put("data", orders.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
