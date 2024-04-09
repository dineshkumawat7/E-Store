package com.ecommerce.shop.utils;

import java.util.UUID;

public class CustomUUIDGenerator {
    public static String generateUUID(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, Math.min(uuid.length(), length));
    }
}
