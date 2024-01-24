/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberCbacDetailDao;
import com.argusoft.medplat.ncd.model.MemberCbacDetail;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in member CBAC details dao.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class MemberCbacDetailDaoImpl extends GenericDaoImpl<MemberCbacDetail, Integer> implements MemberCbacDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberCbacDetail> retrieveCbacDetailsForAsha(Integer userId, Date lastModifiedOn) {
        String query = "select id from imt_family where area_id in (select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE')";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter("userId", userId);
        List<Integer> familyIds = q.addScalar("id", StandardBasicTypes.INTEGER).list();
        if (familyIds != null && !familyIds.isEmpty()) {
            return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(root.get(MemberCbacDetail.Fields.FAMILY_ID).in(familyIds));
                if (lastModifiedOn != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityAuditInfo.Fields.MODIFIED_ON), lastModifiedOn));
                }
                return predicates;
            });
        } else {
            return Collections.emptyList();
        }
    }

}
