
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.service.QRCodeGeneratorService;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * <p>
 * Defines business Login for generating QR code
 * </p>
 *
 * @author ashish
 * @since 11/09/2020 18:00
 *
 */
@Service
@Transactional
public class QRCodeGeneratorServiceImpl implements QRCodeGeneratorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] generateQRCode(String data, Integer width, Integer height, String[] text) {

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            //        If text is needed to display
            if (text != null) {
                int totalTextLineToadd = text.length;
                InputStream in = new ByteArrayInputStream(pngData);
                BufferedImage image = ImageIO.read(in);

//              Create New Image with bottom padding
                BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight() + 25 * totalTextLineToadd, BufferedImage.TYPE_INT_ARGB);
                Graphics g = outputImage.getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                g.setFont(new Font("Arial Black", Font.BOLD, 12));
                Color textColor = Color.BLACK;
                g.setColor(textColor);
                FontMetrics fm = g.getFontMetrics();
                int startingYposition = height + 5;
                for(String displayText : text) {
                    g.drawString(displayText, (outputImage.getWidth() / 2)   - (fm.stringWidth(displayText) / 2), startingYposition);
                    startingYposition += 20;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(outputImage, "PNG", baos);
                baos.flush();
                pngData = baos.toByteArray();
                baos.close();
            }

            return pngData;
        } catch (WriterException | IOException ex) {
            throw new ImtechoUserException(ex.getMessage(), 0);
        }
    }
}
