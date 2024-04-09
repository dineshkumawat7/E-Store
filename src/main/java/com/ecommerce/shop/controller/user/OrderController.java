package com.ecommerce.shop.controller.user;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.payload.OrderAddressDto;
import com.ecommerce.shop.payload.OrderDto;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.services.user.order.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderService orderService;

    @PostMapping("/addOrderAddress")
    public ResponseEntity<?> checkoutOrder(Principal principal, @ModelAttribute OrderAddressDto orderAddressDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        System.out.println(orderAddressDto);
        try {
            String username = principal.getName();
            Optional<User> user = this.userRepository.findByEmail(username);
            if (user.isPresent()) {
                OrderAddressDto addressDto = this.orderService.checkout(user.get().getId(),
                        orderAddressDto);
                response.put("status", "success");
                response.put("message", "Delivery address saved successfully.");
                response.put("data", addressDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllOrderAddress")
    public ResponseEntity<?> getAllOrderAddressByUser(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                List<OrderAddressDto> orderAddressDtos = this.orderService.getAllOrderAddressByUser(user.getId());
                if (!orderAddressDtos.isEmpty()) {
                    response.put("status", "success");
                    response.put("data", orderAddressDtos);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("status", "success");
                    response.put("message", "No any delivery address found.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOrderAddress/{id}")
    public ResponseEntity<?> getOrderAddressByUser(@PathVariable("id") long orderAddressId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                OrderAddressDto orderAddressDto = this.orderService.getOrderAddressByUser(orderAddressId);
                if (orderAddressDto != null) {
                    response.put("status", "success");
                    response.put("data", orderAddressDto);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.put("status", "success");
                    response.put("message", "No any delivery address found.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteOrderAddress/{id}")
    public ResponseEntity<?> deleteOrderAddressById(@PathVariable("id") long orderAddressId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                this.orderService.deleteOrderAddressByIdAndUserId(orderAddressId, user.getId());
                response.put("status", "success");
                response.put("message", "Address deleted successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateOrderAddress/{id}")
    public ResponseEntity<?> updateOrderAddressByIdAndUser(@PathVariable("id") long orderAddressId,
            @ModelAttribute OrderAddressDto orderAddressDto, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                this.orderService.updateOrderAddressByIdAndUser(orderAddressId, user.getId(), orderAddressDto);
                response.put("status", "success");
                response.put("message", "Order address updated successsfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/selectOrderAddress")
    public ResponseEntity<?> selectOrderAddress(@RequestParam("addressId") long orderAddressId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                this.orderService.selectOrderAddress(orderAddressId, user.getId());
                response.put("status", "success");
                response.put("message", "Order address selected successsfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/orderPayment")
    public ResponseEntity<?> orderPayment(@RequestParam("paymentMode") String paymentMode, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username).orElseThrow();
            if (user != null) {
                this.orderService.addPaymentInOrder(user.getId(), paymentMode);
                response.put("status", "success");
                response.put("message", "Payment added in order successsfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/placedOrder")
    public ResponseEntity<?> placeOrder(Principal principal, @RequestParam("orderId") Long orderId) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            Optional<User> user = this.userRepository.findByEmail(username);
            if (user.isPresent()) {
                this.orderService.placeOrder(user.get().getId(), orderId);
                this.orderService.createOrder(user.get());
                response.put("status", "success");
                response.put("message", "Your order request received successfully.Check your order status.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOrderByUser")
    public ResponseEntity<?> getCurrentOrder(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            Optional<User> user = this.userRepository.findByEmail(username);
            if (user.isPresent()) {
                OrderDto orderDto = this.orderService.getCurrentOrderByUser(user.get().getId());
                response.put("status", "success");
                response.put("data", orderDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllPlacedOrders")
    public ResponseEntity<?> getAllPlacedOrders(Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            Optional<User> user = this.userRepository.findByEmail(username);
            if (user.isPresent()) {
                List<OrderDto> orderDtoList = this.orderService.getAllPlacedOrders(user.get().getId());
                response.put("status", "success");
                response.put("totalOrders", orderDtoList.size());
                response.put("data", orderDtoList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trackOrder")
    public ResponseEntity<?> getOrderStatus(@RequestParam("trackingId") String trackingId, Principal principal) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String username = principal.getName();
            Optional<User> user = this.userRepository.findByEmail(username);
            if (user.isPresent()) {
                OrderDto order = this.orderService.getOrderByTrackingId(trackingId);
                response.put("status", "success");
                response.put("data", order);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Session timeout.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            System.out.println(exception);
            response.clear();
            response.put("status", "error");
            response.put("message", "Something wrong on server, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
