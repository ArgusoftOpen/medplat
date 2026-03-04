/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.util;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author subhash
 */
public class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("Utility class, not meant to be instantiated");
    }

    static Random rand = new Random();

    public static String getFiscalYear(Date date) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        int month = calendarDate.get(Calendar.MONTH);
        int year = calendarDate.get(Calendar.YEAR);

        year = (month < 3) ? year - 1 : year;
        return year + "-" + getLastTwoDigits(year);
    }

    public static int getYear(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        int month = calendarDate.get(Calendar.MONTH);
        int year = calendarDate.get(Calendar.YEAR);
        year = (month < 3) ? year - 1 : year ;
        return year;
    }

    public static String getLastTwoDigits(int year) {
        return String.format("%2d", (year % 100) + 1);
    }

    public static String getNextRegisterNo(Integer memberId) {
        return "124" + String.format("%09d", memberId);         // 1 for EC and PW, 24 for Gujarat
    }

    public static String randomNumber() {
        return String.format("%04d", rand.nextInt(9999));
    }

    public static String reduceLength(String text) {
        if (text != null) {
            if (text.length() > 50) {
                return text.substring(0, 50);
            } else {
                return text;
            }
        }
        return null;
    }

    public static String getSubDomainName(String url) {
        String domain = null;
        try {
            domain = new URL(url).getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (domain != null) {
            return domain.substring(0, domain.indexOf("."));
        }
        return null;
    }

    public static String getHostName(String url) {
        String hostname = null;
        try {
            hostname = new URL(url).getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostname;
    }

}
