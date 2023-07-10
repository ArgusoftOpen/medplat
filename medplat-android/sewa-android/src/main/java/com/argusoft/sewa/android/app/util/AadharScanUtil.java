package com.argusoft.sewa.android.app.util;

import android.text.Spannable;
import android.text.SpannableString;

import com.argusoft.sewa.android.app.exception.DataException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Prateek on 04-03-2022.
 */
public class AadharScanUtil {

    private AadharScanUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static final int NUMBER_OF_PARAMS_IN_SECURE_QR_CODE = 15;

    public static final String DOB_DATE_FORMAT_1 = "dd-MM-yyyy";
    public static final String DOB_DATE_FORMAT_2 = "dd/MM/yyyy";
    public static final String DOB_DATE_FORMAT_3 = "yyyy/MM/dd";

    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String GENDER = "gender";  // M/F
    public static final String YOB = "yob";  // year of birth
    public static final String CO = "co";  // "D/O: Father Name"
    public static final String HOUSE = "house";
    public static final String STREET = "street";
    public static final String LM = "lm";  // address
    public static final String LOC = "loc";  // neighborhood
    public static final String VTC = "vtc";  // village
    public static final String PO = "po";  // city
    public static final String DIST = "dist";  // district
    public static final String SUBDIST = "subdist";  // region
    public static final String STATE = "state";
    public static final String PC = "pc";  // postal code
    public static final String DOB = "dob";  // date of birth

    public static Map<String, String> getAadharScanDataMap(String rawString) {
        Document dom = getDocumentFromXml(rawString);

        if (dom != null) {
            return getDataFromQrXml(dom);
        } else if (rawString.matches("\\d{12}")) {
            return getDataFromQrUid(rawString);
        } else if (rawString.matches("[0-9]*")) {
            return getDataFromSecureQr(rawString);
        } else {
            throw new DataException("Not an aadhar card", 100);
        }
    }

    private static Document getDocumentFromXml(String rawString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document dom;
        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Replace </?xml... with <?xml...
            if (rawString.startsWith("</?")) {
                rawString = rawString.replaceFirst("</\\?", "<?");
            }
            // Replace <?xml...?"> with <?xml..."?>
            rawString = rawString.replaceFirst("^<\\?xml ([^>]+)\\?\">", "<?xml $1\"?>");
            //parse using builder to get DOM representation of the XML file
            dom = db.parse(new ByteArrayInputStream(rawString.getBytes(StandardCharsets.UTF_8)));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            dom = null;
        }
        return dom;
    }

    private static Map<String, String> getDataFromQrXml(Document dom) {
        Node node = dom.getChildNodes().item(0);
        NamedNodeMap attributes = node.getAttributes();

        Map<String, String> aadharDetailsMap = new HashMap<>();

        aadharDetailsMap.put(UID, getAttributeOrEmptyString(attributes, UID));
        aadharDetailsMap.put(NAME, getAttributeOrEmptyString(attributes, NAME));
        aadharDetailsMap.put(GENDER, getAttributeOrEmptyString(attributes, GENDER));
        aadharDetailsMap.put(YOB, getAttributeOrEmptyString(attributes, YOB));
        aadharDetailsMap.put(CO, getAttributeOrEmptyString(attributes, CO));
        aadharDetailsMap.put(HOUSE, getAttributeOrEmptyString(attributes, HOUSE));
        aadharDetailsMap.put(STREET, getAttributeOrEmptyString(attributes, STREET));
        aadharDetailsMap.put(LM, getAttributeOrEmptyString(attributes, LM));
        aadharDetailsMap.put(LOC, getAttributeOrEmptyString(attributes, LOC));
        aadharDetailsMap.put(VTC, getAttributeOrEmptyString(attributes, VTC));
        aadharDetailsMap.put(PO, getAttributeOrEmptyString(attributes, PO));
        aadharDetailsMap.put(DIST, getAttributeOrEmptyString(attributes, DIST));
        aadharDetailsMap.put(SUBDIST, getAttributeOrEmptyString(attributes, SUBDIST));
        aadharDetailsMap.put(STATE, getAttributeOrEmptyString(attributes, STATE));
        aadharDetailsMap.put(PC, getAttributeOrEmptyString(attributes, PC));
        aadharDetailsMap.put(DOB, getAttributeOrEmptyString(attributes, DOB));

        aadharDetailsMap.values().removeAll(Collections.singleton(null));
        aadharDetailsMap.values().removeAll(Collections.singleton(""));

        return aadharDetailsMap;
    }

    private static Map<String, String> getDataFromQrUid(String rawString) {
        Map<String, String> aadharDetailsMap = new HashMap<>();
        aadharDetailsMap.put(UID, rawString);
        return aadharDetailsMap;
    }

    private static Map<String, String> getDataFromSecureQr(String rawString) {
        Map<String, String> aadharDetailsMap = new HashMap<>();

        byte[] msgInBytes = null;
        try {
            msgInBytes = decompressByteArray(new BigInteger(rawString).toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (msgInBytes == null) {
            return aadharDetailsMap;
        }

        int[] delimiters = locateDelimiters(msgInBytes);
        String referenceId = getValueInRange(msgInBytes, delimiters[0] + 1, delimiters[1]);
        aadharDetailsMap.put(UID, referenceId.substring(0, 4));
        aadharDetailsMap.put(NAME, getValueInRange(msgInBytes, delimiters[1] + 1, delimiters[2]));
        aadharDetailsMap.put(DOB, getValueInRange(msgInBytes, delimiters[2] + 1, delimiters[3]));
        aadharDetailsMap.put(GENDER, getValueInRange(msgInBytes, delimiters[3] + 1, delimiters[4]));
        aadharDetailsMap.put(CO, getValueInRange(msgInBytes, delimiters[4] + 1, delimiters[5]));
        aadharDetailsMap.put(DIST, getValueInRange(msgInBytes, delimiters[5] + 1, delimiters[6]));
        aadharDetailsMap.put(LM, getValueInRange(msgInBytes, delimiters[6] + 1, delimiters[7]));
        aadharDetailsMap.put(HOUSE, getValueInRange(msgInBytes, delimiters[7] + 1, delimiters[8]));
        aadharDetailsMap.put(LOC, getValueInRange(msgInBytes, delimiters[8] + 1, delimiters[9]));
        aadharDetailsMap.put(PC, getValueInRange(msgInBytes, delimiters[9] + 1, delimiters[10]));
        aadharDetailsMap.put(PO, getValueInRange(msgInBytes, delimiters[10] + 1, delimiters[11]));
        aadharDetailsMap.put(STATE, getValueInRange(msgInBytes, delimiters[11] + 1, delimiters[12]));
        aadharDetailsMap.put(STREET, getValueInRange(msgInBytes, delimiters[12] + 1, delimiters[13]));
        aadharDetailsMap.put(SUBDIST, getValueInRange(msgInBytes, delimiters[13] + 1, delimiters[14]));
        aadharDetailsMap.put(VTC, getValueInRange(msgInBytes, delimiters[14] + 1, delimiters[15]));

        aadharDetailsMap.values().removeAll(Collections.singleton(null));
        aadharDetailsMap.values().removeAll(Collections.singleton(""));

        return aadharDetailsMap;
    }

    private static String getAttributeOrEmptyString(NamedNodeMap attributes, String attributeName) {
        Node node = attributes.getNamedItem(attributeName);
        if (node != null) {
            return node.getTextContent();
        } else {
            return "";
        }
    }

    private static int[] locateDelimiters(byte[] msgInBytes) {
        int[] delimiters = new int[NUMBER_OF_PARAMS_IN_SECURE_QR_CODE + 1];
        int index = 0;
        int delimiterIndex;
        for (int i = 0; i <= NUMBER_OF_PARAMS_IN_SECURE_QR_CODE; i++) {
            delimiterIndex = getNextDelimiterIndex(msgInBytes, index);
            delimiters[i] = delimiterIndex;
            index = delimiterIndex + 1;
        }
        return delimiters;
    }


    private static String getValueInRange(byte[] msgInBytes, int start, int end) {
        return new String(Arrays.copyOfRange(msgInBytes, start, end), StandardCharsets.ISO_8859_1);
    }

    private static int getNextDelimiterIndex(byte[] msgInBytes, int index) {
        int i = index;
        for (; i < msgInBytes.length; i++) {
            if (msgInBytes[i] == -1) {
                break;
            }
        }
        return i;
    }

    private static byte[] decompressByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        GZIPInputStream gzIn = new GZIPInputStream(byteIn);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        int res = 0;
        byte[] buf = new byte[1024];
        while (res >= 0) {
            res = gzIn.read(buf, 0, buf.length);
            if (res > 0) {
                byteOut.write(buf, 0, res);
            }
        }
        return byteOut.toByteArray();
    }

    public static Date formatDate(String rawDateString) {
        if (rawDateString == null || rawDateString.equals("")) {
            return null;
        }

        String[] possibleFormats = {AadharScanUtil.DOB_DATE_FORMAT_1, AadharScanUtil.DOB_DATE_FORMAT_2, AadharScanUtil.DOB_DATE_FORMAT_3};
        Date date;
        for (String fromFormatPattern : possibleFormats) {
            try {
                SimpleDateFormat fromFormat = new SimpleDateFormat(fromFormatPattern);
                date = fromFormat.parse(rawDateString);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.e(AadharScanUtil.class.getSimpleName(), "Expected dob to be in dd/mm/yyyy or yyyy-mm-dd format, got " + rawDateString);
        return null;
    }

    public static String formatGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        String lowercaseGender = gender.toLowerCase();
        switch (lowercaseGender) {
            case "male":
            case "m":
                return "M";
            case "female":
            case "f":
                return "F";
            case "other":
            case "o":
            case "transgender":
            case "t":
                return "T";
            default:
                return gender;
        }
    }

    public static Spannable getAadharTextToBeDisplayedAfterScan(Map<String, String> aadharScannedMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : aadharScannedMap.entrySet()) {
            switch (stringStringEntry.getKey()) {
                case UID:
                    stringBuilder.append(UtilBean.getMyLabel("Aadhar Number"));
                    stringBuilder.append(" : ");
                    String maskAadhaarNumber = "xxxxxxxx" + stringStringEntry.getValue().substring(stringStringEntry.getValue().length() - 4);
                    stringBuilder.append(maskAadhaarNumber);
                    stringBuilder.append("\n");
                    break;
                case NAME:
                    stringBuilder.append(UtilBean.getMyLabel("Name"));
                    stringBuilder.append(" : ");
                    stringBuilder.append(stringStringEntry.getValue());
                    stringBuilder.append("\n");
                    break;
                case YOB:
                    stringBuilder.append(UtilBean.getMyLabel("Year Of Birth"));
                    stringBuilder.append(" : ");
                    stringBuilder.append(stringStringEntry.getValue());
                    stringBuilder.append("\n");
                    break;
                case DOB:
                    stringBuilder.append(UtilBean.getMyLabel("Date Of Birth"));
                    stringBuilder.append(" : ");
                    stringBuilder.append(stringStringEntry.getValue());
                    stringBuilder.append("\n");
                    break;
                case PC:
                    stringBuilder.append(UtilBean.getMyLabel("Pincode"));
                    stringBuilder.append(" : ");
                    stringBuilder.append(stringStringEntry.getValue());
                    stringBuilder.append("\n");
                    break;
                default:
            }
        }

        return new SpannableString(stringBuilder.toString());
    }

}
