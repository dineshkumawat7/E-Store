package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    boolean existsByName(String name);

    @SuppressWarnings("null")
    boolean existsById(Integer id);
}
