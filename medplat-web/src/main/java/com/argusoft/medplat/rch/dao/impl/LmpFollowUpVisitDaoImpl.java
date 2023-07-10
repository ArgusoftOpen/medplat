/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.dao.LmpFollowUpVisitDao;
import com.argusoft.medplat.rch.model.LmpFollowUpVisit;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in lmp follow up visits dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class LmpFollowUpVisitDaoImpl extends GenericDaoImpl<LmpFollowUpVisit, Integer> implements LmpFollowUpVisitDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture() {
        String query = "select member_id as \"memberId\", max(mobile_start_date) as \"serviceDate\" from rch_lmp_follow_up \n"
                + "where service_date > current_timestamp group by member_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberServiceDateDto> q = session.createNativeQuery(query);
        q.addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("serviceDate", StandardBasicTypes.TIMESTAMP);

        return q.setResultTransformer(Transformers.aliasToBean(MemberServiceDateDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAnmolById(Integer id, String anmolId,String status, String code,int caseNo,String xml) {
        String query = "update anmol_lmp_follow_up_details set " +
                "anmol_registration_id = :anmol_registration_id, anmol_follow_up_status = :anmol_follow_up_status, " +
                "anmol_follow_up_wsdl_code = :anmol_follow_up_wsdl_code, anmol_follow_up_date = :anmol_follow_up_date, anmol_case_no = :anmol_case_no where id = :id ";
        NativeQuery q = getCurrentSession().createNativeQuery(query);
        q.setParameter("anmol_registration_id",anmolId);
        q.setParameter("anmol_follow_up_status",anmolId);
        q.setParameter("anmol_follow_up_wsdl_code",code);
        q.setParameter("anmol_follow_up_date",new Date());
        q.setParameter("anmol_case_no",caseNo);
        q.setParameter("id",id);
        q.executeUpdate();
    }

}
