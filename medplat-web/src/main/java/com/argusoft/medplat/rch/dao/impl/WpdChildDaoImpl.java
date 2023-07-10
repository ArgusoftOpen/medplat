/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.rch.dao.WpdChildDao;
import com.argusoft.medplat.rch.model.VisitCommonFields;
import com.argusoft.medplat.rch.model.WpdChildMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in wpd child dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class WpdChildDaoImpl extends GenericDaoImpl<WpdChildMaster, Integer> implements WpdChildDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdChildMaster> getWpdChildbyMemberid(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<WpdChildMaster> criteriaQuery = criteriaBuilder.createQuery(WpdChildMaster.class);
        Root<WpdChildMaster> root = criteriaQuery.from(WpdChildMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(3).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getWpdChildExistsByWpdMotherId(Integer wpdMotherId) {
        String query = "select case when count(*) > 0 then true else false end from rch_wpd_child_master  where wpd_mother_id  = :wpdMotherId ;";
        NativeQuery<Boolean> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("wpdMotherId", wpdMotherId);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertQueryForPatchNotification(Integer memberId, Integer familyId, Date dob) {
        String query = "INSERT INTO event_mobile_notification_pending(notification_configuration_type_id, base_date, family_id, member_id, \n"
                + "created_by, created_on, modified_by, modified_on, is_completed, state, ref_code) values \n"
                + "('f51c8c4f-6b2b-4dcb-8e64-ada1a3044a67', :dob, :familyId, :memberId,-1,now(),-1,now(),false,'PENDING',-1);";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("dob", dob);
        sQLQuery.setParameter("memberId", memberId);
        sQLQuery.setParameter("familyId", familyId);
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdChildMaster> getWpdChildbyWpdMotherId(Integer wpdMotherId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(WpdChildMaster.Fields.WPD_MOTHER_ID), wpdMotherId));
            return predicates;
        });
    }

}
