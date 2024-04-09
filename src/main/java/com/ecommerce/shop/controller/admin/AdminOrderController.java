package com.ecommerce.shop.controller.admin;

import com.ecommerce.shop.services.admin.order.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminOrderController {
    @Autowired
    private AdminOrderService orderService;
    public ResponseEntity<?> getAllPlacedOrders(){
        Map<String, Object> response = new LinkedHashMap<>();
        try{
            this.orderService.getplacedOrders();
            return null;
        }catch (Exception exception){
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
