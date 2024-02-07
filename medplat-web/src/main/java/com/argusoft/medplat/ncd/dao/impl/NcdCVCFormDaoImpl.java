package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdCVCFormDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.NcdCVCForm;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Repository
public class NcdCVCFormDaoImpl extends GenericDaoImpl<NcdCVCForm, Integer> implements NcdCVCFormDao {
    @Override
    public NcdCVCForm retrieveCVCDetailsByMemberAndDate(Integer memberId, Date date, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NcdCVCForm> criteriaQuery = criteriaBuilder.createQuery(NcdCVCForm.class);
        Root<NcdCVCForm> root = criteriaQuery.from(NcdCVCForm.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(NcdCVCForm.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(NcdCVCForm.Fields.SCREENING_DATE), date);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(NcdCVCForm.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }
}
