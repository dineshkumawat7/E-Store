package com.ecommerce.shop.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ProjectConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap<>();
        config.put("cloud_name", "dqv66cygs");
        config.put("api_key", "816436918347434");
        config.put("api_secret", "wKphdH7QwFxrkt2ioiN3AVNJzXw");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
