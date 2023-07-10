
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.SequenceDao;
import com.argusoft.medplat.common.databean.OtpDataBean;
import com.argusoft.medplat.common.service.OtpService;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.mobile.dto.MobileRequestParamDto;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Implements methods of OtpService
 * @author kunjan
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class OtpServiceImpl implements OtpService {

    private static final String SESSION_EXPIRED_MSG="Your session is expired, Please login again";

    @Autowired
    @Lazy
    private MobileFhsService mobileFhsService;
    
    @Autowired
    private SmsService smsService;

    @Autowired
    private SequenceDao sequenceDao;

    private static final String OTP_STRING = "Your code for verification in TeCHO+ application is #OTP#. DO NOT share this code with anyone.";
    private static final String VERIFICATION_CODE_STRING = "Your verification code for TeCHO+ is #OTP#. Please share this with your ANM/MPHW/ASHA to complete your Phone Verification and get useful messages regarding health services and information about benefit schemes on this number as text messages and on WhatsApp.";

    Random rand = new Random();

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateOtp(String mobileNumber, String messageType) {
        String otp = String.format("%04d", rand.nextInt(9999));
        sequenceDao.insertOtp(mobileNumber, otp);
        smsService.sendSms(mobileNumber, OTP_STRING.replace("#OTP#", otp), true, messageType);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void generateOtpTecho(MobileRequestParamDto mobileRequestParamDto) {
        if(mobileRequestParamDto.getToken() != null) {
            UserMaster userMaster = mobileFhsService.isUserTokenValid(mobileRequestParamDto.getToken());
            if(userMaster == null) {
                throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
            }
        } else {
            throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
        }

        String otp = String.format("%04d", rand.nextInt(9999));
        sequenceDao.insertOtp(mobileRequestParamDto.getMobileNumber(), otp);
        smsService.sendSms(mobileRequestParamDto.getMobileNumber(), VERIFICATION_CODE_STRING.replace("#OTP#", otp), true, "MOBILE_NUMBER_VERIFICATION_CODE");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyOtpTecho(MobileRequestParamDto mobileRequestParamDto) {
        if(mobileRequestParamDto.getToken() != null) {
            UserMaster userMaster = mobileFhsService.isUserTokenValid(mobileRequestParamDto.getToken());
            if(userMaster == null) {
                throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
            }
        } else {
            throw new ImtechoMobileException(SESSION_EXPIRED_MSG, 1);
        }

        if (mobileRequestParamDto.getMobileNumber().length() != 10) {
            return false;
        }
        OtpDataBean otpDataBean = sequenceDao.retrieveOtp(mobileRequestParamDto.getMobileNumber());
        if (otpDataBean == null ) {
            return false;
        }

        if (otpDataBean.getCount() <= 2 && otpDataBean.getOtp().equals(mobileRequestParamDto.getOtp())) {
            return true;
        } else {
            sequenceDao.updateTryCount(mobileRequestParamDto.getMobileNumber(),otpDataBean.getCount()+1);
            if (otpDataBean.getCount() > 2) {
                invalidateOtp(mobileRequestParamDto.getMobileNumber(), mobileRequestParamDto.getOtp());
                throw new ImtechoMobileException("OTP is expired. Please re-generate new OTP", 1);
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyOtp(String mobileNumber, String otp) {
        if (mobileNumber.length() != 10) {
            return false;
        }
        OtpDataBean otpDataBean = sequenceDao.retrieveOtp(mobileNumber);
        if (otpDataBean == null ) {
            return false;
        }

        if (otpDataBean.getCount() <= 2 && otpDataBean.getOtp().equals(otp)) {
            return true;
        } else {
            sequenceDao.updateTryCount(mobileNumber,otpDataBean.getCount()+1);
            if (otpDataBean.getCount() > 2) {
                invalidateOtp(mobileNumber, otp);
                throw new ImtechoMobileException("OTP is expired. Please re-generate new OTP", 1);
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateOtp(String mobileNumber, String otp) {
        sequenceDao.invalidateOtp(mobileNumber);
    }

}
