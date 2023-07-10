/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.dao.ChildServiceDao;
import com.argusoft.medplat.rch.model.ChildServiceMaster;
import com.argusoft.medplat.rch.model.VisitCommonFields;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in child service dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class ChildServiceDaoImpl extends GenericDaoImpl<ChildServiceMaster, Integer> implements ChildServiceDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildServiceMaster> retrieveByMemberId(Integer childId, int limit) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildServiceMaster> criteriaQuery = criteriaBuilder.createQuery(ChildServiceMaster.class);
        Root<ChildServiceMaster> root = criteriaQuery.from(ChildServiceMaster.class);
        Predicate childIdEqual = criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), childId);
        criteriaQuery.select(root).where(criteriaBuilder.and(childIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(limit).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveMedicalComplications(Integer memberId) {
        String query = "select string_agg(value,',') from listvalue_field_value_detail where id in\n"
                + "(select diseases from rch_child_service_diseases_rel where child_service_id in \n"
                + "(select id from rch_child_service_master where member_id =" + memberId + "))";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<String> sQLQuery = session.createNativeQuery(query);
        return sQLQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChildServiceMaster getLastChildVisit(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ChildServiceMaster> criteriaQuery = criteriaBuilder.createQuery(ChildServiceMaster.class);
        Root<ChildServiceMaster> root = criteriaQuery.from(ChildServiceMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ChildServiceMaster.Fields.SERVICE_DATE)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSuspectedCpEntry(Integer memberId, Integer locationId, Integer childServiceId, Integer userId) {
        String query = "insert into rch_child_cp_suspects(member_id,location_id,child_service_id,"
                + "created_by,created_on,modified_by,modified_on) values (:memberId,:locationId,:childServiceId,"
                + ":userId,:date,:userId,:date)";
        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("locationId", locationId);
        sQLQuery.setParameter("memberId", memberId);
        sQLQuery.setParameter("childServiceId", childServiceId);
        sQLQuery.setParameter("userId", userId);
        sQLQuery.setParameter("date", new Date());
        sQLQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture() {
        String query = "select member_id as \"memberId\", max(mobile_start_date) as \"serviceDate\" from rch_child_service_master \n"
                + "where service_date > current_timestamp group by member_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberServiceDateDto> q = session.createNativeQuery(query);
        q.addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("serviceDate", StandardBasicTypes.TIMESTAMP);

        return q.setResultTransformer(Transformers.aliasToBean(MemberServiceDateDto.class)).list();
    }
}
