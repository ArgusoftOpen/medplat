package com.argusoft.sewa.android.app.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import com.argusoft.sewa.android.app.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.component.MyStaticComponents;
import com.argusoft.sewa.android.app.constants.FieldNameConstants;
import com.argusoft.sewa.android.app.constants.RelatedPropertyNameConstants;
import com.argusoft.sewa.android.app.core.impl.MorbiditiesIdentification;
import com.argusoft.sewa.android.app.datastructure.SharedStructureData;
import com.argusoft.sewa.android.app.morbidities.beans.BeneficiaryMorbidityDetails;
import com.argusoft.sewa.android.app.morbidities.constants.MorbiditiesConstant;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author alpeshkyada
 */
public class SewaUtil {

    private SewaUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static final int COLOR_RED = Color.parseColor("#FF0000");
    public static final int COLOR_GREEN = Color.parseColor("#014A08");
    public static final int COLOR_YELLOW = Color.parseColor("#ffd500");
    public static final int COLOR_ANSWER = Color.parseColor("#686f7c");
    public static final int COLOR_1 = Color.parseColor("#9b234d");
    public static final int COLOR_2 = Color.parseColor("#ce5700");
    public static final int COLOR_3 = Color.parseColor("#629a0a");
    public static final int COLOR_4 = Color.parseColor("#009be3");
    public static final String COLOR_MORBIDITY_GREEN_CODE = "M";
    public static final int COLOR_MORBIDITY_GREEN = Color.parseColor("#014A08");
    public static final int COLOR_MORBIDITY_NORMAL = Color.parseColor("#000000");
    private static final String TAG = "SewaUtil";
    public static boolean isUserInTraining = false;
    public static String familyUnderstandInMorbidity;
    public static Toast toast;
    public static int CURRENT_THEME = R.style.techo_app;

    public static int getColorForMorbiditySims(String value) {
        if (value != null && value.equalsIgnoreCase(COLOR_MORBIDITY_GREEN_CODE)) {
            return COLOR_MORBIDITY_GREEN;
        } else {
            return COLOR_MORBIDITY_NORMAL;
        }
    }

    public static void setColor(TextView textView, String colorName) {
        int color = COLOR_ANSWER;
        String[] splitTextAns = null;
        List<Integer> textColors = null;
        if (colorName != null && (colorName.contains(MorbiditiesConstant.RED_COLOR_FULL) || colorName.contains(UtilBean.getMyLabel(MorbiditiesConstant.RED_COLOR_FULL))
                || colorName.contains(MorbiditiesConstant.YELLOW_COLOR_FULL) || colorName.contains(UtilBean.getMyLabel(MorbiditiesConstant.YELLOW_COLOR_FULL))
                || colorName.contains(MorbiditiesConstant.GREEN_COLOR_FULL) || colorName.contains(UtilBean.getMyLabel(MorbiditiesConstant.GREEN_COLOR_FULL)))) {
            splitTextAns = colorName.split(GlobalTypes.COMMA);
        }

        if (colorName != null && colorName.trim().length() > 0) {
            if (colorName.trim().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR_FULL)
                    || colorName.trim().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)
                    || colorName.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.RED_COLOR_FULL))) {
                color = COLOR_RED;
            } else if (colorName.trim().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR_FULL)
                    || colorName.trim().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR)
                    || colorName.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.YELLOW_COLOR_FULL))) {
                color = COLOR_YELLOW;
            } else if (colorName.trim().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR_FULL)
                    || colorName.trim().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR)
                    || colorName.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.GREEN_COLOR_FULL))) {
                color = COLOR_GREEN;
            } else if (colorName.trim().equalsIgnoreCase(SewaConstants.QUESTION_COLOR_1)) {
                color = COLOR_1;
            } else if (colorName.trim().equalsIgnoreCase(SewaConstants.QUESTION_COLOR_2)) {
                color = COLOR_2;
            } else if (colorName.trim().equalsIgnoreCase(SewaConstants.QUESTION_COLOR_3)) {
                color = COLOR_3;
            } else if (colorName.trim().equalsIgnoreCase(SewaConstants.QUESTION_COLOR_4)) {
                color = COLOR_4;
            } else if (splitTextAns != null && splitTextAns.length > 1) {
                textColors = new LinkedList<>();
                for (String split : splitTextAns) {
                    if (split.trim().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR_FULL)
                            || split.trim().equalsIgnoreCase(MorbiditiesConstant.RED_COLOR)
                            || split.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.RED_COLOR_FULL))) {
                        textColors.add(Color.RED);
                    } else if (split.trim().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR_FULL)
                            || split.trim().equalsIgnoreCase(MorbiditiesConstant.YELLOW_COLOR)
                            || split.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.YELLOW_COLOR_FULL))) {
                        textColors.add(Color.YELLOW);
                    } else if (split.trim().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR_FULL)
                            || split.trim().equalsIgnoreCase(MorbiditiesConstant.GREEN_COLOR)
                            || split.trim().equalsIgnoreCase(UtilBean.getMyLabel(MorbiditiesConstant.GREEN_COLOR_FULL))) {
                        textColors.add(Color.GREEN);
                    }
                }
            }
        }
        if (textView != null) {
            if (splitTextAns != null && splitTextAns.length > 0 && textColors != null && !textColors.isEmpty()) {
                Spannable span = new SpannableString(colorName);
                // set color for particular word acc. to malnutrition grade for multiple morbidities
                int clength = 0;
                for (int i = 0; i < splitTextAns.length; i++) {
                    span.setSpan(new ForegroundColorSpan(textColors.get(i)), clength, clength + splitTextAns[i].length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                    clength += splitTextAns[i].length() + 1; // +1 added for string separator
                }
                textView.setText(span);
            } else {
                textView.setTextColor(color);
            }
        }
    }

    public static List<BeneficiaryMorbidityDetails> showANCMorbidity(String ancRecord) {
        if (ancRecord != null) {
            MorbiditiesIdentification morbiditiesIdentification = new MorbiditiesIdentification();
            String nameOfBeneficiary = SharedStructureData.relatedPropertyHashTable.get(RelatedPropertyNameConstants.NAME_OF_BENEFICIARY);
            return morbiditiesIdentification.findAvailableANCMorbidities(ancRecord, nameOfBeneficiary);
        } else {
            Log.w(TAG, "Anc record could not found so Anc morbidity can not be checked");
        }
        return new ArrayList<>();
    }

    public static List<BeneficiaryMorbidityDetails> showCCMorbidity(String ccRecord) {
        if (ccRecord != null) {
            MorbiditiesIdentification morbiditiesIdentification = new MorbiditiesIdentification();
            String nameOfBeneficiary = SharedStructureData.relatedPropertyHashTable.get("nameOfBeneficiary");
            return morbiditiesIdentification.findAvailableChildCareMorbidities(ccRecord, nameOfBeneficiary);
        } else {
            Log.w(TAG, "Child-Care record could not found so CC morbidity can not be checked");
        }
        return new ArrayList<>();
    }

    public static List<BeneficiaryMorbidityDetails> showPNCMorbidity(String pncRecord) {
        if (pncRecord != null) {
            MorbiditiesIdentification morbiditiesIdentification = new MorbiditiesIdentification();
            String nameOfBeneficiary = SharedStructureData.relatedPropertyHashTable.get("nameOfBeneficiary");
            return morbiditiesIdentification.findAvailablePNCMorbidities(pncRecord, nameOfBeneficiary);
        } else {
            Log.w(TAG, "PNC Home Visit record could not found so PNC morbidity can not be checked");
        }
        return new ArrayList<>();
    }

    public static void generateToast(Context context, String msg) {
        if (context != null) {
            if (toast != null) {
                String displayedText = ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).getText().toString();
                if (displayedText.equalsIgnoreCase(msg)) {
                    toast.cancel();
                }
            }
            toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, UtilBean.getDpsAccordingScreenWidthHeight(context, 20));
            toast.setDuration(Toast.LENGTH_LONG);
            LinearLayout mainLayout = MyStaticComponents.getLinearLayout(context, -1, LinearLayout.VERTICAL, null);

            mainLayout.setBackgroundResource(R.drawable.my_toast_gradient);

            TextView textView = MyStaticComponents.generateQuestionView(null, null, context, msg);
            if (textView != null) {
                textView.setPadding(50, 20, 50, 20);
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                mainLayout.addView(textView);
                toast.setView(mainLayout);
                toast.show();
            }
        }
    }

    public static Map<String, String> parseXml(String inputXmlString)
            throws XmlPullParserException {

        if (inputXmlString.contains("<?xml")) {
            int startIndex = inputXmlString.indexOf("<?xml");
            int endIndex = inputXmlString.indexOf(">", startIndex);
            String extraTag = inputXmlString.substring(startIndex, endIndex + 1);
            Log.i(TAG, "#### Removing extra Tag " + extraTag);
            inputXmlString = inputXmlString.replace(extraTag, "");
        }
        Map<String, String> detailsMap = new LinkedHashMap<>();
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(inputXmlString)); // pass input whatever xml you have

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    Log.d(TAG, "Start tag " + xpp.getName());
                    int totalAttrs = xpp.getAttributeCount();
                    for (int i = 0; i < totalAttrs; i++) {
                        detailsMap.put(xpp.getAttributeName(i), xpp.getAttributeValue(i));
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "End tag " + xpp.getName());
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "Text " + xpp.getText()); // here you get the text from xml
                    // todo:
                }
                eventType = xpp.next();
            }
            Log.d(TAG, "End document");

        } catch (IOException e) {
            Log.e(TAG, null, e);
        }
        return detailsMap;
    }

    public static LinkedHashMap<String, String> setQrScanFilterData(String scanData) {
        LinkedHashMap<String, String> qrScanFilter = new LinkedHashMap<>();
        if (scanData != null) {
            String[] objects = scanData.replace("{","").replace("}","").split(",");
            for (String object : objects) {
                String[] keyValuePairs = object.split(":");
                if (keyValuePairs.length > 1) {
                    String[] keyValue = keyValuePairs[0].split("\"");
                    if (keyValue.length > 1) {
                        if(keyValue[1].equals(FieldNameConstants.FAMILY_ID)){
                            qrScanFilter.put(FieldNameConstants.FAMILY_ID,keyValuePairs[1].split("\"")[1]);
                        } else if (keyValue[1].equals("hid")) {
                            qrScanFilter.put(FieldNameConstants.HEALTH_ID, keyValuePairs[1].split("\"")[1]);
                        } else if (keyValue[1].equals("hidn")) {
                            qrScanFilter.put(FieldNameConstants.HEALTH_ID_NUMBER, keyValuePairs[1].split("\"")[1]);
                        }
                    }
                }
            }
            qrScanFilter.put("isQrCodeScan", "true");
        } else {
            qrScanFilter.put("isQrCodeScan", "false");
        }
        return qrScanFilter;
    }
}
