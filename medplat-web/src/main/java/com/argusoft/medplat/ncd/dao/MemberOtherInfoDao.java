/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.ncd.model.MemberOtherInfo;

import java.util.Date;

/**
 *
 * <p>
 * Define methods for member other info.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface MemberOtherInfoDao extends GenericDao<MemberOtherInfo, Integer> {

    /**
     * Store dell API response details.
     * @param requestStartDate Start date of request.
     * @param requestEndDate End date of request.
     * @param locationName Location name.
     * @param locationId Location id.
     * @param response Response details.
     * @param enrolled Enrolled.
     */
    void storeDellApiResponseDetails(Date requestStartDate, Date requestEndDate, String locationName, Integer locationId, String response, Integer enrolled);
}

