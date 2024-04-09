package com.ecommerce.shop.controller.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.services.admin.product.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/product/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewProduct(
            @Validated @RequestParam(required = false, value = "image") MultipartFile file,
            @ModelAttribute @Validated ProductDto productDto,
            BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {

            if (file.isEmpty()) {
                bindingResult.rejectValue("image", "image.empty", "Please choose a product image.");
                return ResponseEntity.badRequest().body("*Please choose a product image.");
            }
            if (bindingResult.hasErrors()) {
                Map<String, String> fieldErrors = new HashMap<>();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    fieldErrors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(fieldErrors);
            }

            this.productService.addProduct(productDto, file);
            response.clear();
            response.put("status", "success");
            response.put("message", "New product added successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            response.clear();
            System.out.println(exception);
            response.put("status", "error");
            response.put("message", "Some error occurred, Please try again");
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

    @PutMapping(value = "/product/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,
            @Validated @ModelAttribute ProductDto productDto, BindingResult bindingResult,
            @RequestParam(value = "image", required = true) MultipartFile file) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            if (file.isEmpty()) {
                bindingResult.rejectValue("image", "image.empty", "Please choose a product image.");
                return ResponseEntity.badRequest().body("*Please choose a product image.");
            }
            if (bindingResult.hasErrors()) {
                Map<String, String> fieldErrors = new HashMap<>();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    fieldErrors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest().body(fieldErrors);
            }
            Product product = productService.getProductById(id);
            if (product != null) {
                productService.updateProduct(id, productDto, file);
                response.clear();
                response.put("status", "success");
                response.put("message", "Product is updated successfully.");
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

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                productService.deleteProduct(product);
                response.clear();
                response.put("status", "success");
                response.put("message", "Product is deleted successfully.");
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

}
