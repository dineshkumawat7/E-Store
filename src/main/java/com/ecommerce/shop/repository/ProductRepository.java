package com.ecommerce.shop.repository;

import com.ecommerce.shop.entity.Category;
import com.ecommerce.shop.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByCode(String code);

    boolean existsByCode(String code);

    List<Product> findByNameContaining(String keywords);

    List<Product> findByCategory(Category category);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    List<Product> findTop10ByOrderByCreatedAtDesc();

    List<Product> findByFeaturedTrue();

}
