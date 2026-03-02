package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.databean.OtpDataBean;

/**
 * <p>Defines database method for sequence</p>
 * @author kunjan
 * @since 31/08/2020 10:30
 */
public interface SequenceDao {
    /**
     * Returns a next value of given sequence name
     * @param sequenceName A name of sequence
     * @return A next vale of sequence
     */
    Integer getNextValueBySequenceName(String sequenceName);

    /**
     * Checks number of response in last 24 hours
     * @return true if number id less than 250 else false
     */
    boolean checkNumberOfResponsesInLast2Hours();

    /**
     * Store otp for given mobile number
     * @param mobileNumber A mobile number
     * @param otp A value of otp
     */
    void insertOtp(String mobileNumber, String otp);

    /**
     * Returns a instance of OthDataBean of given mobile number
     * @param mobileNumber A mobile number
     * @return A instance of OthDataBean
     */
    OtpDataBean retrieveOtp(String mobileNumber);

    /**
     *
     * @param mobileNumber A mobile number
     */
    void invalidateOtp(String mobileNumber);

    /**
     *
     * @param mobileNumber A mobile number
     * @param count A value for count
     */
    void updateTryCount(String mobileNumber,Integer count);

    /**
     * Logs push notification of my techo
     * @param token A string token value
     * @param heading A heading of notification
     * @param message A message of notification
     * @param exception An exception of notification
     * @param response A response of notification
     */
    void logNotificationPush(String token, String heading, String message, String exception, String response);
}
