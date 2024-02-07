package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberInitialAssessmentDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Repository
public class MemberInitialAssessmentDaoImpl extends GenericDaoImpl<MemberInitialAssessmentDetail, Integer> implements MemberInitialAssessmentDao {

    @Override
    public MemberInitialAssessmentDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberInitialAssessmentDetail> criteriaQuery = cb.createQuery(MemberInitialAssessmentDetail.class);
        Root<MemberInitialAssessmentDetail> root = criteriaQuery.from(MemberInitialAssessmentDetail.class);
        criteriaQuery.orderBy(cb.desc(root.get("screeningDate")));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("id"),referralId));

        if (screeningDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("screeningDate"),screeningDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(screeningDate);
            cal.add(Calendar.DATE, 1);
            predicates.add(cb.lessThanOrEqualTo(root.get("screeningDate").as(Date.class),cal.getTime()));
        }
        criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        MemberInitialAssessmentDetail memberInitialAssessmentDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberInitialAssessmentDetail == null && screeningDate != null) {
            memberInitialAssessmentDetail = retrieveForReferralId(referralId, null);
            if (memberInitialAssessmentDetail == null) {
                memberInitialAssessmentDetail = new MemberInitialAssessmentDetail();
                memberInitialAssessmentDetail.setReferralId(referralId);
            }
        }
        return memberInitialAssessmentDetail;
    }

    @Override
    public List<MemberInitialAssessmentDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.HEALTH_INFRA_ID), healthInfraId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberInitialAssessmentDetail.Fields.REFERRED_ON)));
            return predicates;
        });
    }

    @Override
    public MemberInitialAssessmentDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberInitialAssessmentDetail> criteriaQuery = criteriaBuilder.createQuery(MemberInitialAssessmentDetail.class);
        Root<MemberInitialAssessmentDetail> root = criteriaQuery.from(MemberInitialAssessmentDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.SCREENING_DATE), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdOn")));
        return session.createQuery(criteriaQuery).getResultList().size() > 0 ? session.createQuery(criteriaQuery).getResultList().get(0) : null;
    }

    @Override
    public MemberInitialAssessmentDetail retrieveLastRecordByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberInitialAssessmentDetail> criteriaQuery = criteriaBuilder.createQuery(MemberInitialAssessmentDetail.class);
        Root<MemberInitialAssessmentDetail> root = criteriaQuery.from(MemberInitialAssessmentDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.MEMBER_ID), memberId);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberInitialAssessmentDetail.Fields.DONE_BY), DoneBy.MO);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberInitialAssessmentDetail.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberInitialAssessmentDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }
}
