package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdOphthalmologistDao;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;
import com.argusoft.medplat.ncd.model.NcdOphthalmologistData;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class NcdOphthalmologistDaoImpl extends GenericDaoImpl<NcdOphthalmologistData, Integer> implements NcdOphthalmologistDao {
    @Override
    public NcdOphthalmologistData retrieveOphthalmologistReponseByDateAndMemberId(Integer memberId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NcdOphthalmologistData> criteriaQuery = criteriaBuilder.createQuery(NcdOphthalmologistData.class);
        Root<NcdOphthalmologistData> root = criteriaQuery.from(NcdOphthalmologistData.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(NcdOphthalmologistData.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(NcdCardiologistData.Fields.SCREENING_DATE)), screeningDate);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }
}
