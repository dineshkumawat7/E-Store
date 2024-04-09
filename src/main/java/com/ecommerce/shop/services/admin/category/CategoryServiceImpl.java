package com.ecommerce.shop.services.admin.category;

import com.ecommerce.shop.entity.Category;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName().trim());
        category.setDescription(categoryDto.getDescription().trim());
        this.categoryRepository.save(category);
    }

    @Override
    public Page<CategoryDto> getPaginatedCategories(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = this.categoryRepository.findAll(pageable);
        return categories.map(category -> convertToCategoryDto(category));
    }

    private CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        @SuppressWarnings("null")
        Optional<Category> optionalCategory = this.categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            return categoryDto;
        }
        return null;
    }

    @Override
    public boolean existsCategoryByName(String name) {
        return this.categoryRepository.existsByName(name);
    }

    @Override
    @Transactional
    public void updateCategory(Integer id, CategoryDto categoryDto) {
        @SuppressWarnings("null")
        Optional<Category> optionalCategory = this.categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryDto.getName().trim());
            category.setDescription(categoryDto.getDescription());
            this.categoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Category category) {
        if (category != null) {
            this.categoryRepository.delete(category);
        }
    }

    @Override
    public Category findById(Integer id) {
        if (id != null) {
            return categoryRepository.findById(id).orElseThrow();
        } else {
            return null;
        }
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtosList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setDescription(category.getDescription());
            categoryDtosList.add(categoryDto);
        }
        return categoryDtosList;
    }

    @Override
    public boolean existsById(Integer id) {
        return categoryRepository.existsById(id);
    }

}
