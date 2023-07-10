package com.argusoft.medplat.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An util class to encrypt data
 * @author harshit
 * @since 31/08/2020 4:30
 */
public class AESEncryption {

    public static final String ALGORITHM = "AES";  //Do not change. GenerateKeys function might throw NoSuchAlgorithmException.
    public static final String ENC_KEY_ALGORITHM = "AES/CBC/PKCS5PADDING"; //Do not change. As we have existing data encrypted using this algorithm we can't change it
    private static AESEncryption aesEncryptionInstance = null;
    private static IvParameterSpec iv;
    private static SecretKeySpec skeySpec;

    /**
     * Returns a instance of class
     * @return A instance of AESEncryption
     */
    public static AESEncryption getInstance() {
        synchronized (AESEncryption.class) {
            if (aesEncryptionInstance == null) {
                aesEncryptionInstance = new AESEncryption();
            }
        }
        return aesEncryptionInstance;
    }

    /**
     * Generates secret key
     * @param key A key value
     * @param initVector Specification of cryptographic parameters
     */
    public static void init(String key, String initVector) {
        try {
            iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        } catch (Exception ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Encrypts given value
     * @param value A value to encrypt
     * @return An encrypted value
     */
    public String encrypt(
            //            String key, String initVector,
            String value) {
        try {

            Cipher cipher = Cipher.getInstance(ENC_KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.INFO, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Decrypts given encrypted value
     * @param encrypted A value to decrypt
     * @return A decrypted value
     */
    public String decrypt(
            String encrypted) {
        try {

            Cipher cipher = Cipher.getInstance(ENC_KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            Logger.getLogger(AESEncryption.class.getName()).log(Level.INFO, ex.getMessage(), ex);
        }

        return null;
    }

}
