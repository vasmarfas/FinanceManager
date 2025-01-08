package org.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            // Создаём экземпляр MessageDigest с алгоритмом SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Преобразуем строку пароля в байты
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Преобразуем байты в шестнадцатеричную строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Добавляем ведущий ноль, если необходимо
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка: Алгоритм SHA-256 недоступен", e);
        }
    }
}