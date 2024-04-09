package com.ecommerce.shop.services.admin.dashboard;

import com.ecommerce.shop.entity.User;

import java.util.List;

public interface DashboardService {
    Long getAllUsersCount();

    Long getAllCategoriesCount();

    Long getAllProductsCount();

    Long getAllOrdersCount();
}
