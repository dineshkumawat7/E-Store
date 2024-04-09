package com.ecommerce.shop.services.admin.product;

import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.payload.ProductDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    void addProduct(ProductDto productDto, MultipartFile file);

    ProductDto getProductDtoById(Long id);

    List<ProductDto> getProductByCategory(String categoryName);

    Product getProductByCode(String code);

    Page<ProductDto> getPaginatedProduct(Integer page, Integer size);

    void deleteProduct(Product product);

    Product getProductById(Long id);

    void updateProduct(Long id, ProductDto productDto, MultipartFile file);

    boolean existsByCode(String code);

    List<ProductDto> getRecentProducts();

    List<ProductDto> getFeaturedProducts();

    Page<ProductDto> getProductByCategoryId(Long categoryId, Integer page, Integer size);
}
