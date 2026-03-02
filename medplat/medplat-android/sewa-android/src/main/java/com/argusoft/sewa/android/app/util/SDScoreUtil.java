package com.argusoft.sewa.android.app.util;

import com.argusoft.sewa.android.app.constants.RchConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prateek on 8/2/19
 */
public class SDScoreUtil {

    private SDScoreUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static Map<Integer, String> sdScoreMapForBoys = new HashMap<>();

    static {
        //minus4~minus3~minus2~minus1~median
        sdScoreMapForBoys.put(45, "1.7~1.9~2~2.2~2.4");
        sdScoreMapForBoys.put(46, "1.8~2~2.2~2.4~2.6");
        sdScoreMapForBoys.put(47, "2~2.1~2.3~2.5~2.8");
        sdScoreMapForBoys.put(48, "2.1~2.3~2.5~2.7~2.9");
        sdScoreMapForBoys.put(49, "2.2~2.4~2.6~2.9~3.1");
        sdScoreMapForBoys.put(50, "2.4~2.6~2.8~3~3.3");
        sdScoreMapForBoys.put(51, "2.5~2.7~3~3.2~3.5");
        sdScoreMapForBoys.put(52, "2.7~2.9~3.2~3.5~3.8");
        sdScoreMapForBoys.put(53, "2.9~3.1~3.4~3.7~4");
        sdScoreMapForBoys.put(54, "3.1~3.3~3.6~3.9~4.3");
        sdScoreMapForBoys.put(55, "3.3~3.6~3.8~4.2~4.5");
        sdScoreMapForBoys.put(56, "3.5~3.8~4.1~4.4~4.8");
        sdScoreMapForBoys.put(57, "3.7~4~4.3~4.7~5.1");
        sdScoreMapForBoys.put(58, "3.9~4.3~4.6~5~5.4");
        sdScoreMapForBoys.put(59, "4.1~4.5~4.8~5.3~5.7");
        sdScoreMapForBoys.put(60, "4.3~4.7~5.1~5.5~6");
        sdScoreMapForBoys.put(61, "4.5~4.9~5.3~5.8~6.3");
        sdScoreMapForBoys.put(62, "4.7~5.1~5.6~6~6.5");
        sdScoreMapForBoys.put(63, "4.9~5.3~5.8~6.2~6.8");
        sdScoreMapForBoys.put(64, "5.1~5.5~6~6.5~7");
        sdScoreMapForBoys.put(65, "5.3~5.7~6.2~6.7~7.3");
        sdScoreMapForBoys.put(66, "5.5~5.9~6.4~6.9~7.5");
        sdScoreMapForBoys.put(67, "5.6~6.1~6.6~7.1~7.7");
        sdScoreMapForBoys.put(68, "5.8~6.3~6.8~7.3~8");
        sdScoreMapForBoys.put(69, "6~6.5~7~7.6~8.2");
        sdScoreMapForBoys.put(70, "6.1~6.6~7.2~7.8~8.4");
        sdScoreMapForBoys.put(71, "6.3~6.8~7.4~8~8.6");
        sdScoreMapForBoys.put(72, "6.4~7~7.6~8.2~8.9");
        sdScoreMapForBoys.put(73, "6.6~7.2~7.7~8.4~9.1");
        sdScoreMapForBoys.put(74, "6.7~7.3~7.9~8.6~9.3");
        sdScoreMapForBoys.put(75, "6.9~7.5~8.1~8.8~9.5");
        sdScoreMapForBoys.put(76, "7~7.6~8.3~8.9~9.7");
        sdScoreMapForBoys.put(77, "7.2~7.8~8.4~9.1~9.9");
        sdScoreMapForBoys.put(78, "7.3~7.9~8.6~9.3~10.1");
        sdScoreMapForBoys.put(79, "7.4~8.1~8.7~9.5~10.3");
        sdScoreMapForBoys.put(80, "7.6~8.2~8.9~9.6~10.4");
        sdScoreMapForBoys.put(81, "7.7~8.4~9.1~9.8~10.6");
        sdScoreMapForBoys.put(82, "7.9~8.5~9.2~10~10.8");
        sdScoreMapForBoys.put(83, "8~8.7~9.4~10.2~11");
        sdScoreMapForBoys.put(84, "8.2~8.9~9.6~10.4~11.3");
        sdScoreMapForBoys.put(85, "8.4~9.1~9.8~10.6~11.5");
        sdScoreMapForBoys.put(86, "8.6~9.3~10~10.8~11.7");
        sdScoreMapForBoys.put(87, "8.9~9.6~10.4~11.2~12.2");
        sdScoreMapForBoys.put(88, "9.1~9.8~10.6~11.5~12.4");
        sdScoreMapForBoys.put(89, "9.3~10~10.8~11.7~12.6");
        sdScoreMapForBoys.put(90, "9.4~10.2~11~11.9~12.9");
        sdScoreMapForBoys.put(91, "9.6~10.4~11.2~12.1~13.1");
        sdScoreMapForBoys.put(92, "9.8~10.6~11.4~12.3~13.4");
        sdScoreMapForBoys.put(93, "9.9~10.8~11.6~12.6~13.6");
        sdScoreMapForBoys.put(94, "10.1~11~11.8~12.8~13.8");
        sdScoreMapForBoys.put(95, "10.3~11.1~12~13~14.1");
        sdScoreMapForBoys.put(96, "10.4~11.3~12.2~13.2~14.3");
        sdScoreMapForBoys.put(97, "10.6~11.5~12.4~13.4~14.6");
        sdScoreMapForBoys.put(98, "10.8~11.7~12.6~13.7~14.8");
        sdScoreMapForBoys.put(99, "11~11.9~12.9~13.9~15.1");
        sdScoreMapForBoys.put(100, "11.2~12.1~13.1~14.2~15.4");
        sdScoreMapForBoys.put(101, "11.3~12.3~13.3~14.4~15.6");
        sdScoreMapForBoys.put(102, "11.5~12.5~13.6~14.7~15.9");
        sdScoreMapForBoys.put(103, "11.7~12.8~13.8~14.9~16.2");
        sdScoreMapForBoys.put(104, "11.9~13~14~15.2~16.5");
        sdScoreMapForBoys.put(105, "12.1~13.2~14.3~15.5~16.8");
        sdScoreMapForBoys.put(106, "12.3~13.4~14.5~15.8~17.2");
        sdScoreMapForBoys.put(107, "12.5~13.7~14.8~16.1~17.5");
        sdScoreMapForBoys.put(108, "12.7~13.9~15.1~16.4~17.8");
        sdScoreMapForBoys.put(109, "12.9~14.1~15.3~16.7~18.2");
        sdScoreMapForBoys.put(110, "13.2~14.4~15.6~17~18.5");
        sdScoreMapForBoys.put(111, "13.4~14.6~15.9~17.3~18.9");
        sdScoreMapForBoys.put(112, "13.6~14.9~16.2~17.6~19.2");
        sdScoreMapForBoys.put(113, "13.8~15.2~16.5~18~19.6");
        sdScoreMapForBoys.put(114, "14.1~15.4~16.8~18.3~20");
        sdScoreMapForBoys.put(115, "14.3~15.7~17.1~18.6~20.4");
        sdScoreMapForBoys.put(116, "14.6~16~17.4~19~20.8");
        sdScoreMapForBoys.put(117, "14.8~16.2~17.7~19.3~21.2");
        sdScoreMapForBoys.put(118, "15~16.5~18~19.7~21.6");
        sdScoreMapForBoys.put(119, "15.3~16.8~18.3~20~22");
        sdScoreMapForBoys.put(120, "15.5~17.1~18.6~20.4~22.4");
    }

    private static Map<Integer, String> sdScoreMapForGirls = new HashMap<>();

    static {
        sdScoreMapForGirls.put(45, "1.7~1.9~2.1~2.3~2.5");
        sdScoreMapForGirls.put(46, "1.9~2~2.2~2.4~2.6");
        sdScoreMapForGirls.put(47, "2~2.2~2.4~2.6~2.8");
        sdScoreMapForGirls.put(48, "2.1~2.3~2.5~2.7~3");
        sdScoreMapForGirls.put(49, "2.2~2.4~2.6~2.9~3.2");
        sdScoreMapForGirls.put(50, "2.4~2.6~2.8~3.1~3.4");
        sdScoreMapForGirls.put(51, "2.5~2.8~3~3.3~3.6");
        sdScoreMapForGirls.put(52, "2.7~2.9~3.2~3.5~3.8");
        sdScoreMapForGirls.put(53, "2.8~3.1~3.4~3.7~4");
        sdScoreMapForGirls.put(54, "3~3.3~3.6~3.9~4.3");
        sdScoreMapForGirls.put(55, "3.2~3.5~3.8~4.2~4.5");
        sdScoreMapForGirls.put(56, "3.4~3.7~4~4.4~4.8");
        sdScoreMapForGirls.put(57, "3.6~3.9~4.3~4.6~5.1");
        sdScoreMapForGirls.put(58, "3.8~4.1~4.5~4.9~5.4");
        sdScoreMapForGirls.put(59, "3.9~4.3~4.7~5.1~5.6");
        sdScoreMapForGirls.put(60, "4.1~4.5~4.9~5.4~5.9");
        sdScoreMapForGirls.put(61, "4.3~4.7~5.1~5.6~6.1");
        sdScoreMapForGirls.put(62, "4.5~4.9~5.3~5.8~6.4");
        sdScoreMapForGirls.put(63, "4.7~5.1~5.5~6~6.6");
        sdScoreMapForGirls.put(64, "4.8~5.3~5.7~6.3~6.9");
        sdScoreMapForGirls.put(65, "5~5.5~5.9~6.5~7.1");
        sdScoreMapForGirls.put(66, "5.1~5.6~6.1~6.7~7.3");
        sdScoreMapForGirls.put(67, "5.3~5.8~6.3~6.9~7.5");
        sdScoreMapForGirls.put(68, "5.5~6~6.5~7.1~7.7");
        sdScoreMapForGirls.put(69, "5.6~6.1~6.7~7.3~8");
        sdScoreMapForGirls.put(70, "5.8~6.3~6.9~7.5~8.2");
        sdScoreMapForGirls.put(71, "5.9~6.5~7~7.7~8.4");
        sdScoreMapForGirls.put(72, "6~6.6~7.2~7.8~8.6");
        sdScoreMapForGirls.put(73, "6.2~6.8~7.4~8~8.8");
        sdScoreMapForGirls.put(74, "6.3~6.9~7.5~8.2~9");
        sdScoreMapForGirls.put(75, "6.5~7.1~7.7~8.4~9.1");
        sdScoreMapForGirls.put(76, "6.6~7.2~7.8~8.5~9.3");
        sdScoreMapForGirls.put(77, "6.7~7.4~8~8.7~9.5");
        sdScoreMapForGirls.put(78, "6.9~7.5~8.2~8.9~9.7");
        sdScoreMapForGirls.put(79, "7~7.7~8.3~9.1~9.9");
        sdScoreMapForGirls.put(80, "7.1~7.8~8.5~9.2~10.1");
        sdScoreMapForGirls.put(81, "7.3~8~8.7~9.4~10.3");
        sdScoreMapForGirls.put(82, "7.5~8.1~8.8~9.6~10.5");
        sdScoreMapForGirls.put(83, "7.6~8.3~9~9.8~10.7");
        sdScoreMapForGirls.put(84, "7.8~8.5~9.2~10.1~11");
        sdScoreMapForGirls.put(85, "8~8.7~9.4~10.3~11.2");
        sdScoreMapForGirls.put(86, "8.1~8.9~9.7~10.5~11.5");
        sdScoreMapForGirls.put(87, "8.4~9.2~10~10.9~11.9");
        sdScoreMapForGirls.put(88, "8.6~9.4~10.2~11.1~12.1");
        sdScoreMapForGirls.put(89, "8.8~9.6~10.4~11.4~12.4");
        sdScoreMapForGirls.put(90, "9~9.8~10.6~11.6~12.6");
        sdScoreMapForGirls.put(91, "9.1~10~10.9~11.8~12.9");
        sdScoreMapForGirls.put(92, "9.3~10.2~11.1~12~13.1");
        sdScoreMapForGirls.put(93, "9.5~10.4~11.3~12.3~13.4");
        sdScoreMapForGirls.put(94, "9.7~10.6~11.5~12.5~13.6");
        sdScoreMapForGirls.put(95, "9.8~10.8~11.7~12.7~13.9");
        sdScoreMapForGirls.put(96, "10~10.9~11.9~12.9~14.1");
        sdScoreMapForGirls.put(97, "10.2~11.1~12.1~13.2~14.4");
        sdScoreMapForGirls.put(98, "10.4~11.3~12.3~13.4~14.7");
        sdScoreMapForGirls.put(99, "10.5~11.5~12.5~13.7~14.9");
        sdScoreMapForGirls.put(100, "10.7~11.7~12.8~13.9~15.2");
        sdScoreMapForGirls.put(101, "10.9~12~13~14.2~15.5");
        sdScoreMapForGirls.put(102, "11.1~12.2~13.3~14.5~15.8");
        sdScoreMapForGirls.put(103, "11.3~12.4~13.5~14.7~16.1");
        sdScoreMapForGirls.put(104, "11.5~12.6~13.8~15~16.4");
        sdScoreMapForGirls.put(105, "11.8~12.9~14~15.3~16.8");
        sdScoreMapForGirls.put(106, "12~13.1~14.3~15.6~17.1");
        sdScoreMapForGirls.put(107, "12.2~13.4~14.6~15.9~17.5");
        sdScoreMapForGirls.put(108, "12.4~13.7~14.9~16.3~17.8");
        sdScoreMapForGirls.put(109, "12.7~13.9~15.2~16.6~18.2");
        sdScoreMapForGirls.put(110, "12.9~14.2~15.5~17~18.6");
        sdScoreMapForGirls.put(111, "13.2~14.5~15.8~17.3~19");
        sdScoreMapForGirls.put(112, "13.5~14.8~16.2~17.7~19.4");
        sdScoreMapForGirls.put(113, "13.7~15.1~16.5~18~19.8");
        sdScoreMapForGirls.put(114, "14~15.4~16.8~18.4~20.2");
        sdScoreMapForGirls.put(115, "14.3~15.7~17.2~18.8~20.7");
        sdScoreMapForGirls.put(116, "14.5~16~17.5~19.2~21.1");
        sdScoreMapForGirls.put(117, "14.8~16.3~17.8~19.6~21.5");
        sdScoreMapForGirls.put(118, "15.1~16.6~18.2~19.9~22");
        sdScoreMapForGirls.put(119, "15.4~16.9~18.5~20.3~22.4");
        sdScoreMapForGirls.put(120, "15.6~17.3~18.9~20.7~22.8");
    }

    public static String calculateSDScore(Integer height, Float weight, String gender) {
        if (gender.equals("M") || gender.equals("Male")) {
            String s = sdScoreMapForBoys.get(height);
            if (s != null) {
                String[] split = s.split("~");
                if (height == 0 || weight == 0F) {
                    return RchConstants.SD_SCORE_CANNOT_BE_CALCULATED;
                } else if (weight <= Float.parseFloat(split[0])) {
                    return RchConstants.SD_SCORE_SD4;
                } else if (weight <= Float.parseFloat(split[1])) {
                    return RchConstants.SD_SCORE_SD3;
                } else if (weight <= Float.parseFloat(split[2])) {
                    return RchConstants.SD_SCORE_SD2;
                } else if (weight <= Float.parseFloat(split[3])) {
                    return RchConstants.SD_SCORE_SD1;
                } else if (weight <= Float.parseFloat(split[4])) {
                    return RchConstants.SD_SCORE_MEDIAN;
                } else {
                    return RchConstants.SD_SCORE_NONE;
                }
            }
        }

        if (gender.equals("F") || gender.equals("Female")) {
            String s = sdScoreMapForGirls.get(height);
            if (s != null) {
                String[] split = s.split("~");
                if (height == 0 || weight == 0F) {
                    return RchConstants.SD_SCORE_CANNOT_BE_CALCULATED;
                } else if (weight <= Float.parseFloat(split[0])) {
                    return RchConstants.SD_SCORE_SD4;
                } else if (weight <= Float.parseFloat(split[1])) {
                    return RchConstants.SD_SCORE_SD3;
                } else if (weight <= Float.parseFloat(split[2])) {
                    return RchConstants.SD_SCORE_SD2;
                } else if (weight <= Float.parseFloat(split[3])) {
                    return RchConstants.SD_SCORE_SD1;
                } else if (weight <= Float.parseFloat(split[4])) {
                    return RchConstants.SD_SCORE_MEDIAN;
                } else {
                    return RchConstants.SD_SCORE_NONE;
                }
            }
        }

        return null;
    }

    public static String getSDScoreForDisplay(String sdScore) {
        if (sdScore != null && !sdScore.isEmpty()) {
            switch (sdScore) {
                case "SD4":
                    return "Less than -4";
                case "SD3":
                    return "-4 to -3";
                case "SD2":
                    return "-3 to -2";
                case "SD1":
                    return "-2 to -1";
                case "MEDIAN":
                    return "MEDIAN";
                default:
                    return "NONE";
            }
        }
        return "NONE";
    }
}
