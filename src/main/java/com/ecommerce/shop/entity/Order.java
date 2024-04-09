package com.ecommerce.shop.entity;

import com.ecommerce.shop.enums.OrderStatus;
import com.ecommerce.shop.payload.OrderDto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column
    private LocalDateTime createdAt;
    @Column
    private Integer amount;
    @Column
    private String payment;
    @Column
    private OrderStatus orderStatus;
    @Column
    private Integer totalAmount;
    @Column
    private Integer discount;
    @Column
    private String trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private OrderAddress orderAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Coupon coupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<CartItems> cartItems;

    public OrderDto getOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setDescription(description);
        orderDto.setTrackingId(trackingId);
        orderDto.setPayment(payment);
        orderDto.setDiscount(discount);
        orderDto.setAmount(amount);
        orderDto.setCreatedAt(createdAt);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setUserName(user.getFirstname());
        if (coupon != null) {
            orderDto.setCouponName(coupon.getName());
        }
        if (orderDto != null) {
            orderDto.setAddressId(orderAddress.getId());
        }
        return orderDto;
    }

}
