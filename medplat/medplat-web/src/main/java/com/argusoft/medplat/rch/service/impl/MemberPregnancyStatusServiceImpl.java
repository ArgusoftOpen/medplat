package com.argusoft.medplat.rch.service.impl;

import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.mobile.dto.LogInRequestParamDetailDto;
import com.argusoft.medplat.mobile.dto.LoggedInUserPrincipleDto;
import com.argusoft.medplat.rch.dao.MemberPregnancyStatusDao;
import com.argusoft.medplat.rch.service.MemberPregnancyStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Define services for pregnancy status.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Service
@Transactional
public class MemberPregnancyStatusServiceImpl implements MemberPregnancyStatusService {

    @Autowired
    private MemberPregnancyStatusDao memberPregnancyStatusDao;

    @Autowired
    private SystemConfigurationDao systemConfigurationDao;

    /**
     * {@inheritDoc}
     */
    public void retrievePregnancyStatusForMobile(LoggedInUserPrincipleDto data, LogInRequestParamDetailDto param) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        SystemConfiguration configuration = systemConfigurationDao.retrieveSystemConfigurationByKey("rch_pregnancy_analytics_last_schedule_date");
        String keyValue = configuration.getKeyValue();
        try {
            Date parse = sdf.parse(keyValue);
            if (param.getLastUpdateDateForPregnancyStatus() == null || parse.after(new Date(param.getLastUpdateDateForPregnancyStatus()))) {
                data.setPregnancyStatus(memberPregnancyStatusDao.retrievePregnancyStatusForMobile(param.getUserId()));
                data.setLastPregnancyStatusDate(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
