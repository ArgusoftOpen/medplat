
package com.argusoft.medplat.common.service;


import com.argusoft.medplat.mobile.dto.MobileRequestParamDto;

/**
 * <p>
 *     Define methods for one time password
 * </p>
 * @author kunjan
 * @since 27/08/2020 4:30
 */
public interface OtpService {
     /**
      * Generates one time password and send sms to given mobile number
      *
      * @param mobileNumber A mobile number
      * @param type         Message Type
      */
     void generateOtp(String mobileNumber, String type);

     /**
      * Generates one time password and send sms to mobile number from MobileRequestParamDto
      * @param mobileRequestParamDto An instance of MobileRequestParamDto
      */
     void generateOtpTecho(MobileRequestParamDto mobileRequestParamDto);

     /**
      * Verify given otp against otp of given mobile number
      * @param mobileNumber A mobile number for otp verification
      * @param otp An otp to verify
      * @return Whether given otp is correct or not
      */
     boolean verifyOtp(String mobileNumber, String otp);

     /**
      * Verify given otp against otp of MobileRequestParamDto
      * @param mobileRequestParamDto An instance of MobileRequestParamDto
      * @return Whether given otp is correct or not
      */
     boolean verifyOtpTecho(MobileRequestParamDto mobileRequestParamDto);

     /**
      * Invalidates given otp for given mobile number
      * @param mobileNumber A mobile number
      * @param otp An otp
      */
     void invalidateOtp(String mobileNumber, String otp);
}
