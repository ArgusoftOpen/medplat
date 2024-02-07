package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberInvestigationDao;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.MemberInvestigationDetail;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class MemberInvestigationDaoImpl extends GenericDaoImpl<MemberInvestigationDetail, Integer> implements MemberInvestigationDao {
    @Override
    public List<MemberInvestigationDetail> findByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberInvestigationDetail> criteriaQuery = criteriaBuilder.createQuery(MemberInvestigationDetail.class);
        Root<MemberInvestigationDetail> root = criteriaQuery.from(MemberInvestigationDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberInvestigationDetail.Fields.MEMBER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberInvestigationDetail.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberInvestigationDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
