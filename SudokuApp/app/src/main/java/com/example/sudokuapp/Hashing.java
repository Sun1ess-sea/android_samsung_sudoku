package com.example.sudokuapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class Hashing {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Конвертируем хэш в строку HEX
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Дополняем нулями, если нужно
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хэширования пароля", e);
        }
    }
}
