package com.example.itview_spring.Util;

import java.security.SecureRandom;

public class AuthCodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        int number = random.nextInt(1_000_000); // 0 ~ 999999
        return String.format("%06d", number);   // 6자리, 앞에 0 채움
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(generateCode());
        }
    }
}
