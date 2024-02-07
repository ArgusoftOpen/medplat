package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdSpecicalistMasterDao;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.model.NcdSpecialistMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class NcdSpecialistMasterDaoImpl extends GenericDaoImpl<NcdSpecialistMaster, Long> implements NcdSpecicalistMasterDao {
    @Override
    public NcdSpecialistMaster retrieveByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NcdSpecialistMaster> criteriaQuery = criteriaBuilder.createQuery(NcdSpecialistMaster.class);
        Root<NcdSpecialistMaster> root = criteriaQuery.from(NcdSpecialistMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(NcdSpecialistMaster.Fields.MEMBER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        return session.createQuery(criteriaQuery).uniqueResult();
    }
}
