package com.argusoft.medplat.common.util;

import java.security.SecureRandom;

public class LoginAESEncryptionKeyManager {

    private static String key;
    private static String initVector;

    public static void generateAndSetKey() {
        try {
            key = generateRandomString(16);
            initVector = generateRandomString(16);
            LoginAESEncryptionUtil.initialize(key, initVector);
        } catch (Exception ignored) {}
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomStringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            randomStringBuilder.append(characters.charAt(randomIndex));
        }

        return randomStringBuilder.toString();
    }

    public static String getKey() {
        return key;
    }

    public static String getInitVector() {
        return initVector;
    }
}
