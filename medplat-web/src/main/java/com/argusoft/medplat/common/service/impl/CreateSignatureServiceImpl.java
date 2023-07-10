/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.service.impl;


import com.argusoft.medplat.exception.ImtechoUserException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.examples.signature.CreateSignatureBase;
import org.apache.pdfbox.examples.signature.SigUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;



/**
 *
 * <p>
 *     Defines Business logic of generating Digital sign with create visible sign on PDF
 * </p>
 * @author ashish
 * @since 22/09/2020 18:22
 *
 */
public class CreateSignatureServiceImpl extends CreateSignatureBase {


    private boolean lateExternalSigning = false;
    private File imageFile;
    private KeyStore keystore;




    /**
     * Initialize the signature creator with a keystore (pkcs12) and pin that
     * should be used for the signature.
     *
     * @param keystore is a pkcs12 keystore.
     * @param pin is the pin for the keystore / private key
     * @throws KeyStoreException if the keystore has not been initialized (loaded)
     * @throws NoSuchAlgorithmException if the algorithm for recovering the key cannot be found
     * @throws UnrecoverableKeyException if the given password is wrong
     * @throws CertificateException if the certificate is not valid as signing time
     * @throws IOException if no certificate could be found
     */
    public CreateSignatureServiceImpl(KeyStore keystore, char[] pin) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, CertificateException {
        super(keystore, pin);
    }

    public File getImageFile()
    {
        return imageFile;
    }

    public void setImageFile(File imageFile)
    {
        this.imageFile = imageFile;
    }

    public boolean isLateExternalSigning()
    {
        return lateExternalSigning;
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    public void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }

    /**
     * Set late external signing. Enable this if you want to activate the demo code where the
     * signature is kept and added in an extra step without using PDFBox methods. This is disabled
     * by default.
     *
     * @param lateExternalSigning
     */
    public void setLateExternalSigning(boolean lateExternalSigning)
    {
        this.lateExternalSigning = lateExternalSigning;
    }


    /**
     * Sign pdf file and create new file that ends with "_signed.pdf".
     *
     * @param inputFile The source pdf document file.
     * @param signedFile The file to be signed.
     * @param humanRect rectangle from a human viewpoint (coordinates start at top left)
     * @param tsaUrl optional TSA url
     * @throws IOException
     */
    public void signPDF(File inputFile, File signedFile, Rectangle2D humanRect, String tsaUrl) throws IOException, KeyStoreException
    {
        this.signPDF(inputFile, signedFile, humanRect, tsaUrl, null);
    }

    /**
     * Sign pdf file and create new file that ends with "_signed.pdf".
     *
     * @param inputFile The source pdf document file.
     * @param signedFile The file to be signed.
     * @param humanRect rectangle from a human viewpoint (coordinates start at top left)
     * @param tsaUrl optional TSA url
     * @param signatureFieldName optional name of an existing (unsigned) signature field
     * @throws IOException
     */
    public void signPDF(File inputFile, File signedFile, Rectangle2D humanRect, String tsaUrl, String signatureFieldName) throws IOException, KeyStoreException {
        try (SignatureOptions signatureOptions = new SignatureOptions()) {

            if (inputFile == null) {
                throw new IOException("Document for signing does not exist");
            }
            setTsaUrl(tsaUrl);
            // creating output document and prepare the IO streams.
            try (FileOutputStream fos = new FileOutputStream(signedFile)) {

                PDDocument doc = PDDocument.load(inputFile);
                int accessPermissions = SigUtils.getMDPPermission(doc);
                if (accessPermissions == 1) {
                    throw new IllegalStateException("No changes to the document are permitted due to DocMDP transform parameters dictionary");
                }
                // Note that PDFBox has a bug that visual signing on certified files with permission 2
                // doesn't work properly, see PDFBOX-3699. As long as this issue is open, you may want to
                // be careful with such files.

                PDSignature signature = null;
                PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
                PDRectangle rect = null;

                // sign a PDF with an existing empty signature, as created by the CreateEmptySignatureForm example.
                if (acroForm != null) {
                    signature = findExistingSignature(acroForm, signatureFieldName);
                    if (signature != null) {
                        rect = acroForm.getField(signatureFieldName).getWidgets().get(0).getRectangle();
                    }
                }

                if (signature == null) {
                    // create signature dictionary
                    signature = new PDSignature();
                }

                if (rect == null) {
                    rect = createSignatureRectangle(doc, humanRect);
                }

                // Optional: certify
                // can be done only if version is at least 1.5 and if not already set
                // doing this on a PDF/A-1b file fails validation by Adobe preflight (PDFBOX-3821)
                // PDF/A-1b requires PDF version 1.4 max, so don't increase the version on such files.
                if (doc.getVersion() >= 1.5f && accessPermissions == 0) {
                    SigUtils.setMDPPermission(doc, signature, 2);
                }

                if (acroForm != null && acroForm.getNeedAppearances() && acroForm.getFields().isEmpty()) {
                    // PDFBOX-3738 NeedAppearances true results in visible signature becoming invisible
                    // with Adobe Reader

                    // we can safely delete it if there are no fields
                    acroForm.getCOSObject().removeItem(COSName.NEED_APPEARANCES);
                    // note that if you've set MDP permissions, the removal of this item
                    // may result in Adobe Reader claiming that the document has been changed.
                    // and/or that field content won't be displayed properly.
                    // ==> decide what you prefer and adjust your code accordingly.

                }
                // default filter
                signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);

                // subfilter for basic and PAdES Part 2 signatures
                signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

                signature.setName("Name");
                signature.setLocation("Location");
                signature.setReason("Reason");

                // the signing date, needed for valid signature
                signature.setSignDate(Calendar.getInstance());

                // do not set SignatureInterface instance, if external signing used
                SignatureInterface signatureInterface = isExternalSigning() ? null : this;

                // register signature dictionary and sign interface

                signatureOptions.setVisualSignature(createVisualSignatureTemplate(doc, 1, rect));
                signatureOptions.setPage(0);
                doc.addSignature(signature, signatureInterface, signatureOptions);

                // write incremental (only for signing purpose)
                doc.saveIncremental(fos);

            }

        }
        catch(ImtechoUserException ex) {
            throw new ImtechoUserException("Exception occur while adding signature options", ex);
        }

    }

    /**
     * Create Rectangle Signature box
     * @param doc An Instance of PDDocument
     * @param humanRect An instance of humanRect
     * @return An instance of PDRectangle
     */
    private PDRectangle createSignatureRectangle(PDDocument doc, Rectangle2D humanRect)
    {
        float x = (float) humanRect.getX();
        float y = (float) humanRect.getY();
        float width = (float) humanRect.getWidth();
        float height = (float) humanRect.getHeight();
        PDPage page = doc.getPage(0);
        PDRectangle pageRect = page.getCropBox();
        PDRectangle rect = new PDRectangle();
        // signing should be at the same position regardless of page rotation.
        switch (page.getRotation())
        {
            case 90:
                rect.setLowerLeftY(x);
                rect.setUpperRightY(x + width);
                rect.setLowerLeftX(y);
                rect.setUpperRightX(y + height);
                break;
            case 180:
                rect.setUpperRightX(pageRect.getWidth() - x);
                rect.setLowerLeftX(pageRect.getWidth() - x - width);
                rect.setLowerLeftY(y);
                rect.setUpperRightY(y + height);
                break;
            case 270:
                rect.setLowerLeftY(pageRect.getHeight() - x - width);
                rect.setUpperRightY(pageRect.getHeight() - x);
                rect.setLowerLeftX(pageRect.getWidth() - y - height);
                rect.setUpperRightX(pageRect.getWidth() - y);
                break;
            case 0:
            default:
                rect.setLowerLeftX(x);
                rect.setUpperRightX(x + width);
                rect.setLowerLeftY(pageRect.getHeight() - y - height);
                rect.setUpperRightY(pageRect.getHeight() - y);
                break;
        }
        return rect;
    }


    /**
     * create a template PDF document with empty signature and return it as a stream.
     * @param srcDoc A given File
     * @param pageNum A page number in PDF
     * @param rect An instance of PDRectangle
     * @return An InputStream of written data
     * @throws IOException
     */
    private InputStream createVisualSignatureTemplate(PDDocument srcDoc, int pageNum, PDRectangle rect) throws IOException, KeyStoreException
    {
        try (PDDocument doc = new PDDocument())
        {
            PDPage page = new PDPage(srcDoc.getPage(pageNum).getMediaBox());
            doc.addPage(page);
            PDAcroForm acroForm = new PDAcroForm(doc);
            doc.getDocumentCatalog().setAcroForm(acroForm);
            PDSignatureField signatureField = new PDSignatureField(acroForm);
            PDAnnotationWidget widget = signatureField.getWidgets().get(0);
            List<PDField> acroFormFields = acroForm.getFields();
            acroForm.setSignaturesExist(true);
            acroForm.setAppendOnly(true);
            acroForm.getCOSObject().setDirect(true);
            acroFormFields.add(signatureField);

            widget.setRectangle(rect);

            // from PDVisualSigBuilder.createHolderForm()
            PDStream stream = new PDStream(doc);
            PDFormXObject form = new PDFormXObject(stream);
            PDResources res = new PDResources();
            form.setResources(res);
            form.setFormType(1);
            PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
            float height = bbox.getHeight();
            Matrix initialScale = null;
            switch (srcDoc.getPage(pageNum).getRotation())
            {
                case 90:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(1));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(), bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 180:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(2));
                    break;
                case 270:
                    form.setMatrix(AffineTransform.getQuadrantRotateInstance(3));
                    initialScale = Matrix.getScaleInstance(bbox.getWidth() / bbox.getHeight(), bbox.getHeight() / bbox.getWidth());
                    height = bbox.getWidth();
                    break;
                case 0:
                default:
                    break;
            }
            form.setBBox(bbox);
            PDFont font = PDType1Font.HELVETICA_BOLD;

            // from PDVisualSigBuilder.createAppearanceDictionary()
            PDAppearanceDictionary appearance = new PDAppearanceDictionary();
            appearance.getCOSObject().setDirect(true);
            PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
            appearance.setNormalAppearance(appearanceStream);
            widget.setAppearance(appearance);

            try (PDPageContentStream cs = new PDPageContentStream(doc, appearanceStream))
            {
                // for 90Â° and 270Â° scale ratio of width / height
                // not really sure about this
                // why does scale have no effect when done in the form matrix???
                if (initialScale != null)
                {
                    cs.transform(initialScale);
                }

                // show background (just for debugging, to see the rect size + position)

                cs.setNonStrokingColor(Color.WHITE);

               //  For making Transparent bg

                cs.addRect(-5000, -5000, 10000, 10000);
                cs.fill();

                // show background image
                // save and restore graphics if the image is too large and needs to be scaled
                cs.saveGraphicsState();

                if (imageFile != null) {
                    cs.transform(Matrix.getScaleInstance(0.25f, 0.25f));
                    PDImageXObject img = PDImageXObject.createFromFileByExtension(imageFile, doc);
//                  change width and height here also
                    cs.drawImage(img, 430, 0,130,260);
                    cs.restoreGraphicsState();
                }



                Map<String,String> p12Details = new HashMap<>();
                KeyStore keystore1 = this.getKeystore();
                Enumeration<String> e = keystore1.aliases();
                while (e.hasMoreElements()) {
                    String alias = e.nextElement();
                    X509Certificate c = (X509Certificate) keystore1.getCertificate(alias);
                    Principal subject = c.getSubjectDN();
                    String[] subjectArray = subject.toString().split(",");
                    for (String s : subjectArray) {
                        String[] str = s.trim().split("=");
                        String key = str[0];
                        String value = str[1];
                        p12Details.put(key, value);
                    }
                }

//              following will be key for p12 file data access
//              CN - Certi Name , OU - Orgainisation Unit , O - oragainisation , L - Location , ST- State/city , C - country code
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss 'IST'");
                Date date = new Date();
                // show text
                float fontSize = 7;
                float leading = fontSize * 1.5f;
                cs.beginText();
                cs.setFont(font, fontSize);
                cs.setNonStrokingColor(Color.black);
                cs.newLineAtOffset(fontSize, height - leading);
                cs.setLeading(leading);
                cs.showText("Digitally Signed By");
                cs.newLine();
                cs.showText(p12Details.get("CN"));
                cs.newLine();
                cs.showText(p12Details.get("OU"));
                cs.newLine();
                cs.showText(p12Details.get("O"));
                cs.newLine();
                cs.showText("Date : " + formatter.format(date));
                cs.newLine();
                cs.showText("Location : " + p12Details.get("L"));
                cs.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    /**
     * Find an existing signature (assumed to be empty). You will usually not need this.
     * @param acroForm An Instance of PDAcroForm
     * @param sigFieldName A SignField Name
     * @return An instance of PDSignature
     */

    private PDSignature findExistingSignature(PDAcroForm acroForm, String sigFieldName)
    {
        PDSignature signature = null;
        PDSignatureField signatureField;
        if (acroForm != null)
        {
            signatureField = (PDSignatureField) acroForm.getField(sigFieldName);
            if (signatureField != null)
            {
                // retrieve signature dictionary
                signature = signatureField.getSignature();
                if (signature == null)
                {
                    signature = new PDSignature();
                    // after solving PDFBOX-3524
                    // signatureField.setValue(signature)
                    // until then:
                    signatureField.getCOSObject().setItem(COSName.V, signature);
                }
                else
                {
                    throw new IllegalStateException("The signature field " + sigFieldName + " is already signed.");
                }
            }
        }
        return signature;
    }



    public static void createDigitalSignature(String p12FilePassword,String p12FilePath,String dsImageFile, String inFilePath, String outFilePath) {
        try {

            String tsaUrl = null;
            // External signing is needed if you are using an external signing service, e.g. to sign
            // several files at once.
            boolean externalSig = false;
            //      password of .p12 file
            char[] password = p12FilePassword.toCharArray();
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(new FileInputStream(p12FilePath), password);
            File documentFile = new File(inFilePath);

            CreateSignatureServiceImpl signing = new CreateSignatureServiceImpl(keystore, password.clone());

            signing.setKeystore(keystore);

            File signedDocumentFile = new File(outFilePath);
            signing.setExternalSigning(externalSig);
            Rectangle2D humanRect = null;
            if(dsImageFile != null && dsImageFile.length() > 0) {
                signing.setImageFile(new File(dsImageFile));
                humanRect = new Rectangle2D.Float(410, 650, 145, 70);

            }
            else{
                humanRect = new Rectangle2D.Float(465, 650, 95, 70);
            }


            signing.signPDF(documentFile, signedDocumentFile, humanRect, tsaUrl, "SignatureField");

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            throw new ImtechoUserException("Error while setting up strcute for Signature ", ex);
        }
    }

}
