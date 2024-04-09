package com.ecommerce.shop.controller.user;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.services.admin.category.CategoryService;
import com.ecommerce.shop.services.admin.product.ProductService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

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

    @GetMapping("/productByCategory/{id}")
    public ResponseEntity<?> getAllCategories(@PathVariable("id") Long categoryId) {
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

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable(value = "id", required = true) Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            ProductDto productDto = productService.getProductDtoById(id);
            if (productDto != null) {
                response.clear();
                response.put("status", "success");
                response.put("data", productDto);
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

    @GetMapping("/products")
    public ResponseEntity<?> getPaginatedProducts(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Page<ProductDto> products = productService.getPaginatedProduct(page, size);
            if (!products.isEmpty()) {
                response.clear();
                response.put("status", "success");
                response.put("data", products);
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

    @GetMapping("/featuredProduct")
    public ResponseEntity<?> getFeaturedProducts() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<ProductDto> recentProducts = this.productService.getFeaturedProducts();
            if (!recentProducts.isEmpty()) {
                response.clear();
                response.put("status", "success");
                response.put("data", recentProducts);
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

    @GetMapping("/recentProduct")
    public ResponseEntity<?> getRecentProducts() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<ProductDto> recentProducts = this.productService.getRecentProducts();
            if (!recentProducts.isEmpty()) {
                response.clear();
                response.put("status", "success");
                response.put("data", recentProducts);
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

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            List<CategoryDto> categories = this.categoryService.getAllCategories();
            if (!(categories.isEmpty())) {
                response.clear();
                response.put("status", "success");
                response.put("total products", categories.size());
                response.put("data", categories);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "success");
                response.put("message", "There is no product available for this category.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productsByCategory")
    public ResponseEntity<?> getProductByCategory(@RequestParam("categoryId") Long categoryId,
            @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer size) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Page<ProductDto> products = this.productService.getProductByCategoryId(categoryId, page, size);
            if (!(products.isEmpty())) {
                response.clear();
                response.put("status", "success");
                response.put("data", products);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.clear();
                response.put("status", "success");
                response.put("message", "There is no product available for this category.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception exception) {
            response.clear();
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
