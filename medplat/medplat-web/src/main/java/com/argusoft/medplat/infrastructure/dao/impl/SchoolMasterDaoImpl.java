/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.infrastructure.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.infrastructure.dao.SchoolMasterDao;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.infrastructure.model.SchoolEntity;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in school master dao.
 * </p>
 *
 * @author rahul
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class SchoolMasterDaoImpl extends GenericDaoImpl<SchoolEntity, Long> implements SchoolMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SchoolDataBean> retrieveAllSchoolForMobile(Date lastUpdatedOn) {
        String query = "select id as \"actualId\", name, code, grant_type as \"grantType\", school_type as \"schoolType\","
                + "is_primary_school as \"isPrimarySchool\", is_higher_secondary_school as \"isHigherSecondarySchool\","
                + "is_madresa as \"isMadresa\", is_gurukul as \"isGurukul\","
                + " location_id as \"locationId\", modified_on as \"modifiedOn\""
                + " from school_master";

        if (lastUpdatedOn != null) {
            query = query + " where modified_on > :lastModifiedOn";
        }

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<SchoolDataBean> sQLQuery = session.createNativeQuery(query)
                .addScalar("actualId", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("code", StandardBasicTypes.STRING)
                .addScalar("grantType", StandardBasicTypes.INTEGER)
                .addScalar("schoolType", StandardBasicTypes.INTEGER)
                .addScalar("isPrimarySchool", StandardBasicTypes.BOOLEAN)
                .addScalar("isHigherSecondarySchool", StandardBasicTypes.BOOLEAN)
                .addScalar("isMadresa", StandardBasicTypes.BOOLEAN)
                .addScalar("isGurukul", StandardBasicTypes.BOOLEAN)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);

        if (lastUpdatedOn != null) {
            sQLQuery.setParameter("lastModifiedOn", lastUpdatedOn);
        }
        return sQLQuery.setResultTransformer(Transformers.aliasToBean(SchoolDataBean.class)).list();
    }

}
