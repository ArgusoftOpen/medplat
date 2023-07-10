package com.argusoft.medplat.common.util;

import java.util.Base64;

/**
 * An util class to encode or decode data using base64
 * @author dhaval
 * @since 31/08/2020 4:30
 */
public class Base64EncoderDecoder {

    private Base64EncoderDecoder() {
            
    }

    private static final String PREFIX = "base64,";

    /**
     * Encode given value using base64
     * @param decodedValue A value to encode
     * @return An encoded value
     */
    public static String encodeToBase64(String decodedValue) {
        if (decodedValue != null && !decodedValue.isEmpty() && !decodedValue.startsWith(PREFIX)) {
            return PREFIX.concat(Base64.getEncoder().encodeToString(decodedValue.getBytes()));
        }
        return decodedValue;
    }

    /**
     * Decode given value using base64
     * @param encodedValue A value to decode
     * @return A decoded value
     */
    public static String decodeFromBase64(String encodedValue) {
        if (encodedValue != null && !encodedValue.isEmpty() && encodedValue.startsWith(PREFIX)) {
            String[] parts = encodedValue.split(",");
            return new String(Base64.getDecoder().decode(parts[1]));
        }
        return encodedValue;
    }
}
