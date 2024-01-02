package com.argusoft.medplat.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginAESEncryptionUtil {

    private static IvParameterSpec iv;
    private static SecretKeySpec skeySpec;

    public static final String ENC_KEY_ALGORITHM = "AES/CBC/PKCS5PADDING";

    public static void initialize(String key, String initVector) {
        iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ENC_KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String decrypt(String encryptedValue, boolean returnNullOnError) {
        try {
            Cipher cipher = Cipher.getInstance(ENC_KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(original);
        } catch (Exception ex) {
            return returnNullOnError ? null : encryptedValue;
        }

    }


}
