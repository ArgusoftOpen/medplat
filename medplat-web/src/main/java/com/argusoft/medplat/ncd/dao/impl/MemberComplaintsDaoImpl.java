/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberComplaintsDao;
import com.argusoft.medplat.ncd.dto.MemberComplaintsDto;
import com.argusoft.medplat.ncd.model.MemberComplaints;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * <p>
 * Implementation of methods defined in member complaints dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberComplaintsDaoImpl extends GenericDaoImpl<MemberComplaints, Integer> implements MemberComplaintsDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberComplaintsDto> retrieveByMemberId(Integer memberId) {
        String sql = "select comp.id,comp.complaint,usr.first_name ||  ' ' || usr.last_name as doneBy ,comp.created_on as doneOn from ncd_member_complaints   comp,um_user  usr where "
                + "member_id=:memberId "
                + "and comp.created_by=usr.id";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberComplaintsDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("complaint", StandardBasicTypes.STRING)
                .addScalar("doneBy", StandardBasicTypes.STRING)
                .addScalar("doneOn", StandardBasicTypes.DATE)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(MemberComplaintsDto.class))
                .list();
    }

}
