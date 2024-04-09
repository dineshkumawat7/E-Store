package com.ecommerce.shop.utils;

import java.util.Random;

public class OTPGenerator {
    // method to generate a 6-digit OTP
    public static String generateOTP() {
        int otpLength = 6;
        String numbers = "0123456789";
        StringBuilder stringBuilder = new StringBuilder(otpLength);
        Random random = new Random();
        for (int i = 0; i < otpLength; i++) {
            int code = random.nextInt(numbers.length());
            stringBuilder.append(numbers.charAt(code));
        }
        return stringBuilder.toString();
    }
}
