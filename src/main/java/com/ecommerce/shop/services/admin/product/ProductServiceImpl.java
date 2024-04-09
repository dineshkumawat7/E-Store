package com.ecommerce.shop.services.admin.product;

import com.ecommerce.shop.entity.Category;
import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.payload.CategoryDto;
import com.ecommerce.shop.payload.ProductDto;
import com.ecommerce.shop.repository.CategoryRepository;
import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.services.admin.category.CategoryService;
import com.ecommerce.shop.services.image.CloudinaryImageStoreService;
import com.ecommerce.shop.utils.CustomUUIDGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CloudinaryImageStoreService cloudinaryImageStoreService;

    @Override
    @Transactional
    public void addProduct(ProductDto productDto, MultipartFile file) {
        Product product = new Product();
        product.setId(productDto.getId());
        String randonCode = CustomUUIDGenerator.generateUUID(14).toUpperCase();
        product.setCode(randonCode);
        product.setName(productDto.getName().trim());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor().trim());
        product.setManufacturer(productDto.getManufacturer().trim());
        product.setDescription(productDto.getDescription().trim());
        product.setFeatured(productDto.isFeatured());
        Category category = this.categoryService.findById(productDto.getCategoryId());
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        this.cloudinaryImageStoreService.uploadProductImage(file, product);

        this.productRepository.save(product);
    }

    @Override
    public ProductDto getProductDtoById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCode(product.getCode());
            productDto.setColor(product.getColor());
            productDto.setPrice(product.getPrice());
            productDto.setManufacturer(product.getManufacturer());
            productDto.setDescription(product.getDescription());
            productDto.setImageUrl(product.getImageUrl());
            CategoryDto categoryDto = categoryService.getCategoryById(product.getCategory().getId());
            productDto.setCategory(categoryDto);
            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setUpdatedAt(product.getUpdatedAt());
            productDto.setFeatured(product.isFeatured());
            productDto.setCategoryId(categoryDto.getId());
            return productDto;
        } else {
            return null;
        }
    }

    @Override
    public Product getProductByCode(String code) {
        return this.productRepository.findByCode(code);
    }

    @Override
    public Page<ProductDto> getPaginatedProduct(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = this.productRepository.findAll(pageable);
        return products.map(product -> convertToProductDto(product));
    }

    private ProductDto convertToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCode(product.getCode());
        productDto.setColor(product.getColor());
        productDto.setPrice(product.getPrice());
        productDto.setManufacturer(product.getManufacturer());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        CategoryDto categoryDto = categoryService.getCategoryById(product.getCategory().getId());
        productDto.setCategory(categoryDto);
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());
        productDto.setFeatured(product.isFeatured());
        productDto.setCategoryId(categoryDto.getId());
        return productDto;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return product;
    }

    @Override
    public void deleteProduct(Product product) {
        if (product != null) {
            productRepository.delete(product);
        }
    }

    @Override
    public void updateProduct(Long id, ProductDto productDto, MultipartFile file) {
        Optional<Product> optionalProduct = this.productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setId(productDto.getId());
            product.setName(productDto.getName().trim());
            product.setPrice(productDto.getPrice());
            product.setColor(productDto.getColor().trim());
            product.setManufacturer(productDto.getManufacturer().trim());
            product.setDescription(productDto.getDescription().trim());
            product.setUpdatedAt(LocalDateTime.now());
            product.setFeatured(productDto.isFeatured());
            Category category = this.categoryService.findById(productDto.getCategoryId());
            product.setCategory(category);
            this.cloudinaryImageStoreService.uploadProductImage(file, product);

            this.productRepository.save(product);
        }
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public Page<ProductDto> getProductByCategoryId(Long categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = this.productRepository.findByCategoryId(categoryId, pageable);
        return products.map(product -> convertToProductDto(product));
    }

    @Override
    public List<ProductDto> getRecentProducts() {
        List<Product> recentProducts = this.productRepository.findTop10ByOrderByCreatedAtDesc();
        List<ProductDto> productDtosList = new ArrayList<ProductDto>();
        for (Product product : recentProducts) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCode(product.getCode());
            productDto.setPrice(product.getPrice());
            productDto.setColor(product.getColor());
            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setUpdatedAt(product.getUpdatedAt());
            productDto.setFeatured(product.isFeatured());
            productDto.setImageUrl(product.getImageUrl());
            productDto.setManufacturer(product.getManufacturer());
            productDto.setDescription(product.getDescription());
            productDto.setCategoryId(product.getCategory().getId());
            CategoryDto categoryDto = categoryService.getCategoryById(product.getCategory().getId());
            productDto.setCategory(categoryDto);

            productDtosList.add(productDto);
        }
        return productDtosList;
    }

    @Override
    public List<ProductDto> getFeaturedProducts() {
        List<Product> recentProducts = this.productRepository.findByFeaturedTrue();
        List<ProductDto> productDtosList = new ArrayList<ProductDto>();
        for (Product product : recentProducts) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCode(product.getCode());
            productDto.setPrice(product.getPrice());
            productDto.setColor(product.getColor());
            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setUpdatedAt(product.getUpdatedAt());
            productDto.setFeatured(product.isFeatured());
            productDto.setImageUrl(product.getImageUrl());
            productDto.setManufacturer(product.getManufacturer());
            productDto.setDescription(product.getDescription());
            productDto.setCategoryId(product.getCategory().getId());
            CategoryDto categoryDto = categoryService.getCategoryById(product.getCategory().getId());
            productDto.setCategory(categoryDto);

            productDtosList.add(productDto);
        }
        return productDtosList;
    }

    @Override
    public List<ProductDto> getProductByCategory(String categoryName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductByCategory'");
    }

}
