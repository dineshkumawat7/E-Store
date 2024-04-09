package com.ecommerce.shop.services.image;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.ecommerce.shop.entity.Product;
import com.ecommerce.shop.entity.User;
import com.ecommerce.shop.repository.ProductRepository;
import com.ecommerce.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStoreServiceImpl implements CloudinaryImageStoreService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Map uploadProductImage(MultipartFile file, Product product) {
        try {
            Map uploadResult = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            String imageUrl = (String) uploadResult.get("url");
            product.setImageUrl(imageUrl);
            this.productRepository.save(product);
            return uploadResult;
        } catch (IOException e) {
            throw new RuntimeException("image uploading error.");
        }
    }

    @Override
    public Map uploadProfileImage(MultipartFile file, User user) {
        try {
            Map uploadResult = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            String imageUrl = (String) uploadResult.get("url");
            user.setImageUrl(imageUrl);
            this.userRepository.save(user);
            return uploadResult;
        } catch (IOException e) {
            throw new RuntimeException("image uploading error.");
        }
    }

}
