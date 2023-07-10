
package com.argusoft.medplat.common.service;

/**
 *
 * <p>
 *     Defines Service for generating QR code
 * </p>
 * @author ashish
 * @since 11/09/2020 18:00
 *
 */

public interface QRCodeGeneratorService {
    /**
     * Generates QR code And Saves at given file path
     * @param data A data value that need to be Stored in QR code
     * @param width A width value of generated QR code
     * @param height A height value of generated QR code
     * @param text A list of String text display
     * @return A byte Array of generated QR code
     */
    public byte[] generateQRCode(String data, Integer width, Integer height, String[] text);
}
