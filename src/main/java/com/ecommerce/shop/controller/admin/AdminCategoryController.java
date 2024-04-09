package com.ecommerce.shop.controller.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.entity.Category;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.services.admin.category.CategoryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category/add")
    public ResponseEntity<?> createNewCategory(@Validated @RequestBody CategoryDto categoryDto,
            BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> fieldErrors = new HashMap<>();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    fieldErrors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(fieldErrors);
            }

            if (categoryService.existsCategoryByName(categoryDto.getName())) {
                response.put("status", "error");
                response.put("message", "Duplicate category name not allowed.");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            this.categoryService.addCategory(categoryDto);
            response.clear();
            response.put("status", "success");
            response.put("message", "New category successfully added.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Integer id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            CategoryDto categoryDto = categoryService.getCategoryById(id);
            if (categoryDto != null) {
                response.put("status", "success");
                response.put("data", categoryDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/all")
    public ResponseEntity<?> getAllCategories() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<CategoryDto> categoryDtos = this.categoryService.getAllCategories();
            if (!categoryDtos.isEmpty()) {
                response.put("status", "success");
                response.put("data", categoryDtos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "success");
                response.put("message", "There is no data available");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getPagiableCategory(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Page<CategoryDto> categories = this.categoryService.getPaginatedCategories(page, size);
            if (!categories.isEmpty()) {
                response.put("status", "success");
                response.put("data", categories);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "success");
                response.put("data", categories);
                response.put("message", "There is no data available.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            boolean isExistsCategory = categoryService.existsById(id);
            if (isExistsCategory) {
                Category category = categoryService.findById(id);
                categoryService.deleteCategory(category);
                response.put("status", "success");
                response.put("message", "Category is deleted successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/category/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Integer id,
            @Validated @RequestBody CategoryDto categoryDto, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> fieldErrors = new HashMap<>();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    fieldErrors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(fieldErrors);
            }
            boolean isExistsCategory = categoryService.existsById(id);
            if (isExistsCategory) {
                categoryService.updateCategory(id, categoryDto);
                response.put("status", "success");
                response.put("message", "Category is updated successfully.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                categoryService.updateCategory(id, categoryDto);
                response.put("status", "error");
                response.put("message", "Data is not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
