/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.dao.PncMasterDao;
import com.argusoft.medplat.rch.model.PncMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Implementation of methods define in pnc master dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class PncMasterDaoImpl extends GenericDaoImpl<PncMaster, Integer> implements PncMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture() {
        String query = "select member_id as \"memberId\", max(mobile_start_date) as \"serviceDate\" from rch_pnc_master \n"
                + "where service_date > current_timestamp group by member_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberServiceDateDto> q = session.createNativeQuery(query);
        q.addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("serviceDate", StandardBasicTypes.TIMESTAMP);

        return q.setResultTransformer(Transformers.aliasToBean(MemberServiceDateDto.class)).list();
    }
}
