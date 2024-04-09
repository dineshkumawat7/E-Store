package com.ecommerce.shop.services.image;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.entity.User;

public interface CloudinaryImageStoreService {
    public Map uploadProductImage(MultipartFile file, Product product);

    public Map uploadProfileImage(MultipartFile file, User user);
}
