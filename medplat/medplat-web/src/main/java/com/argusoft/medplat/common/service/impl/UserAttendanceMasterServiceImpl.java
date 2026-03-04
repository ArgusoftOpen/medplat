
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.UserAttendanceMasterDao;
import com.argusoft.medplat.common.model.UserAttendanceInfo;
import com.argusoft.medplat.common.service.UserAttendanceMasterService;
import com.argusoft.medplat.exception.ImtechoMobileException;
import com.argusoft.medplat.mobile.constants.MessagesConstant;
import com.argusoft.medplat.mobile.dto.MobileRequestParamDto;
import com.argusoft.medplat.mobile.service.MobileFhsService;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Implements methods of UserAttendanceMasterService
 * @author rahul
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class UserAttendanceMasterServiceImpl implements UserAttendanceMasterService {

    @Autowired
    private MobileFhsService mobileFhsService;

    @Autowired
    private UserAttendanceMasterDao attendanceMasterDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer markAttendanceForTheDay(MobileRequestParamDto mobileRequestParamDto) {
        UserMaster userMaster;
        if (mobileRequestParamDto.getToken() != null) {
            userMaster = mobileFhsService.isUserTokenValid(mobileRequestParamDto.getToken());
            if (userMaster == null) {
                throw new ImtechoMobileException("Your session is expired, Please login again", 1);
            }
        } else {
            throw new ImtechoMobileException("Your session is expired, Please login again", 1);
        }

        if (mobileRequestParamDto.getGpsRecords() != null) {
            UserAttendanceInfo userAttendanceInfo = new UserAttendanceInfo();
            Date today = new Date();
            userAttendanceInfo.setLocations(mobileRequestParamDto.getGpsRecords());
            userAttendanceInfo.setUserId(userMaster.getId());
            userAttendanceInfo.setAttendanceDate(today);
            userAttendanceInfo.setStartTime(today);

            try {
                attendanceMasterDao.create(userAttendanceInfo);
                return userAttendanceInfo.getId();
            } catch (Exception e) {
                throw new ImtechoMobileException(e.getMessage(), 1);
            }
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeAttendanceForTheDay(MobileRequestParamDto mobileRequestParamDto) {
        if (mobileRequestParamDto.getToken() != null) {
            UserMaster userMaster = mobileFhsService.isUserTokenValid(mobileRequestParamDto.getToken());
            if (userMaster == null) {
                throw new ImtechoMobileException(MessagesConstant.SESSION_EXPIRED_MESSAGE, 1);
            }
        } else {
            throw new ImtechoMobileException(MessagesConstant.SESSION_EXPIRED_MESSAGE, 1);
        }

        if (mobileRequestParamDto.getGpsRecords() != null && mobileRequestParamDto.getAttendanceId() != null) {
            UserAttendanceInfo userAttendanceInfo = attendanceMasterDao.retrieveById(mobileRequestParamDto.getAttendanceId());
            if (userAttendanceInfo != null) {
                userAttendanceInfo.setLocations(mobileRequestParamDto.getGpsRecords());
                userAttendanceInfo.setEndTime(new Date());

                try {
                    attendanceMasterDao.update(userAttendanceInfo);
                } catch (Exception e) {
                    throw new ImtechoMobileException(e.getMessage(), 1);
                }
            } else {
                throw new ImtechoMobileException(MessagesConstant.MESSAGE_INFO_NOT_FOUND, 1);
            }
        }
    }

}
