package com.argusoft.medplat.ncd.dao.impl;


import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberFollowupDetailDao;
import com.argusoft.medplat.ncd.model.MemberFollowupDetail;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository
public class MemberFollowupDetailDaoImpl extends GenericDaoImpl<MemberFollowupDetail, Integer> implements MemberFollowupDetailDao {

    @Override
    public List<MemberFollowupDetail> getFollowUpByReferenceId(Integer referenceId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberFollowupDetail> criteriaQuery = criteriaBuilder.createQuery(MemberFollowupDetail.class);
        Root<MemberFollowupDetail> root = criteriaQuery.from(MemberFollowupDetail.class);
        Predicate referenceIdEqual = criteriaBuilder.equal(root.get("referenceId"), referenceId);
        criteriaQuery.select(root).where(criteriaBuilder.and(referenceIdEqual));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
