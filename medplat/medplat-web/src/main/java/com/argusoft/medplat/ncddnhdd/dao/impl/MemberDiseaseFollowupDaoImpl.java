/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberDiseaseFollowupDao;
import com.argusoft.medplat.ncddnhdd.dto.MemberDiseaseFollowupDto;
import com.argusoft.medplat.ncddnhdd.enums.DiseaseCode;
import com.argusoft.medplat.ncddnhdd.model.MemberDiseaseFollowup;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;

/**
 *
 * <p>
 * Implementation of methods defined in member disease follow up dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberDiseaseFollowupDaoImpl extends GenericDaoImpl<MemberDiseaseFollowup, Integer> implements MemberDiseaseFollowupDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDiseaseFollowupDto> retrieveNextFollowUp(Integer memberId, Integer userId) {
        Properties params = new Properties();
        params.put("enumClass", DiseaseCode.class.getName());
        params.put("type", "12");

        Type myEnumType = sessionFactory.getTypeHelper().custom(EnumType.class, params);
        String sql = "select min(follow_up_date) as followupDate, disease_code as diseaseCode from ncd_member_referral where\n"
                + "cast (follow_up_date as date) > CURRENT_DATE - 1\n"
                + "and member_id = :memberId and created_by = :userId\n"
                + "and state = 'PENDING'\n"
                + "group by disease_code";

        Session session = sessionFactory.getCurrentSession();
        return session.createNativeQuery(sql)
                .addScalar("followupDate", StandardBasicTypes.DATE)
                .addScalar("diseaseCode", myEnumType)
                .setParameter("memberId", memberId)
                .setParameter("userId", userId)
                .setResultTransformer(Transformers.aliasToBean(MemberDiseaseFollowupDto.class))
                .list();
    }
}
