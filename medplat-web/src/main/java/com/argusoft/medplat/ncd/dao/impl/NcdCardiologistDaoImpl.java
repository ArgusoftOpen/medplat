package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdCardiologistDao;
import com.argusoft.medplat.ncd.dto.NcdCardiologistDto;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import com.argusoft.medplat.ncd.model.NcdCardiologistData;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Repository
public class NcdCardiologistDaoImpl extends GenericDaoImpl<NcdCardiologistData, Integer> implements NcdCardiologistDao {
    @Override
    public NcdCardiologistData retrieveCardiologistReponseByMemberIdAndDate(Integer memberId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NcdCardiologistData> criteriaQuery = criteriaBuilder.createQuery(NcdCardiologistData.class);
        Root<NcdCardiologistData> root = criteriaQuery.from(NcdCardiologistData.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(NcdCardiologistData.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(NcdCardiologistData.Fields.SCREENING_DATE)), screeningDate);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }
}
