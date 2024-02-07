/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberOtherInfoDao;
import com.argusoft.medplat.ncd.model.MemberOtherInfo;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 *
 * <p>
 * Implementation of methods defined in member other info dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberOtherInfoDaoImpl extends GenericDaoImpl<MemberOtherInfo, Integer> implements MemberOtherInfoDao {
    /**
     * {@inheritDoc}
     */
    @Override
    public void storeDellApiResponseDetails(Date requestStartDate, Date requestEndDate, String locationName, Integer locationId, String response, Integer enrolled) {
        String query = "insert into ncd_dell_api_push_response(location_id,location_name,request_start_date,request_end_date,response, enrolled) "
                + " values (:locationId, :locationName, :requestStartDate, :requestEndDate, :response, :enrolled );";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sQLQuery = session.createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationId);
        sQLQuery.setParameter("locationName", locationName);
        sQLQuery.setParameter("requestStartDate", requestStartDate);
        sQLQuery.setParameter("requestEndDate", requestEndDate);
        sQLQuery.setParameter("response", response);
        sQLQuery.setParameter("enrolled", enrolled);
        sQLQuery.executeUpdate();
    }
}
