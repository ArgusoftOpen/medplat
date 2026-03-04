package com.argusoft.sewa.android.app.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author alpeshkyada
 */
public final class Encryptor {

    private static final String TAG = "Encryptor";

    private Encryptor() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * @param plaintext Text to encrypt
     * @return hashValue
     */
    public static synchronized String encrypt(String plaintext) {
        if (plaintext != null) {
            String algorithm = "SHA";
            String encoding = "UTF-8";
            MessageDigest msgDigest;
            String hashValue = null;
            try {
                msgDigest = MessageDigest.getInstance(algorithm);
                msgDigest.update(plaintext.getBytes(encoding));
                byte[] rawByte = msgDigest.digest();
                hashValue = Base64.encodeToString(rawByte, Base64.DEFAULT);

            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "No Such Algorithm Exists", e);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "The Encoding Is Not Supported", e);
            }
            return hashValue;
        } else {
            return null;
        }
    }

}