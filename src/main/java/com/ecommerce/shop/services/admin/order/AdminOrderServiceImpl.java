package com.ecommerce.shop.services.admin.order;

import com.ecommerce.shop.payload.OrderDto;
import com.ecommerce.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDto> getplacedOrders() {
        return null;
    }
}
