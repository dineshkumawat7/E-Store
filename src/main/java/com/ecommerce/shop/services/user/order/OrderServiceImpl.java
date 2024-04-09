package com.ecommerce.shop.services.user.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.shop.entity.CartItems;
import com.ecommerce.shop.entity.Order;
import com.ecommerce.shop.entity.OrderAddress;
import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.payload.CartItemsDto;
import com.ecommerce.shop.payload.OrderAddressDto;
import com.ecommerce.shop.payload.OrderDto;
import com.ecommerce.shop.repository.OrderAddressRepository;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.UserRepository;
import com.ecommerce.shop.utils.CustomUUIDGenerator;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderAddressRepository orderAddressRepository;

    @Transactional
    @Override
    public void createOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setAmount(0);
        order.setDiscount(0);
        order.setOrderStatus(OrderStatus.Pending);
        order.setTotalAmount(0);
        this.orderRepository.save(order);
    }

    @Override
    public OrderAddressDto checkout(Long userId, OrderAddressDto orderAddressDto) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setFullname(orderAddressDto.getFullname());
            orderAddress.setEmail(orderAddressDto.getEmail());
            orderAddress.setPhone(orderAddressDto.getPhone());
            orderAddress.setStreet(orderAddressDto.getStreet());
            orderAddress.setCity(orderAddressDto.getCity());
            orderAddress.setState(orderAddressDto.getState());
            orderAddress.setPincode(orderAddressDto.getPincode());
            orderAddress.setCountry(orderAddressDto.getCountry());
            User user = this.userRepository.findById(userId).orElseThrow();
            orderAddress.setUser(user);
            this.orderAddressRepository.save(orderAddress);

            activeOrder.setOrderAddress(orderAddress);
            this.orderRepository.save(activeOrder);
            return orderAddress.getOrderAddressDto();
        }
        return null;
    }

    @Override
    public List<OrderAddressDto> getAllOrderAddressByUser(Long userId) {
        List<OrderAddress> orderAddresses = this.orderAddressRepository.findByUserId(userId);
        List<OrderAddressDto> orderAddressDtos = new ArrayList<OrderAddressDto>();
        for (OrderAddress orderAddress : orderAddresses) {
            OrderAddressDto orderAddressDto = new OrderAddressDto();
            orderAddressDto.setId(orderAddress.getId());
            orderAddressDto.setFullname(orderAddress.getFullname());
            orderAddressDto.setEmail(orderAddress.getEmail());
            orderAddressDto.setPhone(orderAddress.getPhone());
            orderAddressDto.setStreet(orderAddress.getStreet());
            orderAddressDto.setCity(orderAddress.getCity());
            orderAddressDto.setPincode(orderAddress.getPincode());
            orderAddressDto.setState(orderAddress.getState());
            orderAddressDto.setCountry(orderAddress.getCountry());
            orderAddressDto.setUserId(orderAddress.getUser().getId());
            orderAddressDtos.add(orderAddressDto);
        }
        return orderAddressDtos;
    }

    @Override
    public OrderAddressDto getOrderAddressByUser(Long orderAddressId) {
        Optional<OrderAddress> optionalOrderAddress = this.orderAddressRepository.findById(orderAddressId);
        if (optionalOrderAddress.isPresent()) {
            OrderAddress orderAddress = optionalOrderAddress.get();
            OrderAddressDto orderAddressDto = new OrderAddressDto();
            orderAddressDto.setId(orderAddress.getId());
            orderAddressDto.setFullname(orderAddress.getFullname());
            orderAddressDto.setEmail(orderAddress.getEmail());
            orderAddressDto.setPhone(orderAddress.getPhone());
            orderAddressDto.setStreet(orderAddress.getStreet());
            orderAddressDto.setCity(orderAddress.getCity());
            orderAddressDto.setPincode(orderAddress.getPincode());
            orderAddressDto.setState(orderAddress.getState());
            orderAddressDto.setCountry(orderAddress.getCountry());
            orderAddressDto.setUserId(orderAddress.getUser().getId());
            return orderAddressDto;
        } else {
            return null;
        }
    }

    @Override
    public void selectOrderAddress(Long orderAddressId, Long userId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        if (activeOrder != null) {
            Optional<OrderAddress> orderAddress = this.orderAddressRepository.findById(orderAddressId);
            activeOrder.setOrderAddress(orderAddress.get());
            this.orderRepository.save(activeOrder);
        }
    }

    @Override
    public void updateOrderAddressByIdAndUser(Long orderAddressId, Long userId, OrderAddressDto orderAddressDto) {
        OrderAddress orderAddress = this.orderAddressRepository.findByIdAndUserId(orderAddressId, userId);
        if (orderAddress != null) {
            Optional<OrderAddress> optionalOrderAddress = this.orderAddressRepository.findById(orderAddressId);
            if (optionalOrderAddress.isPresent()) {
                OrderAddress address = optionalOrderAddress.get();
                address.setFullname(orderAddressDto.getFullname());
                address.setEmail(orderAddressDto.getEmail());
                address.setPhone(orderAddressDto.getPhone());
                address.setStreet(orderAddressDto.getStreet());
                address.setCity(orderAddressDto.getCity());
                address.setState(orderAddressDto.getState());
                address.setPincode(orderAddressDto.getPincode());
                address.setCountry(orderAddressDto.getCountry());

                this.orderAddressRepository.save(address);
            }
        }
    }

    @Override
    public void deleteOrderAddressByIdAndUserId(Long orderAddressId, Long userId) {
        OrderAddress orderAddress = this.orderAddressRepository.findByIdAndUserId(orderAddressId, userId);
        if (orderAddress != null) {
            this.orderAddressRepository.delete(orderAddress);
        }
    }

    @Override
    public void addPaymentInOrder(Long userId, String paymentMode) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        if (activeOrder != null) {
            activeOrder.setPayment(paymentMode);
            this.orderRepository.save(activeOrder);
        }
    }

    @Override
    public OrderDto placeOrder(Long userId, Long orderId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        @SuppressWarnings("null")
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            activeOrder.setCreatedAt(LocalDateTime.now());
            activeOrder.setOrderStatus(OrderStatus.Placed);
            String tracking_id = CustomUUIDGenerator.generateUUID(20);
            activeOrder.setTrackingId(tracking_id);
            this.orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    @Override
    public OrderDto getCurrentOrderByUser(Long userId) {
        Order activeOrder = this.orderRepository.findByUserIdAndOrderStatus(userId,
                OrderStatus.Pending);
        List<CartItems> cartItems = activeOrder.getCartItems();
        List<CartItemsDto> cartItemsDtosList = new ArrayList<>();
        for (CartItems cartItem : cartItems) {
            CartItemsDto item = new CartItemsDto();
            item.setId(cartItem.getId());
            item.setProductName(cartItem.getProduct().getName());
            item.setProductImage(cartItem.getProduct().getImageUrl());
            item.setOrderId(cartItem.getOrder().getId());
            item.setProductId(cartItem.getProduct().getId());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalPrice(cartItem.getTotalPrice());
            item.setUserId(cartItem.getUser().getId());
            cartItemsDtosList.add(item);
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setPayment(activeOrder.getPayment());
        orderDto.setUserName(activeOrder.getUser().getFirstname() + " " + activeOrder.getUser().getLastname());
        orderDto.setTrackingId(activeOrder.getTrackingId());
        orderDto.setCreatedAt(activeOrder.getCreatedAt());
        orderDto.setCartItems(cartItemsDtosList);
        orderDto.setDescription(activeOrder.getDescription());
        if (!(activeOrder.getOrderAddress() == null)) {
            OrderAddressDto orderAddressDto = activeOrder.getOrderAddress().getOrderAddressDto();
            orderDto.setAddressId(activeOrder.getOrderAddress().getId());
            orderDto.setOrderAddressDto(orderAddressDto);
        }
        if (activeOrder.getCoupon() != null) {
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }
        return orderDto;
    }

    @Override
    public List<OrderDto> getAllPlacedOrders(Long userId) {
        List<Order> orders = this.orderRepository.findAllByUserIdAndOrderStatus(userId, OrderStatus.Placed);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setUserName(order.getUser().getFirstname() + " " + order.getUser().getLastname());
            orderDto.setAmount(order.getAmount());
            orderDto.setDiscount(order.getDiscount());
            orderDto.setTotalAmount(order.getTotalAmount());
            orderDto.setCreatedAt(order.getCreatedAt());
            orderDto.setTrackingId(order.getTrackingId());
            orderDto.setPayment(order.getPayment());
            orderDto.setAddressId(order.getOrderAddress().getId());
            orderDto.setOrderStatus(order.getOrderStatus());
            orderDto.setDescription(order.getDescription());
            OrderAddressDto orderAddressDto = order.getOrderAddress().getOrderAddressDto();
            orderDto.setOrderAddressDto(orderAddressDto);
            List<CartItems> cartItems = order.getCartItems();
            List<CartItemsDto> cartItemsDto = new ArrayList<>();
            for (CartItems cartItem : cartItems) {
                CartItemsDto item = new CartItemsDto();
                item.setId(cartItem.getId());
                item.setProductName(cartItem.getProduct().getName());
                item.setProductImage(cartItem.getProduct().getImageUrl());
                item.setOrderId(cartItem.getOrder().getId());
                item.setProductId(cartItem.getProduct().getId());
                item.setPrice(cartItem.getPrice());
                item.setQuantity(cartItem.getQuantity());
                item.setTotalPrice(cartItem.getTotalPrice());
                item.setUserId(cartItem.getUser().getId());
                cartItemsDto.add(item);
            }
            orderDto.setCartItems(cartItemsDto);
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    @Override
    public OrderDto getOrderByTrackingId(String trackingId) {
        Order order = this.orderRepository.findByTrackingId(trackingId);
        OrderAddressDto orderAddressDto = order.getOrderAddress().getOrderAddressDto();
        List<CartItems> cartItems = order.getCartItems();
        List<CartItemsDto> cartItemsDtosList = new ArrayList<>();
        for (CartItems cartItem : cartItems) {
            CartItemsDto item = new CartItemsDto();
            item.setId(cartItem.getId());
            item.setProductName(cartItem.getProduct().getName());
            item.setProductImage(cartItem.getProduct().getImageUrl());
            item.setOrderId(cartItem.getOrder().getId());
            item.setProductId(cartItem.getProduct().getId());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setTotalPrice(cartItem.getTotalPrice());
            item.setUserId(cartItem.getUser().getId());
            cartItemsDtosList.add(item);
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(order.getAmount());
        orderDto.setId(order.getId());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setDiscount(order.getDiscount());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setPayment(order.getPayment());
        orderDto.setUserName(order.getUser().getFirstname() + " " + order.getUser().getLastname());
        orderDto.setTrackingId(order.getTrackingId());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setCartItems(cartItemsDtosList);
        orderDto.setDescription(order.getDescription());
        orderDto.setAddressId(order.getOrderAddress().getId());
        orderDto.setOrderAddressDto(orderAddressDto);
        if (order.getCoupon() != null) {
            orderDto.setCouponName(order.getCoupon().getName());
        }
        return orderDto;
    }

}
