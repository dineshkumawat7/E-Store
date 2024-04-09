package com.ecommerce.shop.services.admin.category;

import com.ecommerce.shop.entity.Category;
import com.ecommerce.shop.payload.CategoryDto;

import java.util.List;

import org.springframework.data.domain.Page;

public interface CategoryService {
    void addCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Integer id);

    Category findById(Integer id);

    boolean existsById(Integer id);

    boolean existsCategoryByName(String name);

    void updateCategory(Integer id, CategoryDto categoryDto);

    void deleteCategory(Category category);

    Page<CategoryDto> getPaginatedCategories(Integer page, Integer size);

    List<CategoryDto> getAllCategories();
}
