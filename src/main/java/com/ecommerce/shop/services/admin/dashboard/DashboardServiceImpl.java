package com.ecommerce.shop.services.admin.dashboard;

import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.repository.CategoryRepository;
import com.ecommerce.shop.repository.OrderRepository;
import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Long getAllUsersCount() {
        return this.userRepository.count();
    }

    @Override
    public Long getAllCategoriesCount() {
        return this.categoryRepository.count();
    }

    @Override
    public Long getAllProductsCount() {
        return this.productRepository.count();
    }

    @Override
    public Long getAllOrdersCount() {
        return this.orderRepository.count();
    }
}
