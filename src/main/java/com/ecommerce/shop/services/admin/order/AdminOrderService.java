package com.ecommerce.shop.services.admin.order;

import com.ecommerce.shop.payload.OrderDto;

import java.util.List;

public interface AdminOrderService {
    List<OrderDto> getplacedOrders();
}
